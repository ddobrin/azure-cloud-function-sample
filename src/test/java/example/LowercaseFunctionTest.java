package example;

import com.microsoft.azure.functions.ExecutionContext;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.function.adapter.azure.AzureSpringBootRequestHandler;

import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

public class LowercaseFunctionTest {

    @Test
    public void test() {
        String result = new LowercaseFunction().lowercase().apply("{\"greeting\": \"HELLO\",\"name\": \"Your_name\"}");
        assertThat(result).isEqualTo("{\"greeting\":\"hello\",\"name\":\"your_name\"}");
    }

    @Test
    public void start() throws Exception {
        AzureSpringBootRequestHandler<String, String> handler = new AzureSpringBootRequestHandler<>(
                LowercaseFunction.class);
        ExecutionContext ec = new ExecutionContext() {
            @Override
            public Logger getLogger() {
                return Logger.getAnonymousLogger();
            }

            @Override
            public String getInvocationId() {
                return "id2";
            }

            @Override
            public String getFunctionName() {
                return "lowercase";
            }
        };

        String result = handler.handleRequest("{\"greeting\": \"HELLO\",\"name\": \"Your_name\"}", ec);
        handler.close();
        assertThat(result).isEqualTo("{\"greeting\":\"hello\",\"name\":\"your_name\"}");
    }
}
