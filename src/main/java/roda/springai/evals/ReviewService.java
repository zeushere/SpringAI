package roda.springai.evals;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

  private final ChatClient chatClient;

  public ReviewService(ChatClient.Builder builder) {
    this.chatClient = builder
        .defaultOptions(OpenAiChatOptions.builder().temperature(0.1d).build())
        .build();
  }

  public Sentiment classifySentiment(String review) {
    String system = """
        Classify the sentiment of the following text as POSITIVE, NEGATIVE, or NEUTRAL.
        Your response must be only of there three words.
        """;

    return chatClient.prompt()
        .system(system)
        .user(review)
        .call()
        .entity(Sentiment.class);
  }
}