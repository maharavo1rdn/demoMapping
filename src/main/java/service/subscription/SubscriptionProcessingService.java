package service.subscription;

import adapter.OutboundAdapter;
import model.canonical.CanonicalSubscription;
import service.IdempotencyService;
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

    public void process(CanonicalSubscription subscription) {
        idempotencyService.verifyAndLock(subscription.getExternalRef());
        cbsAdapter.executeSubscriptionOnCbs(subscription);
    }
}
