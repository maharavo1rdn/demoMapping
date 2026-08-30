package gateway.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.mKajy.gateway.common.YamlPayloadBuilder;
import com.project.mKajy.model.OmKycSubscriptionRequest;

import gateway.dto.CbsSubscriptionResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "cbs.subscription.provider", havingValue = "Aspekt", matchIfMissing = true)
public class AspektSubscriptionGateway implements CbsSubscriptionGateway {

    private static final Logger log = LoggerFactory.getLogger(AspektSubscriptionGateway.class);

    private final YamlPayloadBuilder payloadBuilder;
    private final RestTemplate restTemplate;

    @Value("${cbs.base-url}")
    private String cbsBaseUrl;

    @Value("${cbs.subscription.endpoint}")
    private String subscriptionEndpoint;

    @Value("${cbs.subscription.mapping-file}")
    private String mappingFile;

    @Value("${cbs.auth.api-key:}")
    private String apiKey;

    public AspektSubscriptionGateway(YamlPayloadBuilder payloadBuilder, RestTemplate restTemplate) {
        this.payloadBuilder = payloadBuilder;
        this.restTemplate = restTemplate;
    }

    @Override
    public CbsSubscriptionResult sendSubscription(OmKycSubscriptionRequest kyc) {
        ObjectNode body;
        try {
            body = payloadBuilder.build(mappingFile, toSourceMap(kyc));
        } catch (IllegalStateException e) {
            log.error("Mapping CBS invalide pour la souscription {}", kyc.externalId(), e);
            return CbsSubscriptionResult.failure("Erreur de configuration du mapping CBS");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (!apiKey.isBlank()) {
            headers.set("X-API-KEY", apiKey);
        }

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    cbsBaseUrl + subscriptionEndpoint,
                    new HttpEntity<>(body, headers),
                    String.class
            );
            return new CbsSubscriptionResult(
                    response.getStatusCode().value(),
                    response.getBody(),
                    response.getStatusCode().is2xxSuccessful()
            );

        } catch (RestClientException e) {
            // Ne jamais logger apiKey ni le body complet (données personnelles).
            log.error("Échec d'appel CBS pour la souscription {} : {}", kyc.externalId(), e.getMessage());
            return CbsSubscriptionResult.failure("Erreur réseau/CBS : " + e.getMessage());
        }
    }

    private Map<String, String> toSourceMap(OmKycSubscriptionRequest kyc) {
        Map<String, String> map = new HashMap<>();
        map.put("externalId", kyc.externalId());
        map.put("userId", kyc.userId());
        map.put("familyName", kyc.familyName());
        map.put("givenName", kyc.givenName());
        map.put("birthdate", kyc.birthdate());
        map.put("identificationId", kyc.identificationId());
        map.put("idType", kyc.idType());
        map.put("title", kyc.title());
        map.put("issuingDate", kyc.issuingDate());
        map.put("street1", kyc.street1());
        map.put("stateOrProvince", kyc.stateOrProvince());
        map.put("postCode", kyc.postCode());
        map.put("city", kyc.city());
        map.put("frontScanUrl", kyc.frontScanUrl());
        map.put("frontScanType", kyc.frontScanType());
        map.put("backScanUrl", kyc.backScanUrl());
        map.put("backScanType", kyc.backScanType());
        return map;
    }
}