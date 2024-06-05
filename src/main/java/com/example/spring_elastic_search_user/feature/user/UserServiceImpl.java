package com.example.spring_elastic_search_user.feature.user;

import com.example.spring_elastic_search_user.mapper.UserMapper;
import com.example.spring_elastic_search_user.feature.user.userDto.UserRequest;
import com.example.spring_elastic_search_user.feature.user.userDto.UserResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final UserMapper userMapper;


    @Override
    public UserResponse register(UserRequest userRequest) {

        var user = userMapper.toUser(userRequest);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();

    }

    @Override
    public UserResponse getUserByName(String name) {

        var user = userRepository.findUserByName(name);

        return userMapper.

        toUserResponse(user.orElseThrow(() -> new RuntimeException("User not found")));

    }

    @Override
    public void deleteUserByName(String name) {

            var user = userRepository.findUserByName(name);

            userRepository.delete(user.orElseThrow(() -> new RuntimeException("User not found")));
    }

    @Override
    public UserResponse updateUserByName(String name, UserRequest userRequest) {

        var user = userRepository.findUserByName(name).orElseThrow(() -> new RuntimeException("User not found"));

        var user1 = userMapper.toUser(userRequest);

        user1.setId(user.getId());

        System.out.println("=========== User + " + user.toString() + " + ===========");

        return userMapper.toUserResponse(userRepository.save(user1));
    }


}
