package roda.springai.output;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import roda.springai.config.ChatClientTestConfiguration;

@SpringBootTest
@Import(ChatClientTestConfiguration.class)
class VacationPlanIT {

  @Autowired
  private ChatClient chatClient;

  @Test
  void structured_shouldReturnItinerary() {
    //given
    String destination = "Cracow, PL";
    String prompt = "What's a good vacation plan while I'm in {destination} for 3 days?";

    //when
    var result = chatClient.prompt()
        .user(promptUserSpec -> {
          promptUserSpec.text(prompt);
          promptUserSpec.param("destination", destination);
        })
        .call()
        .entity(Itinerary.class);

    //then
    assertThat(result).isOfAnyClassIn(Itinerary.class);
  }
}