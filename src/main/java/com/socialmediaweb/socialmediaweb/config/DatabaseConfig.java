package com.socialmediaweb.socialmediaweb.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");
        
        // Local development fallback
        if (databaseUrl == null || databaseUrl.isEmpty()) {
            System.out.println("DATABASE_URL not found, using local configuration");
            return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/socialmedia_db")
                .username("postgres")
                .password("Hemanth@123")
                .driverClassName("org.postgresql.Driver")
                .build();
        }
        
        try {
            // Parse Railway's DATABASE_URL
            System.out.println("Parsing DATABASE_URL for Railway deployment");
            URI dbUri = new URI(databaseUrl);
            
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            
            System.out.println("Database URL: " + dbUrl);
            System.out.println("Database User: " + username);
            
            return DataSourceBuilder.create()
                .url(dbUrl)
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
                
        } catch (URISyntaxException e) {
            System.err.println("Error parsing DATABASE_URL: " + e.getMessage());
            throw new RuntimeException("Failed to parse DATABASE_URL", e);
        }
    }
}
