package com.example.Chatalyst.repository.dialect;

import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepositoryDialect;

public class MyDialect implements JdbcChatMemoryRepositoryDialect{

        @Override
        public String getSelectMessagesSql() {
            return "SELECT content, type FROM OSAM_LJUDI_SEDAM_LAPTOPA.AI_CHAT_MEMORY WHERE conversation_id = ? ORDER BY \"timestamp\"";
        }

        @Override
        public String getInsertMessageSql() {
            return "INSERT INTO OSAM_LJUDI_SEDAM_LAPTOPA.AI_CHAT_MEMORY (conversation_id, content, type, \"timestamp\") VALUES (?, ?, ?, ?)";
        }

    @Override
    public String getSelectConversationIdsSql() {
        return "SELECT DISTINCT conversation_id FROM OSAM_LJUDI_SEDAM_LAPTOPA.AI_CHAT_MEMORY";
    }

    @Override
    public String getDeleteMessagesSql() {
        return "DELETE FROM OSAM_LJUDI_SEDAM_LAPTOPA.AI_CHAT_MEMORY WHERE conversation_id = ?";
    }
}
