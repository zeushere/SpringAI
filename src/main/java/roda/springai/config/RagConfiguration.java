package roda.springai.config;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@Slf4j
public class RagConfiguration {

  private final String vectorStoreName = "vectorstore.json";

  @Value("classpath:/data/models.json")
  private Resource models;

  @Bean
  public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
    var simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
    var vectorStoreFile = getVectorStore();

    if (vectorStoreFile.exists()) {
      log.info("Vector store already exists at {}", vectorStoreFile.getAbsolutePath());
      simpleVectorStore.load(vectorStoreFile);
    } else {
      log.info("Creating new vector store at {}", vectorStoreFile.getAbsolutePath());
      TextReader textReader = new TextReader(models);
      textReader.getCustomMetadata().put("filename", "models.txt");
      List<Document> documents = textReader.get();
      TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
      List<Document> splitDocuments = tokenTextSplitter.apply(documents);

      simpleVectorStore.add(splitDocuments);
      simpleVectorStore.save(vectorStoreFile);
    }

    return simpleVectorStore;
  }

  private File getVectorStore() {
    Path path = Paths.get("src", "main", "resources", "data");
    String absolutePath = path.toAbsolutePath() + "/" + vectorStoreName;
    return new File(absolutePath);
  }
}
