package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

// Point d'entree Spring Boot classique. Une fois lance (mvn spring-boot:run),
// l'API ecoute sur http://localhost:8080/api/orangemoney/notifications
// et attend qu'Orange Money (ou toi, via curl/Postman pour tester) l'appelle.
@SpringBootApplication
@ComponentScan(basePackages = {"controller", "service", "adapter", "mapping", "repository", "demo"})
public class DemoMappingApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoMappingApplication.class, args);
    }
}
