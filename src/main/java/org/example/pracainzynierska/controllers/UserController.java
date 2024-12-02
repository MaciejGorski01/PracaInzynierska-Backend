package org.example.pracainzynierska.controllers;

import org.example.pracainzynierska.dtos.UserDto;
import org.example.pracainzynierska.exceptions.EntityNotFoundException;
import org.example.pracainzynierska.exceptions.UserAlreadyExistsException;
import org.example.pracainzynierska.functions.JsonValidator;
import org.example.pracainzynierska.functions.PasswordEncodeSecurity;
import org.example.pracainzynierska.functions.Security;
import org.example.pracainzynierska.services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/users")
public class UserController {

    UserService userService;
    JsonValidator jsonValidator = new JsonValidator("schemas/user_schema.json");

    @Autowired
    private PasswordEncodeSecurity passwordEncodeSecurity;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> allUsers = userService.findAllUsers();
        List<UserDto> allUsersImmutable = List.of(allUsers.toArray(new UserDto[]{}));

        if (allUsersImmutable.isEmpty()) {
            throw new EntityNotFoundException("Users");
        }

        return ResponseEntity.ok(allUsersImmutable);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id) {
        try {
            UserDto userDto = userService.findUserById(id);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            throw new EntityNotFoundException("User");
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        try {
            UserDto userDto = userService.findUserByEmail(email);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            throw new EntityNotFoundException("User");
        }
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json);
        jsonObject.put("password", passwordEncodeSecurity.encoder().encode(jsonObject.getString("password")));
        jsonValidator.validator(jsonObject);

        try {
            userService.addUser(jsonObject);
            return ResponseEntity.status(HttpStatus.CREATED).body(jsonObject.toMap());

        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("User_email_key")) {
                throw new UserAlreadyExistsException("There is an account with that email adress: " + jsonObject.getString("email"));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Data integrity violation"));
        }

    }


    @PutMapping("/{id}")
    public ResponseEntity<?> editUser(@RequestBody String json, @PathVariable String id) {
        UserDto existingUserDto;

        try {
            existingUserDto = userService.findUserById(id);
        } catch (NoSuchElementException e) {
            throw new EntityNotFoundException("User");
        }

        JSONObject jsonObject = new JSONObject(json);

        JSONObject updatedUser = new JSONObject();

        updatedUser.put("password", jsonObject.has("password") ? passwordEncodeSecurity.encoder().encode(jsonObject.getString("password")) : existingUserDto.password());
        updatedUser.put("email", jsonObject.has("email") ? jsonObject.getString("email") : existingUserDto.email());
        updatedUser.put("name", jsonObject.has("name") ? jsonObject.getString("name") : existingUserDto.name());
        updatedUser.put("surname", jsonObject.has("surname") ? jsonObject.getString("surname") : existingUserDto.surname());

        jsonValidator.validator(updatedUser);

        try {
            userService.updateUser(updatedUser, id);
            return ResponseEntity.ok(updatedUser.toMap());
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("User_email_key")) {
                throw new UserAlreadyExistsException("There is an account with that email adress: " + updatedUser.getString("email"));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Data integrity violation"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            UserDto user = userService.findUserById(id);

            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new EntityNotFoundException("User to delete");
        }
    }


}
