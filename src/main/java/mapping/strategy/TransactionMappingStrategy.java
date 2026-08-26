package mapping.strategy;

import mapping.config.MappingDefinition;
import model.CanonicalTransaction;
import model.Money;
import model.Party;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Component
public class TransactionMappingStrategy implements MappingStrategy {

    @Override
    public boolean supports(String eventType) {
        return "TRANSACTION".equalsIgnoreCase(eventType);
    }

    @Override
    public Object mapToCanonical(String rawJson, MappingDefinition config) {
        Map<String, String> fields = config.getFields();

        String externalRef = extractField(rawJson, fields.get("externalRef"));
        String amountStr = extractField(rawJson, fields.get("amount"));
        String currency = extractField(rawJson, fields.get("currency"));
        String senderPhone = extractField(rawJson, fields.get("senderMsisdn"));
        String recipientAccount = extractField(rawJson, fields.get("recipientAccount"));

        Money money = (amountStr != null && !amountStr.isBlank()) 
            ? new Money(new BigDecimal(amountStr), currency) : null;

        Party sender = (senderPhone != null) ? new Party("MSISDN", senderPhone) : null;
        Party recipient = (recipientAccount != null) ? new Party("BANK_ACCOUNT", recipientAccount) : null;

        return new CanonicalTransaction(
            "TX-" + UUID.randomUUID().toString().substring(0, 8),
            externalRef,
            config.getProvider(),
            config.getEventType(),
            money,
            sender,
            recipient,
            LocalDateTime.now(),
            rawJson
        );
    }
}