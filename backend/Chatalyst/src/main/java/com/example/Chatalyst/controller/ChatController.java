package com.example.Chatalyst.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import java.rmi.server.UID;
import java.util.UUID;


@RestController
    @RequestMapping("/api")
    public class ChatController {
        private final ChatClient chatClient;

        private final ChatMemory chatMemory;

        private final ObjectMapper objectMapper;



        public ChatController(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, ObjectMapper objectMapper) {
            this.chatClient = chatClientBuilder.build();
            this.chatMemory = chatMemory;
            this.objectMapper = objectMapper;
        }


        @PostMapping("/chat")
        public String chat(@RequestBody String input) {
            return chatClient
                    .prompt()
                    .user(input)
                    .call()
                    .content();
        }
        @Getter
        @Setter
        public static class ChatWithMemoryRequest {
            String input;
            String conversationId;
        }



        @SneakyThrows
        @PostMapping("/chat-with-memory")
        public LLMREsponse chatWithMemory(@RequestBody ChatWithMemoryRequest body) {
            if (body.conversationId == null) body.setConversationId(UUID.randomUUID().toString());
            chatMemory.add(body.conversationId, new UserMessage(body.input));
            LLMREsponse respone = chatClient.prompt()
                    .advisors(
                    MessageChatMemoryAdvisor.builder(chatMemory)
                            .conversationId(body.getConversationId())
                            .build()
            )

                    .system("""
Vi ste stručnjak za analizu podataka pomoću SQL-a. Vaša glavna funkcija je prevesti pitanja korisnika iz prirodnog jezika u precizne i ispravne SQL SELECT upite. Odgovore piši na hrvatskom.

Cilj vam je uvijek generirati jedan točan i izvršiv SQL SELECT upit na temelju korisničkog pitanja i dane sheme baze podataka.

Važno:
- Smijete koristiti isključivo SELECT upite. Nikada nemojte koristiti INSERT, UPDATE, DELETE, DROP ili bilo koji drugi upit koji mijenja podatke u bazi.
- Svaki upit mora biti validan, jednoznačan i jasan.
- Ne pretpostavljajte nepotrebne informacije – držite se isključivo onoga što korisnik traži.

Baza podataka sadrži 3 tablice. Evo njihove sheme:

Prva tablica (customers):
CREATE TABLE osam_ljudi_sedam_laptopa.customerdbo (
    individual_id float4 NULL,
    address_id float4 NULL,
    curr_ann_amt float4 NULL,
    days_tenure int4 NULL,
    cust_orig_date varchar(50) NULL,
    age_in_years int4 NULL,
    date_of_birth varchar(50) NULL,
    social_security_number varchar(50) NULL,
    acct_suspd_date varchar(50) NULL,
    churn bool NULL
);

Druga tablica (addresses):
CREATE TABLE osam_ljudi_sedam_laptopa.address_dbo (
    address_id float4 NULL,
    latitude float4 NULL,
    longitude float4 NULL,
    street_address varchar(50) NULL,
    city varchar(50) NULL,
    state varchar(50) NULL,
    county varchar(50) NULL
);

Treća tablica (demographics):
CREATE TABLE osam_ljudi_sedam_laptopa.demographicdbo (
    individual_id float4 NULL,
    income float4 NULL,
    has_children bool NULL,
    length_of_residence float4 NULL,
    marital_status varchar(50) NULL,
    home_owner bool NULL,
    college_degree bool NULL,
    good_credit bool NULL,
    home_market_value_min int4 NULL,
    home_market_value_max int4 NULL
);

graph (vizualizacija rezultata):
Za svaki SQL upit koji generirate, predložite jedan ili više prikladnih načina kako vizualno prikazati dobivene podatke. Primjeri:
- BAR – za agregirane vrijednosti po kategorijama (npr. broj korisnika po gradu)
- LINE – za vremenske trendove (npr. prihodi kroz godine)
- PIE – za prikaz udjela (npr. udio korisnika s djecom i bez)
- TABLE – za pregled manjih količina podataka sa strukturiranim kolonama

Na kraju svakog upita, dodajte prijedlog vizualizacije na hrvatskom jeziku, npr.:
Predložena vizualizacija: stupčasti grafikon s brojem korisnika po gradu.
""")

                    .advisors(new SimpleLoggerAdvisor())
                    .user(body.getInput())
                    .call()
                    .entity(LLMREsponse.class);
            chatMemory.add(body.conversationId, new AssistantMessage(objectMapper.writeValueAsString(respone)));
            return respone;

        }


        @Getter
        @Setter
        public static class LLMREsponse {
            private String sqlQuery;

            @Nullable
            private String clarification;
            @JsonProperty("chartType")
            private GraphType graph;

        }

        public enum GraphType {
            LINE, BAR, TABLE, PIE
        }
    }

