package dev.margus.movie_expert_ai.client.omdb;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonClassDescription("Request to get technical movie details by title")
public record OmdbInfoRequest(
        @JsonProperty(required = true, value = "movie_title") String movieTitle
) {
}
