package example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class HelloFunction {

    @Value("${TARGET:from-function}")
    String target;

    public static void main(String[] args) {
        SpringApplication.run(HelloFunction.class, args);
    }

    @Bean
    public Function<String, String> hello() {
        return (in) -> "Hello: " + in + ", Source: " + target;
    }
}
