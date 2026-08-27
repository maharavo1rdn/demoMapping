package mapping.strategy;

import org.springframework.stereotype.Component;
import java.util.List;

// Spring injecte ici TOUTES les classes qui implementent MappingStrategy
// (TransactionMappingStrategy, SubscriptionMappingStrategy, et toute
// future strategie). Ajouter un type = ajouter une classe @Component,
// cette classe la trouvera seule.
@Component
public class MappingStrategyRegistry {
    private final List<MappingStrategy> strategies;

    public MappingStrategyRegistry(List<MappingStrategy> strategies) {
        this.strategies = strategies;
    }

    public MappingStrategy getStrategy(String eventType) {
        return strategies.stream()
                .filter(s -> s.supports(eventType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Aucune strategie de mapping trouvee pour : " + eventType));
    }
}
