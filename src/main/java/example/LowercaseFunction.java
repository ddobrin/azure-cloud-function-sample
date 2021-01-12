package example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

@SpringBootApplication
public class LowercaseFunction {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(LowercaseFunction.class, args);
    }

    @Bean
    public Function<String, String> lowercase() {
        return value -> {
            ObjectMapper mapper = new ObjectMapper();

            try {
                Map<String, String> map = mapper.readValue(value, Map.class);

                if(map != null)
                    map.forEach((k, v) -> map.put(k, v != null ? v.toLowerCase() : null));

                return mapper.writeValueAsString(map);
            } catch (IOException e) {
                return ("Function error: - bad request");
            }
        };
    }
}
