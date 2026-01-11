package dev.margus.movie_expert_ai.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Configuration
public class RagConfig {
    private static final Logger log = LoggerFactory.getLogger(RagConfig.class);

    @Value("classpath:data/indie-films-2025.json")
    private Resource indieFilmsResource;

    // We use an ApplicationRunner to load data AFTER the app starts
    // and ONLY if the DB is empty (to avoid duplicates).
    @Bean
    public ApplicationRunner loadData(VectorStore vectorStore, JdbcTemplate jdbcTemplate) {
        return args -> {
            // Check if we already have data to avoid re-loading on every restart
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM vector_store", Integer.class);

            if (count != null && count > 0) {
                log.info("Vector Store already contains {} documents. Skipping load.", count);
                return;
            }

            log.info("Vector Store is empty. Loading Indie Films 2025 data...");

            JsonReader jsonReader = new JsonReader(
                    indieFilmsResource,
                    "title", "genre", "synopsis", "year", "director");

            List<Document> documents = jsonReader.get();
            System.out.println("Document list of raw json");
            System.out.println(documents);
            System.out.println("-----------------------------");
            TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = tokenTextSplitter.apply(documents);
            System.out.println("Document splitted by tokens");
            System.out.println(splitDocuments);

            vectorStore.add(splitDocuments);

            log.info("Successfully loaded {} documents into PGVector.", splitDocuments.size());
        };
    }
}
