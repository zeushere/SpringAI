package roda.springai.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModelComparison {

  private final ChatClient chatClient;

  public ModelComparison(@Qualifier("openAIChatClient") ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @GetMapping("/models")
  public String models() {
    return chatClient.prompt()
        .user("Can You give me an up to date list popular large language models and their current context windows?")
        .call()
        .content();
  }

  @GetMapping("/models/stuff-the-prompt")
  public String modelsStuffThePrompt() {
    return chatClient.prompt()
        .system("""
            If You are asked about up to date language models and their current context windows, here is some information to help you with your response:
            
            1. GPT-3 (Generative Pre-trained Transformer 3) - 2048 tokens
            2. BERT (Bidirectional Encoder Representations from Transformers) - 512 tokens
            3. XLNet (eXtreme Learning Network) - 512 tokens
            4. T5 (Text-to-Text Transfer Transformer) - 1024 tokens
            5. RoBERTa (Robustly optimized BERT approach) - 512 tokens
            6. GPT-2 (Generative Pre-trained Transformer 2) - 1024 tokens
            7. DistilBERT (Distilled BERT) - 512 tokens 
            """)
        .user("Can You give me an up to date list popular large language models and their current context windows?")
        .call()
        .content();
  }
}
