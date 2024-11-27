package org.example.pracainzynierska.repositories;

import org.example.pracainzynierska.mappers.UserMapper;
import org.example.pracainzynierska.models.User;
import org.json.JSONObject;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAll(){
        String sql = "SELECT json_agg(row_to_json(\"User\")) AS users_json FROM \"User\";";
        return jdbcTemplate.queryForObject(sql, new UserMapper());
    }

    public List<User> findById(String id){
        String sql = "SELECT json_agg(row_to_json(\"User\")) AS users_json FROM \"User\" WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new UserMapper(), id);
    }

    public void create(String id, JSONObject jsonObject){
        String sql = "INSERT INTO \"User\" (id, password, email, name, surname) " +
                "SELECT ?, password, email, name, surname " +
                "FROM json_to_record(?::json) AS temp(id text, password text, email text, name text, surname text)";
        jdbcTemplate.update(sql, id, jsonObject.toString());
    }

    public List<User> findUserByEmail(String email){
        String sql = "SELECT json_agg(row_to_json(\"User\")) AS users_json FROM \"User\" WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new UserMapper(), email);
    }

    public void update(JSONObject jsonObject, String id){
        String sql = "UPDATE \"User\" " +
                "SET password = temp.password, email = temp.email, name = temp.name, surname = temp.surname " +
                "FROM json_to_record(?::json) AS temp(password text, email text, name text, surname text) " +
                "WHERE \"User\".id = ?; ";
        jdbcTemplate.update(sql, jsonObject.toString(), id);
    }

    public void delete(String id){
        String sql = "DELETE FROM \"User\" WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

}
