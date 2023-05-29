package it.develhope.javaTeam2Develhope.configurations;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition
@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI baseOpenApi(){
        return new OpenAPI().info(new Info().title("Dante's Sandwich")
                        .version("1.0.0")
                        .description("A new online library from Giulia Contarino, Alessio Limina and Eduard Ancuta")
                        .contact(new Contact()
                                .name("Dante's Sandwich")
                                .email("dantesandwich23@gmail.com")
                                .url("https://www.dantesandwich.it")));
    }
}
