package com.manuel.chat.chatwebsockets.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        servers = {
                @Server(url = "http://localhost:8080", description = "Servidor local de desarrollo")
        }
)
public class OpenApiConfiguration {

    @Bean
    public OpenAPI detailsOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Chat Web Sockets Public API")
                                .description("Documentación de la API pública del chat")
                                .version("1.0")
                                .contact(
                                        new Contact()
                                                .name("Manuel Jiménez Bravo")
                                                .email("jbmanuel16@gmail.com")
                                                .url("https://github.com/manutatum")
                                )
                                .license(
                                        new License()
                                                .name("MIT License")
                                                .url("https://opensource.org/licenses/MIT")
                                )
                )
                .components(
                        new Components().addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                )
                )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }

}