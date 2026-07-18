package spring.ai.example.spring_ai_demo;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

// import com.openai.models.beta.assistants.AssistantCreateParams.ToolResources.FileSearch.VectorStore;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.vectorstore.VectorStore;
@Component
public class PdfLoader {
    private final VectorStore vectorStore;

    public PdfLoader(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void init() {

        Resource pdf = new ClassPathResource("docs/Java_Memory_Model.pdf");

        PagePdfDocumentReader reader = new PagePdfDocumentReader(pdf);

        List<Document> docs = reader.get();

        TokenTextSplitter splitter = new TokenTextSplitter();

        List<Document> chunks = splitter.apply(docs);

        vectorStore.add(chunks);

        System.out.println("Loaded " + chunks.size() + " chunks ");

    }
}
