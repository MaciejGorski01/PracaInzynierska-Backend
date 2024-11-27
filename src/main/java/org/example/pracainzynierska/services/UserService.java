package org.example.pracainzynierska.services;

import org.example.pracainzynierska.dtos.UserDto;
import org.example.pracainzynierska.models.User;
import org.example.pracainzynierska.repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserDto> findAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToUserDto).toList();
    }

    public UserDto findUserById(String id){
        User user = userRepository.findById(id).getFirst();
        return mapToUserDto(user);
    }

    public UserDto findUserByEmail(String email){
        User user = userRepository.findUserByEmail(email).getFirst();
        return mapToUserDto(user);
    }

    public void addUser(JSONObject jsonObject){
        userRepository.create(UUID.randomUUID().toString(), jsonObject);
    }

    public void deleteUser(String id){

        userRepository.delete(id);
    }

    public void updateUser(JSONObject jsonObject, String id){
        userRepository.update(jsonObject, id);
    }

    public UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto(user.getId(), user.getPassword(), user.getEmail(), user.getName(), user.getSurname());
        return userDto;
    }
}
