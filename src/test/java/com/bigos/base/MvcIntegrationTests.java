package com.bigos.base;

import com.bigos.conf.MvcConfig;
import com.bigos.conf.TestConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringJUnitConfig(classes = {MvcConfig.class, TestConfig.class})
@WebAppConfiguration("classpath:com/bigos/conf")
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MvcIntegrationTests {
    private static final String PROPERTY_CLEANUP_AFTER_TEST = "cleanupAfterTest";

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private DatabaseCleaner databaseCleaner;

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

    public MvcRequestBuilder get(String uri) {
        return new MvcRequestBuilder(MockMvcRequestBuilders.get(uri), mockMvc);
    }

    public MvcRequestBuilder post(String uri) {
        return new MvcRequestBuilder(MockMvcRequestBuilders.post(uri), mockMvc);
    }

    public MvcRequestBuilder put(String uri) {
        return new MvcRequestBuilder(MockMvcRequestBuilders.put(uri), mockMvc);
    }
}
