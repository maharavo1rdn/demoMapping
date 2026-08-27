package service.transaction;

import adapter.OutboundAdapter;
import model.canonical.CanonicalTransaction;
import model.Money;
import repository.BankCodeRepository;
import service.FeeEngine;
import service.IdempotencyService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionProcessingService {

    private final IdempotencyService idempotencyService;
    private final FeeEngine feeEngine;
    private final BankCodeRepository bankCodeRepository;
    private final OutboundAdapter cbsAdapter;

    public TransactionProcessingService(IdempotencyService idempotencyService,
            FeeEngine feeEngine,
            BankCodeRepository bankCodeRepository,
            OutboundAdapter cbsAdapter) {
        this.idempotencyService = idempotencyService;
        this.feeEngine = feeEngine;
        this.bankCodeRepository = bankCodeRepository;
        this.cbsAdapter = cbsAdapter;
    }

    // Spring route AUTOMATIQUEMENT ici si l'evenement publie est une CanonicalTransaction
    @EventListener
    public void process(CanonicalTransaction transaction) {
        idempotencyService.verifyAndLock(transaction.getExternalRef());

        String cbsCurrency = bankCodeRepository.resolveCbsCurrency(transaction.getMoney().getCurrency());
        Money normalizedMoney = new Money(transaction.getMoney().getAmount(), cbsCurrency);
        Money finalMoney = feeEngine.applyTransactionFees(normalizedMoney);

        cbsAdapter.executeTransactionOnCbs(transaction, finalMoney);
    }
}
