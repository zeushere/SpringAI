package roda.springai.evals;

import static org.assertj.core.api.Assertions.assertThat;
import static roda.springai.evals.Sentiment.POSITIVE;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReviewServiceIT {

  private RelevancyEvaluator relevancyEvaluator;

  @BeforeEach
  void setUp(@Autowired ChatClient.Builder builder) {
    this.relevancyEvaluator = new RelevancyEvaluator(builder);
  }

  @Autowired
  private ReviewService subject;

  @Test
  void classifySentiment_positiveSentiment_shouldCheckByRelevant() {
    //given
    String positiveReview = "I absolutely loved the hotel, it was amazing!";

    EvaluationRequest request = new EvaluationRequest(positiveReview, contextStringsToDocument(List.of(positiveReview)), POSITIVE.name());

    //when
    EvaluationResponse result = relevancyEvaluator.evaluate(request);

    //then
    assertThat(result.isPass()).isTrue();
  }

  @Test
  void classifySentiment_negativeSentiment_shouldCheckByRelevant() {
    //given
    String negativeReview = "I absolutely hate the hotel, it was boring!";

    EvaluationRequest request = new EvaluationRequest(negativeReview, contextStringsToDocument(List.of(negativeReview)), POSITIVE.name());

    //when
    EvaluationResponse result = relevancyEvaluator.evaluate(request);

    //then
    assertThat(result.isPass()).isFalse();
  }

  @Test
  void classifySentiment_positiveSentiment_shouldReturnPositive() {
    //given
    String positiveReview = "I absolutely loved the hotel, it was amazing!";

    //when
    Sentiment result = subject.classifySentiment(positiveReview);

    //then
    assertThat(result).isEqualTo(POSITIVE);
  }

  @Test
  void classifySentiment_negativeSentiment_shouldReturnNegative() {
    //given
    String negativeReview = "I absolutely hate the hotel, it was boring!";

    //when
    Sentiment result = subject.classifySentiment(negativeReview);

    //then
    assertThat(result).isEqualTo(Sentiment.NEGATIVE);
  }

  @Test
  void classifySentiment_neutralSentiment_shouldReturnNeutral() {
    //given
    String neutralReview = "I don't care about this hotel. It's simple just hotel";

    //when
    Sentiment result = subject.classifySentiment(neutralReview);

    //then
    assertThat(result).isEqualTo(Sentiment.NEGATIVE);
  }

  private List<Document> contextStringsToDocument(List<String> contextStrings) {
    return contextStrings.stream()
        .map(Document::new)
        .toList();
  }
}