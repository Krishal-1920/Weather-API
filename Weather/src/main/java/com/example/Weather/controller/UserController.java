package com.example.Weather.controller;

import com.example.Weather.entity.User;
import com.example.Weather.mapper.UserMapper;
import com.example.Weather.model.UserModel;
import com.example.Weather.service.CustomUserDetailsService;
import com.example.Weather.service.UserService;
import com.example.Weather.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;


    @PostMapping("/signUp")
    public ResponseEntity<UserModel> signUp(@RequestBody UserModel userModel){
        return ResponseEntity.ok(userService.newUser(userModel));
    }


    @GetMapping("/getDetails/{userId}")
    public ResponseEntity<UserModel> getUser(@RequestHeader("Authorization") String tokenHeader){
        return ResponseEntity.ok(userService.viewProfile(String.valueOf(tokenHeader)));
    }


    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAll(@RequestHeader("Authorization") String tokenHeader){
        return ResponseEntity.ok(userService.getAllUsers(tokenHeader));
    }


    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UserModel updatedUserModel,
                                        @RequestHeader("Authorization") String tokenHeader) {
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(userService.updateUser(updatedUserModel, authenticatedEmail));
    }


    @DeleteMapping("/deleteId")
    public void deleteId(@RequestHeader("Authorization") String tokenHeader){
        userService.deleteUser(tokenHeader);
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserModel userModel) {
        try {
            // Wrap email and password into UsernamePasswordAuthenticationToken
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userModel.getEmail(), userModel.getPassword()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(userModel.getEmail());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect Email or Password");
        }
    }

}