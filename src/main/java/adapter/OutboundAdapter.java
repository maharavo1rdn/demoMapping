package adapter;

import model.canonical.CanonicalSubscription;
import model.canonical.CanonicalTransaction;
import model.Money;

// Si le CBS change un jour, seule l'implementation de cette interface change.
public interface OutboundAdapter {
    void executeTransactionOnCbs(CanonicalTransaction transaction, Money processedMoney);

    void executeSubscriptionOnCbs(CanonicalSubscription subscription);
}
