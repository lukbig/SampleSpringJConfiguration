package com.bigos.domain.users;

import com.bigos.base.MvcIntegrationTests;
import com.bigos.base.MvcJsonResult;
import com.bigos.presentation.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ListUserIntegrationTest extends MvcIntegrationTests {
    private static final String LIST_USER_URI = "/user";

    private static final String USERNAME = "John";
    private static final String PASSWORD = "Kowalsky";
    private static final Privileges PRIVILIGES = Privileges.USER;

    @Autowired
    UserRepository userRepository;

    @Test
    void shouldListUsers() {
        User user = new User(USERNAME, PASSWORD, PRIVILIGES);
        userRepository.save(user);

        MvcJsonResult<List<UserDto>> jsonResult = getForJson(LIST_USER_URI, new HashMap<>(), List.class, UserDto.class);
        List<UserDto> userDtos = jsonResult.getJson();
        UserDto userDto = userDtos.get(0);

        assertThat(jsonResult.getStatus());
        assertThat(userDtos).hasSize(1);
        assertThat(userDto.getUsername()).isEqualTo(USERNAME);
        assertThat(userDto.getPassword()).isEqualTo(PASSWORD);
        assertThat(userDto.getPrivileges()).isEqualTo(PRIVILIGES);
    }
}
