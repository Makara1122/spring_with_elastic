package com.example.spring_elastic_search_user.feature.user.userDto;

import lombok.Builder;

@Builder
public record UserResponse(
        String id,
        String name,
        String email,
        String address
) {
}
