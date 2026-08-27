package dispatcher;

import adapter.InboundAdapter;
import model.RawInboundMessage;
import model.canonical.CanonicalEvent;
import repository.RawPayloadRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

// Point d'entree unique pour tout message Orange Money, quel que soit
// son type (transaction, souscription...).
@Service
public class WebhookEventDispatcher {

    private static final String PROVIDER = "orangemoney";

    private final InboundAdapter inboundAdapter;
    private final ApplicationEventPublisher eventPublisher;
    private final RawPayloadRepository rawPayloadRepository;

    public WebhookEventDispatcher(InboundAdapter inboundAdapter,
                                  ApplicationEventPublisher eventPublisher,
                                  RawPayloadRepository rawPayloadRepository) {
        this.inboundAdapter = inboundAdapter;
        this.eventPublisher = eventPublisher;
        this.rawPayloadRepository = rawPayloadRepository;
    }

    public void dispatch(String rawPayload) {
        // ETAPE 0 (NOUVEAU) : on enregistre le payload brut EN PREMIER, avant
        // toute transformation. Meme si le mapping ou l'envoi au CBS echoue
        // plus loin, cette ligne existe deja en base -- rien n'est jamais perdu,
        // et on peut la rejouer plus tard si un bug de mapping est corrige.
        String rawId = "RAW-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        RawInboundMessage rawMessage = new RawInboundMessage(rawId, PROVIDER, rawPayload);
        rawPayloadRepository.save(rawMessage);

        try {
            // 1. L'adaptateur devine le type d'evenement et produit l'objet canonique correspondant
            CanonicalEvent event = inboundAdapter.processInbound(rawPayload);

            // 2. Publication dans le bus Spring. Aucun if, aucun instanceof :
            // Spring route automatiquement vers qui ecoute ce type d'objet exact.
            eventPublisher.publishEvent(event);

            rawPayloadRepository.markProcessed(rawId);
        } catch (Exception e) {
            rawPayloadRepository.markFailed(rawId, e.getMessage());
            throw e;
        }
    }
}
