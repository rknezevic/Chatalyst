package com.example.Chatalyst.config;

import com.example.Chatalyst.repository.dialect.MyDialect;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class MessageRepositoryConfig {
    @Bean
    public ChatMemoryRepository chatMemoryRepository(JdbcTemplate template) {
        return JdbcChatMemoryRepository.builder().dialect(new MyDialect()).jdbcTemplate(template).build();
    }

}
