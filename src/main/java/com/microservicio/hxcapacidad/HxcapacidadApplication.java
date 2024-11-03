package com.microservicio.hxcapacidad;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "Spring webflux crud example",
		version = "1.0",
		description = "Apis microservicio de capacidades"
))
public class HxcapacidadApplication {

	public static void main(String[] args) {
		SpringApplication.run(HxcapacidadApplication.class, args);
	}

}
