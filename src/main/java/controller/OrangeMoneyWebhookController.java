package controller;

import service.OmTransactionService;
import service.OmSubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/webhooks/orange-money")
public class OrangeMoneyWebhookController {

    private final OmTransactionService transactionService;
    private final OmSubscriptionService subscriptionService;

    public OrangeMoneyWebhookController(OmTransactionService transactionService,
                                        OmSubscriptionService subscriptionService) {
        this.transactionService = transactionService;
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/transactions")
    public ResponseEntity<Map<String, String>> handleTransaction(@RequestBody String rawPayload) {
        transactionService.processTransaction(rawPayload);
        return ResponseEntity.ok(Map.of("status", "SUCCESS"));
    }

    @PostMapping("/subscriptions")
    public ResponseEntity<Map<String, String>> handleSubscription(@RequestBody String rawPayload) {
        subscriptionService.processSubscription(rawPayload);
        return ResponseEntity.ok(Map.of("status", "SUCCESS"));
    }
}
