package roda.springai.tools.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WeatherTools {

  private static final String BASE_URL = "https://api.weather.gov";
  private final RestClient restClient;

  public WeatherTools(RestClient.Builder builder) {
    this.restClient = builder
        .baseUrl(BASE_URL)
        .defaultHeader("Accept", "application/geo+json")
        .defaultHeader("User-Agent", "WeatherApiClient/1.0 (your@email.com)")
        .build();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Alert(@JsonProperty("features") List<Feature> features) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Feature(@JsonProperty("properties") Properties properties) {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Properties(@JsonProperty("event") String event, @JsonProperty("areaDesc") String areaDesc,
                             @JsonProperty("severity") String severity, @JsonProperty("description") String description,
                             @JsonProperty("instruction") String instruction) {

    }
  }

  @Tool(description = "Get weather alerts for a US state. Input is Two-letter US state code (e.g. CA, NY)")
  public String getAlerts(@ToolParam(description = "Two-letter US state code (e.g. CA, NY") String state) {
    Alert alert = restClient.get().uri("/alerts/active/area/{state}", state).retrieve().body(Alert.class);

    return alert.features()
        .stream()
        .map(f -> String.format("""
                Event: %s
                Area: %s
                Severity: %s
                Description: %s
                Instructions: %s
                """, f.properties().event(), f.properties.areaDesc(), f.properties.severity(),
            f.properties.description(), f.properties.instruction()))
        .collect(Collectors.joining("\n"));
  }

}