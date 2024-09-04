package org.example.pracainzynierska.services;

import org.example.pracainzynierska.dtos.UserDto;
import org.example.pracainzynierska.models.User;
import org.example.pracainzynierska.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public UserDto findUserById(Long id){
        User user = userRepository.findById(id).get();
        return mapToUserDto(user);
    }

    public UserDto findUserByEmail(String email){
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            return mapToUserDto(user);
        } else {
            throw new RuntimeException("User not found!");
        }
    }

    public User addUser(User user){
        return userRepository.create(user.getPassword(), user.getEmail(), user.getName(), user.getSurname());
    }

    //todo:
    //reverse mapping
    //update
    //delete

    public UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto(user.getId(), user.getPassword(), user.getEmail(), user.getName(), user.getSurname());
        return userDto;
    }
}
