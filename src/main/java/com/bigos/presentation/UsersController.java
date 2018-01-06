package com.bigos.presentation;

import com.bigos.presentation.dto.UserDto;
import com.bigos.service.AddUserService;
import com.bigos.service.UsersFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UsersController {

    @Autowired
    AddUserService addUserService;
    @Autowired
    UsersFinder usersFinder;

    @RequestMapping("/add")
    void addUser(@RequestBody UserDto userDto) {
        addUserService.addUser(userDto);
    }

    @GetMapping
    List<UserDto> list() {
        return usersFinder.list();
    }
}
