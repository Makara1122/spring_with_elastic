package com.example.spring_elastic_search_user.feature.user.userDto;

import lombok.Builder;

@Builder
public record UserRequest(
        String name,
        String email,
        String address
) {
}
