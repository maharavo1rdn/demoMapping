package adapter;

import model.CanonicalSubscription;
import model.CanonicalTransaction;
import model.Money;

public interface OutboundAdapter {
    void executeTransactionOnCbs(CanonicalTransaction transaction, Money processedMoney);

    void executeSubscriptionOnCbs(CanonicalSubscription subscription);
}