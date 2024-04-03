package com.sr.electronic.store.Electronic_Store.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

/*This is Annotation Based Configuration for Info and Security

@io.swagger.v3.oas.annotations.security.SecurityScheme(
        name = "scheme1",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)//Now For this configuration Implementation
 // We need to apply @SecurityRequirement(name = "scheme1") in EachController Class
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "Electronic Store API!!",
                description = "This is Backend of Electronic Store Developed by Mritunjay",
                version = "1.0.0",
                contact = @io.swagger.v3.oas.annotations.info.Contact(
                        name = "Mritunjay Pratap Singh Rajawat",
                        email = "rajawat.sunny512@gmail.com",
                        url = "www.imashanirajawt.com"
                ),
                license = @io.swagger.v3.oas.annotations.info.License(
                        name = "OPEN",
                        url = "www.iamshanirajawat.com"
                )
        ),
        externalDocs = @io.swagger.v3.oas.annotations.ExternalDocumentation(
                description = "BackEnd Docs Of Electronic Store",
                url = "www.iamshanirajawat.com"
        )
)
 */
public class SwaggerConfig{

    @Bean
    public OpenAPI openAPI(){

        String schemeName= "bearerScheme";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(schemeName))
                .components(new Components()
                        .addSecuritySchemes(schemeName,new SecurityScheme()
                                .name(schemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .bearerFormat("JWT")
                                .scheme("bearer")
                        )
                )
                .info(new Info().title("Electronic Store API!!")
                        .description("This is Electronic Store Backend Developed by Shani Rajawat")
                        .version("1.0")
                        .contact(new Contact().name("Shani").email("rajawat.sunny512@gmail.com"))
                        .license(new License().name("RAJAWAT"))
                )
                .externalDocs(new ExternalDocumentation().url("iamshanirajwat.com").description("This is external URL"))
                ;
    }

    // Here we use @Bean Based Configuration But there is another method as well
    // We can use annotation based method for both Security and info
    // means we use we Use @OpenApi

}
