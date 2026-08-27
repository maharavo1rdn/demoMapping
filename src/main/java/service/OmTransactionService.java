package service;

import adapter.OmTransactionAdapter;
import model.TransactionRequest;
import org.springframework.stereotype.Service;

@Service
public class OmTransactionService {

    private final OmTransactionAdapter adapter;

    public OmTransactionService(OmTransactionAdapter adapter) {
        this.adapter = adapter;
    }

    public void processTransaction(String rawPayload) {
        TransactionRequest transaction = adapter.parse(rawPayload);

        System.out.println("--- NOUVELLE TRANSACTION ---");
        System.out.println("Ref Externe : " + transaction.getExternalRef());
        System.out.println("Montant : " + transaction.getAmount() + " " + transaction.getCurrency());
        System.out.println("Statut : " + transaction.getStatus());

        // TODO: Logique métier d'enregistrement en BDD ou notification
    }
}
