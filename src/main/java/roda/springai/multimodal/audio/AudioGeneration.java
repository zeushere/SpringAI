package roda.springai.multimodal.audio;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi.SpeechRequest.AudioResponseFormat;
import org.springframework.ai.openai.api.OpenAiAudioApi.SpeechRequest.Voice;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AudioGeneration {

  private final OpenAiAudioSpeechModel openAiAudioSpeechModel;

  @GetMapping("/speak")
  public ResponseEntity<byte[]> generateSpeech(
      @RequestParam(defaultValue = "It's a great time to be a Java & Spring Developer") String text) {
    var options = OpenAiAudioSpeechOptions.builder()
        .model("tts-1-hd")
        .voice(Voice.ONYX)
        .responseFormat(AudioResponseFormat.MP3)
        .speed(1.0F)
        .build();

    SpeechPrompt speechPrompt = new SpeechPrompt(text, options);
    SpeechResponse speechResponse = openAiAudioSpeechModel.call(speechPrompt);

    byte[] audioBytes = speechResponse.getResult().getOutput();

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"speech.mp3\"")
        .body(audioBytes);
  }
}
