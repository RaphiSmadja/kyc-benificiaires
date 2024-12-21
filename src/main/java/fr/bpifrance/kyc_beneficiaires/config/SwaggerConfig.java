package fr.bpifrance.kyc_beneficiaires.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gestion des KYC BPI")
                        .version("1.0")
                        .description("Documentation de l'API pour l'application des KYC."));
    }
}