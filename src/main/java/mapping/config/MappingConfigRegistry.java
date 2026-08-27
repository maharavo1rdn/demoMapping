package mapping.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

// Charge TOUS les fichiers YAML de mapping au demarrage. Ajouter un
// nouveau type d'evenement Orange Money = deposer un nouveau fichier
// dans resources/mapping/orangemoney/, cette classe le trouvera seule.
@Component
public class MappingConfigRegistry {

    private final List<MappingDefinition> definitions = new ArrayList<>();
    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    @PostConstruct
    public void loadAllYamlConfigs() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            // BUG CORRIGE : le dossier reel est "resources/mapping/orangemoney/*.yml"
            // (singulier), le pattern cherchait "mappings" (avec un s) et ne
            // trouvait donc jamais rien.
            Resource[] resources = resolver.getResources("classpath:mapping/**/*.yml");

            for (Resource resource : resources) {
                MappingDefinition def = yamlMapper.readValue(resource.getInputStream(), MappingDefinition.class);
                definitions.add(def);
            }

            if (definitions.isEmpty()) {
                throw new IllegalStateException(
                        "Aucun fichier de mapping trouve dans classpath:mapping/**/*.yml");
            }
        } catch (IOException e) {
            throw new IllegalStateException("Erreur lors du chargement des configurations YAML de mapping", e);
        }
    }

    // Le projet est dedie a Orange Money : pas besoin de filtrer par
    // opérateur, on renvoie simplement toutes les definitions connues.
    public List<MappingDefinition> getAllDefinitions() {
        return Collections.unmodifiableList(definitions);
    }
}
