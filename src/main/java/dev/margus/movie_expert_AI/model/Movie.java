package dev.margus.movie_expert_ai.model;

import java.util.List;

public record Movie(
        String title,
        String director,
        String genre,
        Integer releaseYear,
        Double ImdbGrade,
        String synopsis,
        List<String> keyActors
) {}
