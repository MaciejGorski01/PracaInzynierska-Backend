package org.example.pracainzynierska.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class User {

    Long id;

    @NotEmpty(message = "This field can't be empty!")
    @Size( min = 7, message = "Password must be at least 8 characters long!")
    String password;

    @NotEmpty(message = "This field can't be empty!")
    @Email(message = "This field must be a valid email address!")
    String email;

    @NotEmpty(message = "This field can't be empty!")
    String name;

    @NotEmpty(message = "This field can't be empty!")
    String surname;

}
