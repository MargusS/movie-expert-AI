package dev.margus.movie_expert_ai.client.omdb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OmdbClient {

    private final RestClient restClient;
    private final String apiKey;

    public OmdbClient(@Value("${omdb.api.key}") String apiKey) {
        this.restClient = RestClient.builder()
                .baseUrl("http://www.omdbapi.com")
                .build();
        this.apiKey = apiKey;
    }

    public OmdbInfoResponse getMovieInfo(String title) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", apiKey)
                        .queryParam("t", title)
                        .build())
                .retrieve()
                .body(OmdbInfoResponse.class);
    }
}
