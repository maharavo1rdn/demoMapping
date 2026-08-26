package mapping.strategy;

import org.springframework.stereotype.Component;
import java.util.List;

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
                .orElseThrow(() -> new IllegalArgumentException("Aucune stratégie de mapping trouvée pour : " + eventType));
    }
}