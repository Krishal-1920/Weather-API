package com.example.Weather.mapper;

import com.example.Weather.entity.User;
import com.example.Weather.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "gender", target = "gender")
    User userModelToUser(UserModel userModel);

    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "gender", target = "gender")
    UserModel userToUserModel(User savedUser);


    User updateUserModel(UserModel userModel,@MappingTarget User user);
}
