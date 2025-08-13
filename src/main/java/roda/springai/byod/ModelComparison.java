package roda.springai.byod;

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
            If You are asked about up to date language models and their current context windows, here is information about Your response. Use only this information in friendly form for UX:
            
            {
                "company": "OpenAI",
                "model": "GPT-4o",
                "context_window_size": 128000
              },
              {
                "company": "OpenAI",
                "model": "GPT-4 Turbo",
                "context_window_size": 128000
              },
              {
                "company": "OpenAI",
                "model": "GPT-4.1",
                "context_window_size": 1000000
              },
              {
                "company": "OpenAI",
                "model": "o1-preview",
                "context_window_size": 128000
              },
              {
                "company": "OpenAI",
                "model": "GPT-3.5 Turbo",
                "context_window_size": 16000
              },
              {
                "company": "Anthropic",
                "model": "Claude 3 Opus",
                "context_window_size": 200000
              },
              {
                "company": "Anthropic",
                "model": "Claude 3 Sonnet",
                "context_window_size": 200000
              },
              {
                "company": "Anthropic",
                "model": "Claude 3 Haiku",
                "context_window_size": 200000
              },
              {
                "company": "Google",
                "model": "Gemini 1.5 Pro (1M tier)",
                "context_window_size": 1000000
              },
              {
                "company": "Google",
                "model": "Gemini 1.5 Flash",
                "context_window_size": 1000000
              },
              {
                "company": "Google",
                "model": "Gemini 2.0 Pro",
                "context_window_size": 2000000
              },
              {
                "company": "Google",
                "model": "Gemini 2.0 Flash",
                "context_window_size": 1000000
              },
              {
                "company": "Google",
                "model": "Gemini 2.5 Pro",
                "context_window_size": 1000000
              },
              {
                "company": "Meta AI",
                "model": "Llama 3.1 405B",
                "context_window_size": 128000
              },
              {
                "company": "Meta AI",
                "model": "Llama 3 70B",
                "context_window_size": 128000
              },
              {
                "company": "xAI",
                "model": "Grok 1.5",
                "context_window_size": 128000
              },
              {
                "company": "xAI",
                "model": "Grok 3",
                "context_window_size": 1000000
              },
              {
                "company": "Mistral AI",
                "model": "Mistral Large 2",
                "context_window_size": 128000
              },
              {
                "company": "Mistral AI",
                "model": "Mixtral 8x22B",
                "context_window_size": 64000
              },
              {
                "company": "Mistral AI",
                "model": "Mistral NeMo 12B",
                "context_window_size": 128000
              },
              {
                "company": "Mistral AI",
                "model": "Mistral Small 3.1",
                "context_window_size": 128000
              },
              {
                "company": "Alibaba Cloud",
                "model": "Qwen2.5 72B",
                "context_window_size": 128000
              },
              {
                "company": "Alibaba Cloud",
                "model": "Qwen2.5 Turbo Long",
                "context_window_size": 1000000
              },
              {
                "company": "DeepSeek",
                "model": "DeepSeek V3",
                "context_window_size": 128000
              },
              {
                "company": "DeepSeek",
                "model": "DeepSeek R1",
                "context_window_size": 128000
              },
              {
                "company": "Cohere",
                "model": "Command R",
                "context_window_size": 128000
              },
              {
                "company": "Cohere",
                "model": "Command R+",
                "context_window_size": 128000
              },
              {
                "company": "Cohere",
                "model": "Command A",
                "context_window_size": 256000
              },
              {
                "company": "Databricks",
                "model": "DBRX Instruct",
                "context_window_size": 32000
              },
              {
                "company": "AI21 Labs",
                "model": "Jamba 1.5 Large",
                "context_window_size": 256000
              },
              {
                "company": "IBM",
                "model": "Granite 3B/8B Long",
                "context_window_size": 128000
              },
              {
                "company": "Amazon AWS",
                "model": "Amazon Nova Micro",
                "context_window_size": 128000
              },
              {
                "company": "Amazon AWS",
                "model": "Amazon Nova Pro",
                "context_window_size": 300000
              }
            """)
        .user("Can You give me an up to date list popular large language models and their current context windows?")
        .call()
        .content();
  }
}
