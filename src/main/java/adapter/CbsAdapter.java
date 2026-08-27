package adapter;

import model.canonical.CanonicalSubscription;
import model.canonical.CanonicalTransaction;
import model.Money;
import org.springframework.stereotype.Component;

@Component
public class CbsAdapter implements OutboundAdapter {

    @Override
    public void executeTransactionOnCbs(CanonicalTransaction transaction, Money processedMoney) {
        System.out.println("====== [CBS OUTBOUND - TRANSACTION] ======");
        System.out.println("Operateur           : " + transaction.getProvider());
        System.out.println("Reference Externe   : " + transaction.getExternalRef());
        System.out.println("Compte Debiteur     : " + transaction.getSender().getValue());
        System.out.println("Compte Crediteur    : " + transaction.getRecipient().getValue());
        System.out.println("Montant Traite      : " + processedMoney.getAmount() + " " + processedMoney.getCurrency());
        System.out.println("Reponse CBS         : 200 OK (Poste avec succes dans le Grand Livre)\n");
    }

    @Override
    public void executeSubscriptionOnCbs(CanonicalSubscription subscription) {
        System.out.println("====== [CBS OUTBOUND - SOUSCRIPTION] ======");
        System.out.println("Operateur           : " + subscription.getProvider());
        System.out.println("Reference Externe   : " + subscription.getExternalRef());
        System.out.println("Client MSISDN       : " + subscription.getCustomer().getValue());
        System.out.println("Compte CBS Associe  : " + subscription.getBankAccount().getValue());
        System.out.println("Reponse CBS         : 200 OK (Mandat de liaison enregistre)\n");
    }
}
