package mapping.strategy;

import mapping.config.MappingDefinition;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

// Contrat commun a toute strategie de transformation (transaction, souscription,
// futur type). Chaque implementation sait construire SON type d'objet canonique.
public interface MappingStrategy {
    boolean supports(String eventType);
    Object mapToCanonical(String rawJson, MappingDefinition config);

    default String extractField(String rawJson, String jsonPath) {
        if (jsonPath == null || jsonPath.isBlank()) return null;
        try {
            Object value = JsonPath.read(rawJson, jsonPath);
            return value != null ? String.valueOf(value) : null;
        } catch (PathNotFoundException e) {
            return null;
        }
    }
}
