package dev.margus.movie_expert_ai.tool;

import dev.margus.movie_expert_ai.client.omdb.OmdbClient;
import dev.margus.movie_expert_ai.client.omdb.OmdbInfoResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class MovieTools {
    private final OmdbClient omdbClient;

    public MovieTools(OmdbClient omdbClient) {
        this.omdbClient = omdbClient;
    }

    @Tool(name = "movieInfoTool", description = "Get detailed technical information about a movie including cast, director, year, and IMDb rating.")
    public OmdbInfoResponse getMovieInfo(@ToolParam(description = "The exact title of the movie to look up") String title) {
        OmdbInfoResponse result = omdbClient.getMovieInfo(title);
        System.out.println(result);

        return result;
    }
}
