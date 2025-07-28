package com.example.Chatalyst.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


    @RestController
    @RequestMapping("/api")
    public class ChatController {
        private final ChatClient chatClient;

        private final ChatMemory chatMemory;

        public ChatController(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
            this.chatClient = chatClientBuilder.build();
            this.chatMemory = chatMemory;
        }

        @PostMapping("/chat")
        public String chat(@RequestBody String input) {
            return chatClient
                    .prompt()
                    .user(input)
                    .call()
                    .content();
        }

        public record ChatWithMemoryRequest(String input, String conversationId) {
        }

        @PostMapping("/chat-with-memory")
        public String chatWithMemory(@RequestBody ChatWithMemoryRequest body) {
            return chatClient.prompt()
                    .advisors(MessageChatMemoryAdvisor.builder(chatMemory)
                            .conversationId(body.conversationId)
                            .build())
                    .user(body.input())
                    .call()
                    .content();

        }


    }

