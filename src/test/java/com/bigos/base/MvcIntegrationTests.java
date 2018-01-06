package com.bigos.base;

import com.bigos.conf.MvcConfig;
import com.bigos.conf.TestConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringJUnitConfig(classes = {MvcConfig.class, TestConfig.class})
@WebAppConfiguration("classpath:com/bigos/conf")
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MvcIntegrationTests {
    private static final Map<String, List<String>> EMPTY_PARAMS = Collections.emptyMap();
    private static final String PROPERTY_CLEANUP_AFTER_TEST = "cleanupAfterTest";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    DatabaseCleaner databaseCleaner;

    public MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        databaseCleaner.cleanup();
    }

    @AfterAll
    void afterAll() {
        if (System.getProperty(PROPERTY_CLEANUP_AFTER_TEST) != null
                && System.getProperty(PROPERTY_CLEANUP_AFTER_TEST).equals("false")) {
            System.err.println("Not cleaning after tests");
        } else {
            databaseCleaner.cleanup();
        }
    }

    public MvcJsonResult postForJson(String uri, Object request, Class responseClass, Class... parameterClasses) {
        return requestForJson(post(uri), request, EMPTY_PARAMS, responseClass, parameterClasses);
    }

    public MvcJsonResult postForJson(String uri, Object request, Map<String, String> params, Class responseClass, Class... parameterClasses) {
        return requestForJson(post(uri), request, mapParams(params), responseClass, parameterClasses);
    }

    public MvcJsonResult putForJson(String uri, Object request, Class responseClass, Class... parameterClasses) {
        return requestForJson(put(uri), request, EMPTY_PARAMS, responseClass, parameterClasses);
    }

    public MvcJsonResult putForJson(String uri, Object request, Map<String, String> params, Class responseClass, Class... parameterClasses) {
        return requestForJson(put(uri), request, mapParams(params), responseClass, parameterClasses);
    }

    public MvcJsonResult getForJson(String uri, Object request, Map<String, String> params, Class responseClass, Class... parameterClasses) {
        return requestForJson(get(uri), request, mapParams(params), responseClass, parameterClasses);
    }

    public MvcJsonResult getForJson(String uri, Map<String, List<String>> params, Class responseClass, Class... parameterClasses) {
        return requestForJson(get(uri), null, params, responseClass, parameterClasses);
    }

    private MvcJsonResult requestForJson(MockHttpServletRequestBuilder requestBuilder, Object request,
                                 Map<String, List<String>> params, Class responseClass, Class... parameterClasses) {
        MvcResult mvcResult;
        Object response = null;
        WarnDto warn = null;

        try {
            requestBuilder
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .params(fromMap(params));

            mvcResult = mockMvc.perform(requestBuilder)
                    .andDo(print())
                    .andReturn();

            if (responseClass != null &&
                    !mvcResult.getResponse().getContentAsString().isEmpty() &&
                    mvcResult.getResponse().getStatus() >= HttpStatus.OK.value() &&
                    mvcResult.getResponse().getStatus() < HttpStatus.MULTIPLE_CHOICES.value()) {
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(responseClass, parameterClasses);
                response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), javaType);
            }

            if (!mvcResult.getResponse().getContentAsString().isEmpty() &&
                    mvcResult.getResponse().getStatus() >= HttpStatus.BAD_REQUEST.value()) {
                warn = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), WarnDto.class);
            }
        } catch (Throwable t) {
            throw new RuntimeException(t.getMessage());
        }

        return new MvcJsonResult(mvcResult, response, warn);
    }

    private Map<String, List<String>> mapParams(Map<String, String> params) {
        return params != null ?
                params.entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, v -> Arrays.asList(v.getValue())))
                : EMPTY_PARAMS;
    }

    private MultiValueMap<String, String> fromMap(Map<String, List<String>> map) {
        return new LinkedMultiValueMap<>(map);
    }
}
