package roda.springai.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@Slf4j
public class RagConfiguration {

  private final String vectorStoreName = "vectorstore.json";

  @Value("classpath:/data/models.json")
  private Resource models;
}
