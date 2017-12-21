package com.bigos.hello;

import com.bigos.base.MvcIntegrationTests;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;

class HelloWorldIntegrationTest extends MvcIntegrationTests {

    @Test
    public void shouldLoadSpringContext() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/"))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        assertThat(status).isEqualTo(HttpStatus.OK.value());
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
