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
import java.util.stream.Collectors;

@Component
public class MappingConfigRegistry {

    private final List<MappingDefinition> definitions = new ArrayList<>();
    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    @PostConstruct
    public void loadAllYamlConfigs() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:mappings/**/*.yml");

            for (Resource resource : resources) {
                MappingDefinition def = yamlMapper.readValue(resource.getInputStream(), MappingDefinition.class);
                definitions.add(def);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Erreur lors du chargement des configurations YAML de mapping", e);
        }
    }

    public List<MappingDefinition> getDefinitionsForProvider(String provider) {
        return definitions.stream()
                .filter(d -> d.getProvider().equalsIgnoreCase(provider))
                .collect(Collectors.toList());
    }
}