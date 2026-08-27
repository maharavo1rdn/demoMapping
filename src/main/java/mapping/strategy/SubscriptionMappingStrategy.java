package mapping.strategy;

import mapping.config.MappingDefinition;
import model.canonical.CanonicalSubscription;
import model.Party;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Component
public class SubscriptionMappingStrategy implements MappingStrategy {

    @Override
    public boolean supports(String eventType) {
        return "SUBSCRIPTION".equalsIgnoreCase(eventType);
    }

    @Override
    public Object mapToCanonical(String rawJson, MappingDefinition config) {
        Map<String, String> fields = config.getFields();

        String externalRef = extractField(rawJson, fields.get("externalRef"));
        String senderPhone = extractField(rawJson, fields.get("senderMsisdn"));
        String recipientAccount = extractField(rawJson, fields.get("recipientAccount"));

        return new CanonicalSubscription(
                "SUB-" + UUID.randomUUID().toString().substring(0, 8),
                externalRef,
                config.getProvider(),
                new Party("MSISDN", senderPhone),
                new Party("BANK_ACCOUNT", recipientAccount),
                LocalDateTime.now()
        );
    }
}
