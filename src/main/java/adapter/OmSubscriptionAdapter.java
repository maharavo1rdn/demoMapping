package adapter;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import model.SubscriptionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Map;

@Component
public class OmSubscriptionAdapter {

    @Value("classpath:mappings/om-subscription.yml")
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

    public SubscriptionRequest parse(String rawJson) {
        DocumentContext jsonContext = JsonPath.parse(rawJson);

        String externalRef = jsonContext.read(fieldsMapping.get("externalRef"));
        String planId = jsonContext.read(fieldsMapping.get("planId"));
        String customerMsisdn = jsonContext.read(fieldsMapping.get("customerMsisdn"));
        String status = jsonContext.read(fieldsMapping.get("status"));
        String nextBillingDate = jsonContext.read(fieldsMapping.get("nextBillingDate"));

        return new SubscriptionRequest(externalRef, planId, customerMsisdn, status, nextBillingDate);
    }
}
