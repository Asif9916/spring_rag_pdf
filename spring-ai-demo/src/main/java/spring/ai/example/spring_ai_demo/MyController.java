package spring.ai.example.spring_ai_demo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.ai.vectorstore.VectorStore;

@RestController
public class MyController {

        private final ChatClient chatClient;
        private final VectorStore vectorStore;

        public MyController(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
                this.chatClient = chatClientBuilder.build();
                this.vectorStore = vectorStore;

        }

        @GetMapping("/ask")
        public String ask(
                        @RequestParam String question) {

                List<Document> docs = vectorStore.similaritySearch(question);

                System.out.println("Documents found: " + docs.size());

                docs.forEach(d -> {
                        System.out.println("--------------------");
                        System.out.println(d.getText());
                });
                String context = docs.stream()
                                .map(Document::getText)
                                .collect(Collectors.joining("\n"));

                return chatClient.prompt()

                                .system("""
                                                Answer ONLY from the supplied context.
                                                If the answer isn't present,
                                                say 'I don't know.'
                                                """)

                                .user("""
                                                Context:

                                                %s

                                                Question:

                                                %s
                                                """.formatted(context, question))

                                .call()
                                .content();
        }
}
