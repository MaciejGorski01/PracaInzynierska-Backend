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
        User user = userRepository.findById(id);
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

    public void addUser(User user){
        userRepository.create(user.getPassword(), user.getEmail(), user.getName(), user.getSurname());
    }

    public void deleteUser(Long id){
        userRepository.delete(id);
    }

    public void updateUser(User user){
        userRepository.update(user.getPassword(), user.getEmail(), user.getName(), user.getSurname(), user.getId());
    }

    public UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto(user.getId(), user.getPassword(), user.getEmail(), user.getName(), user.getSurname());
        return userDto;
    }
}
