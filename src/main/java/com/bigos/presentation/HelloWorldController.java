package com.bigos.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class HelloWorldController {

    @GetMapping
    public String hello() {
        return "hello friend";
    }

    @GetMapping("/example")
    public Map<String, String> exampleRest() {
        Map<String, String> map = new HashMap<>();
        map.put("a", "b");
        return map;
    }
}
