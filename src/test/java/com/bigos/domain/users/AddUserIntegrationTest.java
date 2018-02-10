package com.bigos.domain.users;

import com.bigos.base.MvcIntegrationTests;
import com.bigos.base.MvcJsonResult;
import com.bigos.presentation.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class AddUserIntegrationTest extends MvcIntegrationTests {
    private static final String ADD_USER_URI = "/user/add";

    private static final String USERNAME = "John";
    private static final String PASSWORD = "Kowalsky";
    private static final Privileges PRIVILIGES = Privileges.USER;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldAddUser() {
        UserDto userDto = new UserDto(USERNAME, PASSWORD, PRIVILIGES);

        MvcJsonResult jsonResult = post(ADD_USER_URI).withContent(userDto).forResult();

        assertThat(jsonResult.getStatus()).isEqualTo(HttpStatus.OK);
        assertThat(userRepository.existsById(USERNAME));
    }
}
