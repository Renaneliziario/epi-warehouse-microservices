package br.com.renan.almoxarifado.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI epiWithdrawalOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Servico de Retirada de EPI")
                .description("Catalogo de EPI e retiradas do almoxarifado")
                .version("v1"));
    }
}
