package com.example.Chatalyst.controller;

import com.example.Chatalyst.model.DateTimeTools;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.jdbc.BadSqlGrammarException;




import java.rmi.server.UID;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.bind.annotation.CrossOrigin;


@RestController
    @RequestMapping("/api")
@CrossOrigin(origins = "*")
    public class ChatController {
        private final ChatClient chatClient;

        private final ChatMemory chatMemory;

        private final ObjectMapper objectMapper;



    @Autowired
        private JdbcTemplate jdbcTemplate;

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
        public ChatWithDataResponse chatWithMemory(@RequestBody ChatWithMemoryRequest body) {


            if (body.conversationId == null)  {
                body.setConversationId(UUID.randomUUID().toString());
            }


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
- Smijete koristiti isključivo SELECT upite. Nikada nemojte koristiti INSERT, UPDATE, DELETE, DROP ili bilo koji drugi upit koji mijenja podatke u bazi. Ako korisnik zatraži upit koji nije SELECT, postupi isto kao da njegov zahtjev nije dobar.
- Svaki upit mora biti validan, jednoznačan i jasan.
- Ne pretpostavljajte nepotrebne informacije – držite se isključivo onoga što korisnik traži.
- Ako korisnički zahtjev uključuje kolonu ili tablicu koja NE postoji u danoj shemi baze podataka, NEMOJ generirati SQL upit. Umjesto toga, vrati jasnu poruku korisniku poput:
"Traženi podatak (npr. kolona 'XYZ') ne postoji u dostupnim tablicama pa ne mogu generirati SQL upit."
- Ako korisnikov upit nije potpun npr; 'daj mi sve podatke' nemoj generirati sql upit nego vrati poruku 'Upit nije potpun.'
- Ako korisnik zatraži prevelik broj podataka, promijeni LIMIT odgovora na 500

Baza podataka sadrži 3 tablice. Evo njihove sheme (uključujući shemu):

Prva tablica (customers):
TABLE osam_ljudi_sedam_laptopa.customerdbo (
    individual_id int8 NULL,
    address_id int8 NULL,
    curr_ann_amt int8 NULL,
    days_tenure int4 NULL,
    cust_orig_date date NULL,
    age_in_years int4 NULL,
    date_of_birth date NULL,
    social_security_number varchar(50) NULL,
    acct_suspd_date date NULL,
    churn bool NULL
);

primjer podataka za customersdbo:
221302190038	521300791513	1200.5493	6291	2005-09-11	54	1968-01-25	794-XX-5308	2022-07-20	true
221301388953	521300506253	1162.3987	6291	2005-09-11	63	1958-12-29	581-XX-7606	2022-11-15	true
221300599064	521300221962	1292.3265	6291	2005-09-11	59	1963-10-28	107-XX-5224	2022-05-26	true
221302148321	521300776813	649.1104	1555	2018-08-30	46	1976-08-24	261-XX-2740	2022-02-23	true
221303281021	521301510584	877.5463	6291	2005-09-11	50	1972-09-25	187-XX-3457	2022-11-27	true
221302083391	521300753252	1012.3374	80	2022-09-13	55	1967-07-07	657-XX-4648	2022-08-23	true

Druga tablica (addresses):
TABLE osam_ljudi_sedam_laptopa.addressdbo (
    address_id int8 NULL,
    latitude float4 NULL,
    longitude float4 NULL,
    street_address varchar(50) NULL,
    city varchar(50) NULL,
    state varchar(50) NULL,
    county varchar(50) NULL
);

primjer podataka za addressdbo:
521301086809	32.315804	-96.6279	8457 Wright Mountains Apt. 377	Ennis	TX	Ellis
521300039185			082 Cline Mountains Apt. 353	Irving	TX	Dallas
521300239034	32.80629	-96.779854	457 John Mills	Dallas	TX	Dallas
521301307921	32.825737	-96.93969	5726 Barnett Meadow	Irving	TX	Dallas
521300970034	32.86719	-96.71555	050 Nicholas Views	Dallas	TX	Dallas
521301487164	33.055527	-96.70529	207 Rebecca Brook	Plano	TX	Collin
521300610942	33.406006	-96.966034	9983 Jesse Landing	Pilot Point	TX	Denton
521300852902	32.892216	-97.08318	76627 Waters Estate Apt. 016	Grapevine	TX	Tarrant
521300082471	32.858974	-96.64946	378 Anderson Manors Suite 859	Dallas	TX	Dallas
521301378311	32.982513	-96.575035	12710 Vanessa Rest	Sachse	TX	Dallas

Treća tablica (demographics):
TABLE osam_ljudi_sedam_laptopa.demographicdbo (
    individual_id int8 NULL,
    income float4 NULL,
    has_children bool NULL,
    length_of_residence float4 NULL,
    martial_status varchar(50) NULL, -- vrijednosti: Married ili Single ili UNKNOWN
    home_owner bool NULL,
    college_degree bool NULL,
    good_credit bool NULL,
    home_market_value_min float4 NULL,
    home_market_value_max float4 NULL
);


primjer podataka za demographicdbo:
221302803089	125000.0	true	8.0	Single	true	true	true	300000.0	349999.0
221303165601	42500.0	false	0.0	Single	false	false	false		
221303160257	27500.0	false	15.0	Married	true	false	true	75000.0	99999.0
221303153810	80372.18	false	0.0	UNKNOWN	true	false	false	1000.0	24999.0
221303153255	125000.0	false	0.0	UNKNOWN	false	false	true		
221303149860	70000.0	true	14.0	Married	true	false	true	100000.0	124999.0
221303145066	87500.0	true	3.0	Single	true	false	true	75000.0	99999.0
221303141057	62500.0	true	5.0	Married	true	false	true	50000.0	74999.0
221303155883	125000.0	false	3.0	Married	true	true	true	75000.0	99999.0
221303150302	42500.0	true	5.0	UNKNOWN	false	true	true	75000.0	99999.0

graph (vizualizacija rezultata):
Za svaki SQL upit koji generirate, predložite jedan ili više prikladnih načina kako vizualno prikazati dobivene podatke. Primjeri:
- BAR – za agregirane vrijednosti po kategorijama (npr. broj korisnika po gradu)
- LINE – za vremenske trendove (npr. prihodi kroz godine)
- PIE – za prikaz udjela (npr. udio korisnika s djecom i bez)
- TABLE – za pregled manjih količina podataka sa strukturiranim kolonama

Na kraju svakog upita, dodajte prijedlog vizualizacije na hrvatskom jeziku, npr.:
Predložena vizualizacija: stupčasti grafikon s brojem korisnika po gradu.

Ako korisnički upit nije stvarno pitanje koje zahtijeva SQL SELECT upit (npr. upit je pozdrav tipa "hej", "kako si", "kako se zoveš", "reci nešto pametno" i slično), odgovor mora biti kratak i pristojan, ali jasan, u stilu:

"Molim vas postavite pitanje koje mogu prevesti u SQL SELECT upit na temelju danih tablica."

Ne smijete pokušavati generirati SQL upit ako nema konkretnog zahtjeva za podatcima.

""")



                    .advisors(new SimpleLoggerAdvisor())
                    .user(body.getInput())
                    .tools(new DateTimeTools())
                    .call()
                    .entity(LLMREsponse.class);
            chatMemory.add(body.conversationId, new AssistantMessage(objectMapper.writeValueAsString(respone)));

            String sql = respone.getSqlQuery();

            List<Map<String, Object>> result = List.of(); // prazna lista

            try {
                if (sql != null && !sql.isBlank()) {
                    if (!sql.toUpperCase().contains("LIMIT")) {
                        sql = sql.trim().replaceAll(";", "") + " LIMIT 500;";
                    } else {
                        sql = sql.trim().replaceAll(";", "");
                        int limitIndex = sql.toUpperCase().indexOf("LIMIT") + 5;
                        String afterLimit = sql.substring(limitIndex).trim();
                        int limitPart;

                        try {
                            limitPart = Integer.parseInt(afterLimit.split("\\s+")[0]);
                        } catch (NumberFormatException e) {
                            // fallback ako AI generira npr. "LIMIT xyz"
                            limitPart = 9999;
                        }

                        if (limitPart > 500) {
                            sql = sql.substring(0, sql.toUpperCase().indexOf("LIMIT")) + "LIMIT 500;";
                        } else {
                            sql += ";";
                        }
                    }


                    result = jdbcTemplate.queryForList(sql);
                }
            } catch (BadSqlGrammarException e) {
                System.out.println("UPITT " + sql);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Upit nije moguće izvršiti jer sadrži nepostojeću tablicu ili kolonu.", e);

            }

            System.out.println("UPITT " + sql);


            return new ChatWithDataResponse(respone, result, body.conversationId);


        }

        @Getter
        @Setter
        @AllArgsConstructor
        public static class ChatWithDataResponse{
            private LLMREsponse llmrEsponse;
            private List<Map<String, Object>> data;
            private String conversationId;
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

