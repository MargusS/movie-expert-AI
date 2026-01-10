package dev.margus.movie_expert_ai.controller;

import dev.margus.movie_expert_ai.model.MovieResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movie-chat")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/suggestions/{genre}")
    public MovieResponse getMethodName(@PathVariable String genre, @RequestParam(defaultValue = "default-session") String chatId) {

        MovieResponse result = chatClient.prompt()
                .user(u -> u.text("""
                                Suggest 5 movies for the genre {genre}.
                                CRITICAL: Check the JSON history above for previous suggestions. Do NOT suggest any movie title that has already appeared in the conversation history.
                                """)
                        .param("genre", genre))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .entity(MovieResponse.class);

        return result;
    }
}
