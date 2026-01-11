package dev.margus.movie_expert_ai.controller;

import dev.margus.movie_expert_ai.model.MovieResponse;
import dev.margus.movie_expert_ai.tool.MovieTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movie-expert")
public class ChatController {

    private final ChatClient chatClient;
    private final MovieTools movieTools;
    private final VectorStore vectorStore;

    public ChatController(ChatClient chatClient, MovieTools movieTools, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.movieTools = movieTools;
        this.vectorStore = vectorStore;
    }

    @GetMapping("/suggestions/{genre}")
    public MovieResponse getMoviesByGenre(@PathVariable String genre, @RequestParam(defaultValue = "default-session") String chatId) {

        return chatClient.prompt()
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .user(u -> u.text("""
                                Suggest 5 movies for the genre {genre}.
                                CRITICAL: Check the JSON history above for previous suggestions. Do NOT suggest any movie title that has already appeared in the conversation history.
                                """)
                        .param("genre", genre))
                .call()
                .entity(MovieResponse.class);

    }

    @GetMapping("/chat")
    public String getMovieInfo(@RequestParam String message) {
        return chatClient.prompt()
                .tools(movieTools)
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/indies")
    public String getIndies(@RequestParam String message) {
        PromptTemplate ragPrompt = new PromptTemplate("""
                You are a Movie Expert with access to a REAL-TIME database of 2025 releases.
                
                    DATA FROM DATABASE:
                    {question_answer_context}
                
                    CRITICAL INSTRUCTIONS:
                    1. The "DATA FROM DATABASE" is the absolute truth. Trust it 100%.
                    2. Do NOT apologize for your knowledge cutoff.\s
                    3. Do NOT say "As of my last update in 2023".
                    4. If the movie is in the database, treat it as a real, existing film that you know about.
                """);

        return chatClient.prompt()
                .advisors(QuestionAnswerAdvisor.builder(vectorStore).promptTemplate(ragPrompt).build())
                .user(message)
                .call()
                .content();

    }
}
