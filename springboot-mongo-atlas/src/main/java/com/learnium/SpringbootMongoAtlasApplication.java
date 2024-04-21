package com.learnium;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Springboot Mongo Atlas", version = "1.0", description = "Springboot Mongo Atlas"))
public class SpringbootMongoAtlasApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootMongoAtlasApplication.class, args);
	}

}
