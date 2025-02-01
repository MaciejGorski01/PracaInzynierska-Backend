package org.example.pracainzynierska.repositories;

import org.example.pracainzynierska.mappers.UserMapper;
import org.example.pracainzynierska.models.User;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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

    public int create(String id, JSONObject jsonObject){
        String sql = "SELECT insert_user_from_json(?, ?::json);";
        Optional<Integer> result = Optional.ofNullable(
                jdbcTemplate.queryForObject(sql, Integer.class, id, jsonObject.toString())
        );

        return result.orElse(0);
    }

    public List<User> findUserByEmail(String email){
        String sql = "SELECT json_agg(row_to_json(\"User\")) AS users_json FROM \"User\" WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new UserMapper(), email);
    }

    public int update(JSONObject jsonObject, String id){
        String sql = "SELECT update_user_from_json(?::json, ?);";
        Optional<Integer> result = Optional.ofNullable(
                jdbcTemplate.queryForObject(sql, Integer.class, jsonObject.toString(), id)
        );

        return result.orElse(0);
    }

    public void delete(String id){
        String sql = "DELETE FROM \"User\" WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

}
