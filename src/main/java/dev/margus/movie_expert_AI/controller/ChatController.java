package dev.margus.movie_expert_ai.controller;

import dev.margus.movie_expert_ai.model.MovieResponse;
import dev.margus.movie_expert_ai.tool.MovieTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.vectorstore.SearchRequest;
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

    @GetMapping("/savvy")
    public String getMovieSavvy(@RequestParam String message) {
        PromptTemplate ragPrompt = new PromptTemplate("""
                EXCLUSIVE CATALOG CONTEXT:
                {question_answer_context}
                
                USER QUERY:
                """ + message);

        return chatClient.prompt()
                .advisors(QuestionAnswerAdvisor.builder(vectorStore)
                        .searchRequest(SearchRequest.builder().topK(5).build())
                        .build())
                .tools(movieTools)
                .user(ragPrompt.getTemplate())
                .call()
                .content();
    }
}
