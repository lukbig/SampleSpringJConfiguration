package com.bigos.hello;

import com.bigos.conf.MvcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = MvcConfig.class)
@WebAppConfiguration("classpath:com/bigos/conf")
@ActiveProfiles("test")
class HelloWorldIntegrationTest {

    @Autowired WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldLoadSpringContext() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/"))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void shouldLoadRestObject() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/example"))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        assertThat(status).isEqualTo(HttpStatus.OK.value());
        assertThat(content).isEqualTo("{\"a\":\"b\"}");
    }
}
