package model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import model.Money;
import model.Party;
import model.canonical.interface.CanonicalEvent;

public class CanonicalTransaction implements CanonicalEvent{
    private String internalId;
    private String externalRef;
    private String provider;
    private String eventType;
    private Money money;
    private Party sender;
    private Party recipient;
    private LocalDateTime timestamp;
    private String rawPayload;
    private Map<String, String> metadata = new HashMap<>();

    public CanonicalTransaction() {
    }

    public CanonicalTransaction(String internalId, String externalRef, String provider, String eventType,
            Money money, Party sender, Party recipient, LocalDateTime timestamp, String rawPayload) {
        this.internalId = internalId;
        this.externalRef = externalRef;
        this.provider = provider;
        this.eventType = eventType;
        this.money = money;
        this.sender = sender;
        this.recipient = recipient;
        this.timestamp = timestamp;
        this.rawPayload = rawPayload;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public String getExternalRef() {
        return externalRef;
    }

    public void setExternalRef(String externalRef) {
        this.externalRef = externalRef;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Money getMoney() {
        return money;
    }

    public void setMoney(Money money) {
        this.money = money;
    }

    public Party getSender() {
        return sender;
    }

    public void setSender(Party sender) {
        this.sender = sender;
    }

    public Party getRecipient() {
        return recipient;
    }

    public void setRecipient(Party recipient) {
        this.recipient = recipient;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getRawPayload() {
        return rawPayload;
    }

    public void setRawPayload(String rawPayload) {
        this.rawPayload = rawPayload;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}