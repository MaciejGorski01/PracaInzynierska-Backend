package org.example.pracainzynierska.repositories;

import org.example.pracainzynierska.mappers.UserMapper;
import org.example.pracainzynierska.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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
        String sql = "SELECT u from User u";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    public User findById(String id){
        String sql = "SELECT u FROM User u WHERE u.id = ?";
        return jdbcTemplate.queryForObject(sql, new UserMapper(), id);
    }

    public void create(String password, String email, String name, String surname){
        String sql = "INSERT INTO User u (u.password, u.email, u.name, u.surname) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, password, email, name, surname);
    }

    public User findUserByEmail(String email){
        String sql = "SELECT u FROM User u WHERE u.email = ?";
        return jdbcTemplate.queryForObject(sql, new UserMapper(), email);
    }

    public void update(String password, String email, String name, String surname, String id){
        String sql = "UPDATE User u SET u.name = :name, u.surname = :surname, u.password = :password WHERE u.id = :id";
        jdbcTemplate.update(sql, password, email, name, surname, id);
    }

    public void delete(String id){
        String sql = "DELETE FROM User u WHERE u.id = :id";
        jdbcTemplate.update(sql, id);
    }

}
