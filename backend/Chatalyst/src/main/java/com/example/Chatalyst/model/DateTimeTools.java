package com.example.Chatalyst.model;

import org.springframework.ai.tool.annotation.Tool;

import java.time.LocalDateTime;

public class DateTimeTools {
    @Tool(description = "This returns current UTC time.")
    public LocalDateTime getCurrentTimeStamp(){
        return LocalDateTime.now();
    }
}

