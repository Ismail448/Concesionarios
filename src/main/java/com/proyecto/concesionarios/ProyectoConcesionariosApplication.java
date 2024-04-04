package com.proyecto.concesionarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProyectoConcesionariosApplication {

	public static void main(String[] args) {
		String environment = System.getenv("ENVIRONMENT");
		if (environment != null && environment.equals("cloud")) {
			System.setProperty("spring.profiles.active", "cloud");
		} else {
			System.setProperty("spring.profiles.active", "local");
		}
		SpringApplication.run(ProyectoConcesionariosApplication.class, args);
	}

}
