package adapter;

import model.CanonicalSubscription;
import model.CanonicalTransaction;
import model.Money;
import org.springframework.stereotype.Component;

@Component
public class CbsAdapter implements OutboundAdapter {

    @Override
    public void executeTransactionOnCbs(CanonicalTransaction transaction, Money processedMoney) {
        System.out.println("====== [CBS OUTBOUND - TRANSACTION] ======");
        System.out.println("Opérateur           : " + transaction.getProvider());
        System.out.println("Référence Externe  : " + transaction.getExternalRef());
        System.out.println("Compte Débiteur    : " + transaction.getSender().getValue());
        System.out.println("Compte Créditeur   : " + transaction.getRecipient().getValue());
        System.out.println("Montant Traité     : " + processedMoney.getAmount() + " " + processedMoney.getCurrency());
        System.out.println("Réponse CBS        : 200 OK (Posté avec succès dans le Grand Livre)\n");
    }

    @Override
    public void executeSubscriptionOnCbs(CanonicalSubscription subscription) {
        System.out.println("====== [CBS OUTBOUND - SOUSCRIPTION] ======");
        System.out.println("Opérateur           : " + subscription.getProvider());
        System.out.println("Référence Externe  : " + subscription.getExternalRef());
        System.out.println("Client MSISDN      : " + subscription.getCustomer().getValue());
        System.out.println("Compte CBS Associé : " + subscription.getBankAccount().getValue());
        System.out.println("Réponse CBS        : 200 OK (Mandat de liaison enregistré)\n");
    }
}