package service;

import adapter.InboundAdapter;
import model.RawInboundMessage;
import model.canonical.CanonicalEvent;
import model.canonical.CanonicalSubscription;
import model.canonical.CanonicalTransaction;
import repository.RawPayloadRepository;
import service.subscription.SubscriptionProcessingService;
import service.transaction.TransactionProcessingService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrangeMoneyProcessingService {

    private static final String PROVIDER = "orangemoney";

    private final InboundAdapter inboundAdapter;
    private final RawPayloadRepository rawPayloadRepository;
    private final TransactionProcessingService transactionProcessingService;
    private final SubscriptionProcessingService subscriptionProcessingService;

    public OrangeMoneyProcessingService(InboundAdapter inboundAdapter,
                                        RawPayloadRepository rawPayloadRepository,
                                        TransactionProcessingService transactionProcessingService,
                                        SubscriptionProcessingService subscriptionProcessingService) {
        this.inboundAdapter = inboundAdapter;
        this.rawPayloadRepository = rawPayloadRepository;
        this.transactionProcessingService = transactionProcessingService;
        this.subscriptionProcessingService = subscriptionProcessingService;
    }

    public void processNotification(String rawPayload) {
        String rawId = "RAW-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        RawInboundMessage rawMessage = new RawInboundMessage(rawId, PROVIDER, rawPayload);
        rawPayloadRepository.save(rawMessage);

        try {
            CanonicalEvent event = inboundAdapter.processInbound(rawPayload);

            if (event instanceof CanonicalTransaction transaction) {
                transactionProcessingService.process(transaction);
            } else if (event instanceof CanonicalSubscription subscription) {
                subscriptionProcessingService.process(subscription);
            } else {
                throw new IllegalArgumentException("Type d'événement non pris en charge: " +
                        (event != null ? event.getClass().getName() : "null"));
            }

            rawPayloadRepository.markProcessed(rawId);
        } catch (Exception e) {
            rawPayloadRepository.markFailed(rawId, e.getMessage());
            throw e;
        }
    }
}
