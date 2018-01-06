package com.bigos.service;

import com.bigos.domain.users.User;
import com.bigos.domain.users.UserRepository;
import com.bigos.presentation.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersFinder {

    @Autowired
    UserRepository userRepository;

    public List<UserDto> list() {
        List<User> userList = (List<User>) userRepository.findAll();
        List<UserDto> userDtos = userList.stream()
                .map((User user) -> {
                    return new UserDto(user.getUsername(), user.getPassword(), user.getPrivileges());
                })
                .collect(Collectors.toList());
        return userDtos;
    }
}
