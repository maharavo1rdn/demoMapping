package service;

import adapter.OrangeMoneyInboundAdapter;
import mapping.config.MappingConfigRegistry;
import mapping.strategy.MappingStrategyRegistry;
import mapping.strategy.SubscriptionMappingStrategy;
import mapping.strategy.TransactionMappingStrategy;
import repository.BankCodeRepository;
import repository.RawPayloadRepository;
import service.subscription.SubscriptionProcessingService;
import service.transaction.TransactionProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrangeMoneyProcessingServiceTest {

    private OrangeMoneyProcessingService processingService;

    @BeforeEach
    void setUp() {
        MappingConfigRegistry configRegistry = new MappingConfigRegistry();
        MappingStrategyRegistry strategyRegistry = new MappingStrategyRegistry(
                java.util.List.of(new TransactionMappingStrategy(), new SubscriptionMappingStrategy())
        );
        OrangeMoneyInboundAdapter adapter = new OrangeMoneyInboundAdapter(configRegistry, strategyRegistry);
        RawPayloadRepository rawPayloadRepository = new RawPayloadRepository();
        IdempotencyService idempotencyService = new IdempotencyService();
        FeeEngine feeEngine = new FeeEngine();
        BankCodeRepository bankCodeRepository = new BankCodeRepository();
        adapter.CbsAdapter cbsAdapter = new adapter.CbsAdapter();

        TransactionProcessingService transactionProcessingService = new TransactionProcessingService(
                idempotencyService, feeEngine, bankCodeRepository, cbsAdapter
        );
        SubscriptionProcessingService subscriptionProcessingService = new SubscriptionProcessingService(
                idempotencyService, cbsAdapter
        );

        processingService = new OrangeMoneyProcessingService(
                adapter,
                rawPayloadRepository,
                transactionProcessingService,
                subscriptionProcessingService
        );
    }

    @Test
    void testProcessTransactionSuccessfully() {
        String transactionPayload = """
            {
              "transaction_id": "OM-TX-1001",
              "amount": "5000",
              "currency": "XOF",
              "sender_phone": "2250700000000",
              "receiver_iban": "CI93CI0080123456789012345678"
            }
            """;

        assertDoesNotThrow(() -> processingService.processNotification(transactionPayload));
    }

    @Test
    void testProcessSubscriptionSuccessfully() {
        String subscriptionPayload = """
            {
              "subscription_id": "OM-SUB-2001",
              "customer_msisdn": "2250700000000",
              "bank_account_number": "CI93CI0080123456789012345678"
            }
            """;

        assertDoesNotThrow(() -> processingService.processNotification(subscriptionPayload));
    }

    @Test
    void testDuplicateTransactionThrowsException() {
        String transactionPayload = """
            {
              "transaction_id": "OM-TX-1002",
              "amount": "5000",
              "currency": "XOF",
              "sender_phone": "2250700000000",
              "receiver_iban": "CI93CI0080123456789012345678"
            }
            """;

        processingService.processNotification(transactionPayload);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                processingService.processNotification(transactionPayload)
        );

        assertTrue(exception.getMessage().contains("Doublon detecte"));
    }

    @Test
    void testUnknownPayloadThrowsException() {
        String unknownPayload = """
            {
              "unknown_id": "999"
            }
            """;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                processingService.processNotification(unknownPayload)
        );

        assertTrue(exception.getMessage().contains("Aucun schema de mapping Orange Money ne correspond au payload recu."));
    }
}
