package com.example.spring_elastic_search_user.feature.user;

import com.example.spring_elastic_search_user.feature.user.userDto.UserRequest;
import com.example.spring_elastic_search_user.feature.user.userDto.UserResponse;

import java.util.List;

public interface UserService {

    // create user
    UserResponse register(UserRequest userRequest);

    // get all Users
    List<UserResponse> getAllUsers();

    // get user by name
    UserResponse getUserByName(String name);

    void deleteUserByName(String name);

}
