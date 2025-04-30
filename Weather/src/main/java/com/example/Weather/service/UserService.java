package com.example.Weather.service;

import com.example.Weather.entity.User;
import com.example.Weather.mapper.UserMapper;
import com.example.Weather.model.UserModel;
import com.example.Weather.repository.UserRepository;
import com.example.Weather.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    public UserModel newUser(UserModel userModel){
        User addUser = userMapper.userModelToUser(userModel);
        addUser.setPassword(passwordEncoder.encode(userModel.getPassword()));
        addUser = userRepository.save(addUser);
        return userMapper.userToUserModel(addUser);
    }


    public UserModel viewProfile(String tokenHeader) {
        String authenticatedEmail = jwtUtils.extractUsername(tokenHeader);
        User user = userRepository.findByEmail(authenticatedEmail);

        return userMapper.userToUserModel(user);
    }


    public List<User> getAllUsers(String tokenHeader) {
        String authenticatedEmail = jwtUtils.extractUsername(tokenHeader);


        return userRepository.findAll();
    }


    public UserModel updateUser(UserModel updateUserModel, String authenticatedEmail) {
        User existingUser = userRepository.findByEmail(authenticatedEmail);
        if (existingUser == null) {
            throw new UsernameNotFoundException("User not found with email: " + authenticatedEmail);
        }

        User updatedUser = userMapper.updateUserModel(updateUserModel, existingUser);
        if (updateUserModel.getPassword() != null && !updateUserModel.getPassword().isEmpty()) {
            updatedUser.setPassword(passwordEncoder.encode(updateUserModel.getPassword()));
        }

        User savedUser = userRepository.save(updatedUser);
        return userMapper.userToUserModel(savedUser);
    }


    public void deleteUser(String tokenHeader) {
        String email = jwtUtils.extractUsername(tokenHeader);
        User user = userRepository.findByEmail(email);

        userRepository.delete(user);
    }

}
