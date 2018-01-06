package com.bigos.base;

import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

public class MvcJsonResult<T> {
    private final MvcResult mvcResult;
    private final T json;
    private final WarnDto warn;

    public MvcJsonResult(MvcResult mvcResult, T json, WarnDto warn) {
        this.mvcResult = mvcResult;
        this.json = json;
        this.warn = warn;
    }

    public HttpStatus getStatus() {
        return HttpStatus.valueOf(mvcResult.getResponse().getStatus());
    }

    public MvcResult getMvcResult() {
        return mvcResult;
    }

    public T getJson() {
        return json;
    }

    public WarnDto getWarn() {
        return warn;
    }
}
