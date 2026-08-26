package adapter;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import mapping.config.MappingConfigRegistry;
import mapping.config.MappingDefinition;
import mapping.strategy.MappingStrategy;
import mapping.strategy.MappingStrategyRegistry;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenericMobileMoneyAdapter {

    private final MappingConfigRegistry configRegistry;
    private final MappingStrategyRegistry strategyRegistry;

    public GenericMobileMoneyAdapter(MappingConfigRegistry configRegistry,
            MappingStrategyRegistry strategyRegistry) {
        this.configRegistry = configRegistry;
        this.strategyRegistry = strategyRegistry;
    }

    public Object processInbound(String providerName, String rawJsonPayload) {
        List<MappingDefinition> providerConfigs = configRegistry.getDefinitionsForProvider(providerName);

        MappingDefinition matchedConfig = detectDefinition(providerConfigs, rawJsonPayload);

        MappingStrategy strategy = strategyRegistry.getStrategy(matchedConfig.getEventType());
        return strategy.mapToCanonical(rawJsonPayload, matchedConfig);
    }

    private MappingDefinition detectDefinition(List<MappingDefinition> configs, String rawJsonPayload) {
        for (MappingDefinition config : configs) {
            MappingDefinition.DetectionRule rule = config.getDetection();
            try {
                Object extractedValue = JsonPath.read(rawJsonPayload, rule.getJsonPath());

                if (extractedValue != null) {
                    if (rule.getExpectedValue() == null ||
                            rule.getExpectedValue().equalsIgnoreCase(String.valueOf(extractedValue))) {
                        return config;
                    }
                }
            } catch (PathNotFoundException ignored) {
                // Le JSONPath n'existe pas dans ce payload, on passe au fichier de config
                // suivant
            }
        }
        throw new IllegalArgumentException(
                "Aucun schéma de mapping ne correspond au payload entrant pour ce provider.");
    }
}