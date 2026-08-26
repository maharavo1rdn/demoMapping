package dispatcher;

import adapter.GenericMobileMoneyAdapter;
import model.CanonicalEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class WebhookEventDispatcher {

    private final GenericMobileMoneyAdapter inboundAdapter;
    private final ApplicationEventPublisher eventPublisher;

    public WebhookEventDispatcher(GenericMobileMoneyAdapter inboundAdapter, 
                                  ApplicationEventPublisher eventPublisher) {
        this.inboundAdapter = inboundAdapter;
        this.eventPublisher = eventPublisher;
    }

    public void dispatch(String providerName, String rawPayload) {
        // 1. L'adaptateur génère le bon type d'événement (qui implémente CanonicalEvent)
        CanonicalEvent event = (CanonicalEvent) inboundAdapter.processInbound(providerName, rawPayload);

        // 2. Publication dans le bus Spring. AUCUN IF, AUCUN INSTANCEOF.
        // Spring va automatiquement chercher qui écoute ce type d'objet exact.
        eventPublisher.publishEvent(event);
    }
}