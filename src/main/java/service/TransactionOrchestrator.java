package service;

import adapter.GenericMobileMoneyAdapter;
import adapter.OutboundAdapter;
import model.CanonicalSubscription;
import model.CanonicalTransaction;
import model.Money;
import repository.BankCodeRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionOrchestrator {

    private final IdempotencyService idempotencyService;
    private final FeeEngine feeEngine;
    private final BankCodeRepository bankCodeRepository;
    private final OutboundAdapter cbsAdapter;
    private final GenericMobileMoneyAdapter inboundAdapter;

    public TransactionOrchestrator(IdempotencyService idempotencyService,
            FeeEngine feeEngine,
            BankCodeRepository bankCodeRepository,
            OutboundAdapter cbsAdapter,
            GenericMobileMoneyAdapter inboundAdapter) {
        this.idempotencyService = idempotencyService;
        this.feeEngine = feeEngine;
        this.bankCodeRepository = bankCodeRepository;
        this.cbsAdapter = cbsAdapter;
        this.inboundAdapter = inboundAdapter;
    }

    public void processPayload(String providerName, String rawPayload) {
        Object canonicalModel = inboundAdapter.processInbound(providerName, rawPayload);

        if (canonicalModel instanceof CanonicalTransaction tx) {
            idempotencyService.verifyAndLock(tx.getExternalRef());
            String cbsCurrency = bankCodeRepository.resolveCbsCurrency(tx.getMoney().getCurrency());
            Money normalizedMoney = new Money(tx.getMoney().getAmount(), cbsCurrency);
            Money finalMoney = feeEngine.applyTransactionFees(normalizedMoney);

            cbsAdapter.executeTransactionOnCbs(tx, finalMoney);

        } else if (canonicalModel instanceof CanonicalSubscription sub) {
            idempotencyService.verifyAndLock(sub.getExternalRef());
            cbsAdapter.executeSubscriptionOnCbs(sub);

        } else {
            throw new IllegalArgumentException("Modèle canonique non pris en charge");
        }
    }
}