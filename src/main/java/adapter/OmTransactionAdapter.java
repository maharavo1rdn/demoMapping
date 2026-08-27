package adapter;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import model.TransactionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class OmTransactionAdapter {

    @Value("classpath:mappings/om-transaction.yml")
    private Resource yamlResource;

    private Map<String, String> fieldsMapping;

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void init() throws Exception {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = yamlResource.getInputStream()) {
            Map<String, Object> rules = yaml.load(inputStream);
            this.fieldsMapping = (Map<String, String>) rules.get("fields");
        }
    }

    public TransactionRequest parse(String rawJson) {
        DocumentContext jsonContext = JsonPath.parse(rawJson);

        String externalRef = jsonContext.read(fieldsMapping.get("externalRef"));
        Object rawAmount = jsonContext.read(fieldsMapping.get("amount"));
        String currency = jsonContext.read(fieldsMapping.get("currency"));
        String customerMsisdn = jsonContext.read(fieldsMapping.get("customerMsisdn"));
        String status = jsonContext.read(fieldsMapping.get("status"));

        return new TransactionRequest(
                externalRef,
                new BigDecimal(rawAmount.toString()),
                currency,
                customerMsisdn,
                status
        );
    }
}
