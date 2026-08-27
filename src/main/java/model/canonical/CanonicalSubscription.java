package model.canonical;

import model.Party;

import java.time.LocalDateTime;

// Le format pivot pour une souscription (liaison d'un numero Orange Money
// a un compte bancaire). Different d'une transaction : pas de montant,
// mais un client + un compte associe.
public class CanonicalSubscription implements CanonicalEvent {
    private String internalId;
    private String externalRef;
    private String provider;
    private Party customer;
    private Party bankAccount;
    private LocalDateTime timestamp;

    public CanonicalSubscription() {}

    public CanonicalSubscription(String internalId, String externalRef, String provider,
                                 Party customer, Party bankAccount, LocalDateTime timestamp) {
        this.internalId = internalId;
        this.externalRef = externalRef;
        this.provider = provider;
        this.customer = customer;
        this.bankAccount = bankAccount;
        this.timestamp = timestamp;
    }

    public String getInternalId() { return internalId; }
    public void setInternalId(String internalId) { this.internalId = internalId; }

    @Override
    public String getExternalRef() { return externalRef; }
    public void setExternalRef(String externalRef) { this.externalRef = externalRef; }

    @Override
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public Party getCustomer() { return customer; }
    public void setCustomer(Party customer) { this.customer = customer; }

    public Party getBankAccount() { return bankAccount; }
    public void setBankAccount(Party bankAccount) { this.bankAccount = bankAccount; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
