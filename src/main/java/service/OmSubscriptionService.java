package service;

import adapter.OmSubscriptionAdapter;
import model.SubscriptionRequest;
import org.springframework.stereotype.Service;

@Service
public class OmSubscriptionService {

    private final OmSubscriptionAdapter adapter;

    public OmSubscriptionService(OmSubscriptionAdapter adapter) {
        this.adapter = adapter;
    }

    public void processSubscription(String rawPayload) {
        SubscriptionRequest subscription = adapter.parse(rawPayload);

        System.out.println("--- NOUVELLE SOUSCRIPTION ---");
        System.out.println("Ref Externe : " + subscription.getExternalRef());
        System.out.println("Plan ID : " + subscription.getPlanId());
        System.out.println("Statut : " + subscription.getStatus());

        // TODO: Logique métier d'enregistrement de l'abonnement
    }
}
