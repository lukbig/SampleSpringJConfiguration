package com.bigos.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Component
@Profile({"local", "default"})
public class DatabaseCleaner {
    private static final List<String> TABLES_FOR_CLEANUP = Arrays.asList(
            "users"
    );

    private static final List<String> URL_WHITELIST = Arrays.asList(
        "jdbc:postgresql://localhost:5432/test-db-name"
    );

    @Autowired
    DataSource dataSource;

    private JdbcTemplate jdbcTemplate;
    private String username;
    private String url;

    @PostConstruct
    void createSeparateJdbcTemplate() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        try {
            url = dataSource.getConnection().getMetaData().getURL();
            username = dataSource.getConnection().getMetaData().getUserName();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        if (!URL_WHITELIST.contains(url)) {
            System.err.println("Your database URL " + url + " is not on whitelist!");
            System.exit(1);
        }
    }

    public void cleanup() {
        System.err.println("Cleaning up database " + username + " at " + url);
        try {
            TABLES_FOR_CLEANUP.forEach( (String table) -> {
                String sqlStatement = "DELETE FROM " + table;
                int rowCount = jdbcTemplate.update(sqlStatement);
                if (rowCount > 0) {
                    System.err.println("Deleted " + rowCount + " rows from " + table);
                }
            });
        } catch (Throwable t) {
            System.err.println("There was an error during SQL execution "  + t.getMessage());
            throw new RuntimeException(t);
        }

        System.err.println("Cleaned up " + TABLES_FOR_CLEANUP.size() + " tables");
    }
}
