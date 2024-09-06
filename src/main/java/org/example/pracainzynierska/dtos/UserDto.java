package org.example.pracainzynierska.dtos;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserDto(
        String id,

        @NotEmpty(message = "This field can't be empty!")
        @Size( min = 7, message = "Password must be at least 8 characters long!")
        String password,

        @NotEmpty(message = "This field can't be empty!")
        @Email(message = "This field must be a valid email address!")
        String email,

        @NotEmpty(message = "This field can't be empty!")
        String name,

        @NotEmpty(message = "This field can't be empty!")
        String surname
) { }
