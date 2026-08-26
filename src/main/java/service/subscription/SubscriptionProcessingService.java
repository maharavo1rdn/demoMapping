package service.subscription;

import adapter.OutboundAdapter;
import model.CanonicalSubscription;
import service.IdempotencyService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionProcessingService {

    private final IdempotencyService idempotencyService;
    private final OutboundAdapter cbsAdapter;

    public SubscriptionProcessingService(IdempotencyService idempotencyService, 
                                         OutboundAdapter cbsAdapter) {
        this.idempotencyService = idempotencyService;
        this.cbsAdapter = cbsAdapter;
    }

    @EventListener
    public void process(CanonicalSubscription subscription) {
        idempotencyService.verifyAndLock(subscription.getExternalRef());
        cbsAdapter.executeSubscriptionOnCbs(subscription);
    }
}