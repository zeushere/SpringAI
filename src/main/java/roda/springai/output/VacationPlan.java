package roda.springai.output;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VacationPlan {

  private final ChatClient chatClient;

  public VacationPlan(@Qualifier("openAIChatClient") ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @GetMapping("/vacation/unstructured")
  public String unstructured() {
    return chatClient.prompt()
        .user("I want to plan a trip to Hawaii. Give me a list of things to do.")
        .call()
        .content();
  }

  @GetMapping("/vacation/structured")
  public Itinerary structured() {
    return chatClient.prompt()
        .user("I want to plan a trip to Hawaii. Give me a list of things to do.")
        .call()
        .entity(Itinerary.class);
  }
}