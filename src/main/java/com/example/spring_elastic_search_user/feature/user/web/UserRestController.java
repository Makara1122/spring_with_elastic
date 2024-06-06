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

    @PatchMapping("/update/{name}")
    public UserResponse updateByName(@PathVariable String name,@RequestBody UserRequest userRequet) {
        return userService.updateUserByName(name, userRequet);
    }

    @GetMapping("/allElasic")
    public List<User> getAllUserElastic() throws IOException {

        SearchResponse<User> searchResponse = elService._matchAllService();

        List<Hit<User>> listHit = searchResponse.hits().hits();

        List<User> listUsers = new ArrayList<>();

        for (Hit<User> users : listHit) {

            listUsers.add(users.source());

        }


        return listUsers;
    }

    @GetMapping("/{wildcard}/{value}")
    public List<User> getUsersByWildcard (@PathVariable String wildcard,@PathVariable String value) throws IOException {

        String wildcard2 =  value + "*";

        SearchResponse<User> searchResponse = elService._wildcardService(wildcard,wildcard2);

        List<Hit<User>> listHit = searchResponse.hits().hits();

        List<User> users = new ArrayList<>();

        for (Hit<User> user1 : listHit) {

            users.add(user1.source());

        }

        return users;
    }

    @GetMapping("/query/{query}")
    public List<UserResponse> getUsersByQuery(@PathVariable String query) throws IOException {
        SearchResponse<User> searchResponse = elService._queryAll(query);
        List<Hit<User>> hitList = searchResponse.hits().hits();
        List<UserResponse> userResponses = new ArrayList<>();
        for (Hit<User> hit : hitList) {
            userResponses.add(
                    UserResponse.builder()
                            .name(hit.source().getName())
                            .email(hit.source().getEmail())
                            .id(hit.id())
                            .address(hit.source().getAddress())
                            .build()
            );
        }
        return userResponses;
    }

}
