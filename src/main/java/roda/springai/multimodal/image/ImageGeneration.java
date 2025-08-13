package roda.springai.multimodal.image;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ImageGeneration {

  private final OpenAiImageModel openAiImageModel;

  @GetMapping("/generate-image")
  public ResponseEntity<Map<String, String>> generateImages(@RequestParam(defaultValue = "A beautiful sunset over Cracow") String prompt) {
    ImageOptions options = OpenAiImageOptions.builder()
        .model("dall-e-3")
        .width(1024)
        .height(1024)
        .quality("hd")
        .style("natural")
        .build();

    ImagePrompt imagePrompt = new ImagePrompt(prompt, options);
    ImageResponse imageResponse = openAiImageModel.call(imagePrompt);

    String url = imageResponse.getResult().getOutput().getUrl();

    return ResponseEntity.ok(Map.of(
        "prompt", prompt,
        "imageUrl", url
    ));
  }
}
