package roda.springai.multimodal.image;

import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageDetection {

  private final ChatClient chatClient;

  @Value(value = "classpath:/images/simple-image.jpg")
  private Resource simpleImage;

  public ImageDetection(@Qualifier("openAIChatClient") ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @GetMapping("/image-to-text")
  public String imageToText() {
    return chatClient.prompt()
        .user(u ->
            u.text("Can you please describe what You see on the following images.")
                .media(IMAGE_JPEG, simpleImage))
        .call()
        .content();
  }
}