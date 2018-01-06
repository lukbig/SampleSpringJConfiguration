package com.bigos.service;

import com.bigos.domain.users.User;
import com.bigos.domain.users.UserRepository;
import com.bigos.presentation.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddUserService {

    @Autowired
    UserRepository userRepository;

    public void addUser(UserDto userDto) {
        User user = createUser(userDto);
        userRepository.save(user);
    }

    private User createUser(UserDto userDto) {
        return new User(
                userDto.getUsername(),
                userDto.getUsername(),
                userDto.getPrivileges()
        );
    }
}
