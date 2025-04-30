package com.example.Weather.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserModel {

    private String fullName;
    private LocalDate dateOfBirth;
    private String email;
    private String password;
    private String gender;


}
