package com.bigos.base;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class MvcRequestBuilder {
    private final MockHttpServletRequestBuilder requestBuilder;
    private final MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Object request = null;
    private Map<String, List<String>> params = emptyMap();

    public MvcRequestBuilder(MockHttpServletRequestBuilder requestBuilder, MockMvc mockMvc) {
        this.requestBuilder = requestBuilder;
        this.mockMvc = mockMvc;
    }

    public MvcRequestBuilder withContent(Object request) {
        this.request = request;
        return this;
    }

    public MvcRequestBuilder withParam(String key, String value) {
        params.put(key, singletonList(value));
        return this;
    }

    public MvcRequestBuilder withParams(Map<String, String> params) {
        this.params = mapParams(params);
        return this;
    }

    public MvcRequestBuilder withParamsList(Map<String, List<String>> params) {
        this.params = params;
        return this;
    }

    public <T> MvcJsonResult<T> forJsonOf(Class responseClass, Class... parameterClasses) {
        MvcResult mvcResult;
        T response;
        WarnDto warn;
        try {
            mvcResult = buildAndPerformRequest();
            response = getResponse(mvcResult, responseClass, parameterClasses);
            warn = getWarn(mvcResult);
        } catch (Throwable t) {
            throw new RuntimeException(t.getMessage());
        }
        return new MvcJsonResult<>(mvcResult, response, warn);
    }

    public MvcJsonResult forResult() {
        MvcResult mvcResult;
        WarnDto warn;
        try {
            mvcResult = buildAndPerformRequest();
            warn = getWarn(mvcResult);
        } catch (Throwable t) {
            throw new RuntimeException(t.getMessage());
        }
        return new MvcJsonResult<>(mvcResult, null, warn);
    }

    private MvcResult buildAndPerformRequest() throws Exception {
        requestBuilder
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .params(fromMap(params));

        return mockMvc.perform(requestBuilder)
                .andDo(print())
                .andReturn();
    }

    private <T> T getResponse(MvcResult mvcResult, Class responseClass, Class... parameterClasses) throws Exception {
        T response = null;
        if (responseClass != null &&
                !mvcResult.getResponse().getContentAsString().isEmpty() &&
                mvcResult.getResponse().getStatus() >= HttpStatus.OK.value() &&
                mvcResult.getResponse().getStatus() < HttpStatus.MULTIPLE_CHOICES.value()) {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(responseClass, parameterClasses);
            response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), javaType);
        }
        return response;
    }

    private WarnDto getWarn(MvcResult mvcResult) throws Exception {
        WarnDto warn = null;
        if (!mvcResult.getResponse().getContentAsString().isEmpty() &&
                mvcResult.getResponse().getStatus() >= HttpStatus.BAD_REQUEST.value()) {
            warn = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), WarnDto.class);
        }
        return warn;
    }

    private Map<String, List<String>> mapParams(Map<String, String> params) {
        return params != null ?
                params.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, v -> singletonList(v.getValue())))
                : emptyMap();
    }

    private MultiValueMap<String, String> fromMap(Map<String, List<String>> map) {
        return new LinkedMultiValueMap<>(map);
    }
}
