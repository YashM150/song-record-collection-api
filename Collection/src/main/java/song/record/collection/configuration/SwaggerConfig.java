package song.record.collection.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI songCollectionOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Song Record Collection API")
                        .description("Song Record Collection — secured with Basic Auth")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Song Collection Team")
                                .email("dev@songcollection.com"))
                        .license(new License()
                                .name("MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development")))


                .components(new Components()
                        .addSecuritySchemes("basicAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")
                                        .description("Enter your username and password")))


                .addSecurityItem(new SecurityRequirement()
                        .addList("basicAuth"));
    }
}