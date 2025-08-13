package roda.springai.evals;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.dockerjava.api.model.HostConfig;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.ollama.OllamaContainer;

@SpringBootTest
@Testcontainers
public class FactCheckingEvaluatorIT {

  private static final String OLLAMA_IMAGE = "ollama/ollama:0.1.48";

  @Container
  static final OllamaContainer ollama =
      new OllamaContainer(OLLAMA_IMAGE)
          .withCreateContainerCmdModifier(cmd -> {
            if (cmd.getHostConfig() == null) {
              cmd.withHostConfig(new HostConfig());
            }
            HostConfig hostConfig = cmd.getHostConfig();
            hostConfig.withRuntime("runc");
            hostConfig.withDeviceRequests(new ArrayList<>());
          });

  @DynamicPropertySource
  static void registerOllamaProps(DynamicPropertyRegistry registry) {
    registry.add("spring.ai.ollama.base-url",
        () -> "http://" + ollama.getHost() + ":" + ollama.getFirstMappedPort());
  }

  private FactCheckingEvaluator factCheckingEvaluator;

  @BeforeEach
  void setUp(@Autowired ChatClient.Builder builder) {
    factCheckingEvaluator = new FactCheckingEvaluator(builder);
  }

  @Test
  void evaluate_thenResponseIsTrue() {
    // given
    String contextDocument = """
        The Eiffel Tower is a wrought-iron lattice tower located on the Champ de Mars in Paris, France. \
        It was designed and built by Gustave Eiffel's company and completed in 1889 as the entrance to the \
        1889 World's Fair.""";

    String aiClaim = "The Eiffel Tower was completed in 1889.";
    String prompt = "When was the Eiffel Tower completed?";

    EvaluationRequest request = new EvaluationRequest(
        prompt,
        List.of(new Document(contextDocument)),
        aiClaim
    );

    // when
    EvaluationResponse result = factCheckingEvaluator.evaluate(request);

    // then
    assertThat(result.isPass()).isTrue();
  }

  @Test
  void evaluate_thenResponseIsFalse() {
    // given
    String contextDocument = """
        The Eiffel Tower is a wrought-iron lattice tower located on the Champ de Mars in Paris, France. \
        It was designed and built by Gustave Eiffel's company and completed in 1889 as the entrance to the \
        1889 World's Fair.""";

    String aiClaim = "The Eiffel Tower is located in Berlin, Germany.";
    String prompt = "Where is the Eiffel Tower?";

    EvaluationRequest request = new EvaluationRequest(
        prompt,
        List.of(new Document(contextDocument)),
        aiClaim
    );

    // when
    EvaluationResponse response = factCheckingEvaluator.evaluate(request);

    // then
    assertThat(response.isPass()).isFalse();
  }
}
