package org.example.pracainzynierska.repositories;

import org.example.pracainzynierska.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT u from User u")
    List<User> findAll();

    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findById(@Param("id") Long id);

    @Modifying
    @Query(value = "INSERT INTO User u (u.password, u.email, u.name, u.surname) VALUES (:password, :email, :name, :surname);", nativeQuery = true)
    User create(@Param("password") String password,
                @Param("email") String email,
                @Param("name") String name,
                @Param("surname") String surname);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findUserByEmail(@Param("email") String email);

    @Modifying
    @Query("UPDATE User u SET u.name = :name, u.surname = :surname, u.password = :password WHERE u.id = :id AND u.email = :email ")
    void update(@Param("password") String password,
                @Param("email") String email,
                @Param("name") String name,
                @Param("surname") String surname,
                @Param("id") Long id);

    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :id")
    void delete(@Param("id") Long id);


}
