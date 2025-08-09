package roda.springai.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController {

  private final ChatClient chatClient;

  public ArticleController(@Qualifier("openAIChatClient") ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @GetMapping("/posts/new")
  public String newPost(@RequestParam(value = "topic", defaultValue = "JDK Virtual Threads") String topic) {
    
  }
}
