package spharos.nu.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .components(new Components())
            .components(new io.swagger.v3.oas.models.Components())
            .info(apiInfo());
    }


    private Info apiInfo() {
        return new Info()
            .title("Nu - Springdoc 문서")
            .description("Springdoc을 사용한 Swagger UI")
            .version("1.0.0");
    }
}
