package roda.springai.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

  private final ChatClient chatClient;

  public ChatController(@Qualifier("openAIChatClient") ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @GetMapping("/chat")
  public String chat() {
    return chatClient.prompt()
        .user("Tell me an interesting fact about Java")
        .call()
        .content();
  }

  @GetMapping("/stream")
  public Flux<String> stream() {
    return chatClient.prompt()
        .user("I'm visiting Cracow soon, can You give me 10 places I must visit?")
        .stream()
        .content();
  }

  @GetMapping("/joke")
  public ChatResponse joke() {
    return chatClient.prompt()
        .user("Tell me a dad joke about dogs")
        .call()
        .chatResponse();
  }
}
