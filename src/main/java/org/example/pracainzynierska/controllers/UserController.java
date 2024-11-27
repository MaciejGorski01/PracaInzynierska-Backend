package org.example.pracainzynierska.controllers;

import org.example.pracainzynierska.dtos.NoteDto;
import org.example.pracainzynierska.dtos.UserDto;
import org.example.pracainzynierska.functions.JsonValidator;
import org.example.pracainzynierska.services.UserService;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {

    UserService userService;
    JsonValidator jsonValidator = new JsonValidator("schemas/user_schema.json");

    public UserController(UserService userService){
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> allUsers = userService.findAllUsers();
        List<UserDto> allUsersImmutable = List.of(allUsers.toArray(new UserDto[]{}));

        if (allUsersImmutable.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(allUsersImmutable);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id){
        try{
            UserDto userDto = userService.findUserById(id);
            return ResponseEntity.ok(userDto);
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
        try{
            UserDto userDto = userService.findUserByEmail(email);
            return ResponseEntity.ok(userDto);
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            jsonValidator.validator(jsonObject);
            userService.addUser(jsonObject);

            return ResponseEntity.status(HttpStatus.CREATED).body(jsonObject.toMap());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> editUser(@RequestBody String json, @PathVariable String id){
        try{
            UserDto existingUserDto = userService.findUserById(id);
            JSONObject jsonObject = new JSONObject(json);

            JSONObject updatedUser = new JSONObject();

            updatedUser.put("password", jsonObject.has("password") ? jsonObject.getString("password"): existingUserDto.password());
            updatedUser.put("email", jsonObject.has("email") ? jsonObject.getString("email"): existingUserDto.email());
            updatedUser.put("name", jsonObject.has("name") ? jsonObject.getString("name"): existingUserDto.name());
            updatedUser.put("surname", jsonObject.has("surname") ? jsonObject.getString("surname"): existingUserDto.surname());

            jsonValidator.validator(updatedUser);

            userService.updateUser(updatedUser, id);

            return ResponseEntity.ok(updatedUser.toMap());

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        try {
            try {
                UserDto user = userService.findUserById(id);
            } catch (Exception e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
            }
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    //TODO: password encrypt, file handling

}
