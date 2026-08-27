import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Point d'entree Spring Boot classique. Une fois lance (mvn spring-boot:run),
// l'API ecoute sur http://localhost:8080/api/orangemoney/notifications
// et attend qu'Orange Money (ou toi, via curl/Postman pour tester) l'appelle.
@SpringBootApplication
public class DemoMappingApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoMappingApplication.class, args);
    }
}
