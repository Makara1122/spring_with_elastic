package com.example.spring_elastic_search_user.feature.user.web;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.spring_elastic_search_user.domain.User;
import com.example.spring_elastic_search_user.feature.elastic.ELServiceImpl;
import com.example.spring_elastic_search_user.feature.user.UserService;
import com.example.spring_elastic_search_user.feature.user.userDto.UserRequest;
import com.example.spring_elastic_search_user.feature.user.userDto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserRestController {
    private final UserService userService;
    private final ELServiceImpl elService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody UserRequest userRequest) {
        return userService.register(userRequest);
    }

    @GetMapping("/all")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{name}")
    public UserResponse getUserByName(@PathVariable String name) {
        return userService.getUserByName(name);
    }

    @GetMapping("/autoSuggest/{partialUserName}")
    public List<String> autoSuggest(@PathVariable String partialUserName) throws IOException {
        SearchResponse<User> searchResponse = elService.autoSuggest(partialUserName);
        List<Hit<User>> hitList = searchResponse.hits().hits();
        List<User> userList = new ArrayList<>();
        for (Hit<User> hit : hitList) {
            userList.add(hit.source());
        }
        List<String> listUserName = new ArrayList<>();
        for (User user : userList) {
            listUserName.add(user.getName());
        }
        return listUserName;
    }

    @GetMapping("/autoSuggestEmail/{partialEmail}")
    public List<String> autoSuggestEmail(@PathVariable String partialEmail) throws IOException {
        SearchResponse<User> searchResponse = elService.autoSuggestEmail(partialEmail);
        List<Hit<User>> hitList = searchResponse.hits().hits();
        List<User> userList = new ArrayList<>();
        for (Hit<User> hit : hitList) {
            userList.add(hit.source());
        }
        List<String> listUserName = new ArrayList<>();
        for (User user : userList) {
            listUserName.add(user.getEmail());
        }
        return listUserName;
    }

    @DeleteMapping("/{name}")
    public void deleteUserByName(@PathVariable String name) {
        userService.deleteUserByName(name);
    }


}
