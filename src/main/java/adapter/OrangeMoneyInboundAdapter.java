package adapter;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import mapping.config.MappingConfigRegistry;
import mapping.config.MappingDefinition;
import mapping.strategy.MappingStrategy;
import mapping.strategy.MappingStrategyRegistry;
import model.canonical.CanonicalEvent;
import org.springframework.stereotype.Component;

import java.util.List;

// Renomme depuis "GenericMobileMoneyAdapter" : le projet est dedie a Orange
// Money, inutile de garder un nom qui suggere un support multi-operateurs.
//
// Cette classe ne connait AUCUN nom de champ Orange Money specifiquement.
// Elle sait juste :
//   1. Lire tous les fichiers de mapping disponibles (transaction.yml,
//      subscription.yml, ...)
//   2. Deviner lequel correspond au JSON recu (grace au "detection.jsonPath")
//   3. Deleguer la transformation a la bonne strategie (Transaction ou
//      Subscription)
@Component
public class OrangeMoneyInboundAdapter implements InboundAdapter {

    private final MappingConfigRegistry configRegistry;
    private final MappingStrategyRegistry strategyRegistry;

    public OrangeMoneyInboundAdapter(MappingConfigRegistry configRegistry,
            MappingStrategyRegistry strategyRegistry) {
        this.configRegistry = configRegistry;
        this.strategyRegistry = strategyRegistry;
    }

    @Override
    public CanonicalEvent processInbound(String rawJsonPayload) {
        MappingDefinition matchedConfig = detectDefinition(configRegistry.getAllDefinitions(), rawJsonPayload);

        MappingStrategy strategy = strategyRegistry.getStrategy(matchedConfig.getEventType());
        return (CanonicalEvent) strategy.mapToCanonical(rawJsonPayload, matchedConfig);
    }

    // Regarde, dans l'ordre, chaque fichier de mapping connu et retient le
    // premier dont la regle de detection correspond au JSON recu.
    // Exemple : si le JSON contient "$.transaction_id", c'est transaction.yml.
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
                // Ce champ n'existe pas dans ce payload : on essaie le fichier de mapping suivant
            }
        }
        throw new IllegalArgumentException(
                "Aucun schema de mapping Orange Money ne correspond au payload recu.");
    }
}
