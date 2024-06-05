package com.example.spring_elastic_search_user.mapper;

import com.example.spring_elastic_search_user.domain.User;
import com.example.spring_elastic_search_user.feature.user.userDto.UserRequest;
import com.example.spring_elastic_search_user.feature.user.userDto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toUser(UserRequest userRequest);

    UserResponse toUserResponse(User user);
}
