package model;

import java.time.LocalDateTime;

// Une ligne de la future table "raw_inbound_messages".
// Sauvegarde AVANT toute transformation : meme si le mapping ou l'envoi
// au CBS echoue, on garde une preuve exacte de ce qu'Orange Money a envoye,
// rejouable et exploitable pour l'audit.
public class RawInboundMessage {
    public enum Status { PENDING, PROCESSED, FAILED }

    private final String id;
    private final String provider;
    private final String rawJsonPayload;
    private final LocalDateTime receivedAt;
    private Status status;
    private String failureReason;

    public RawInboundMessage(String id, String provider, String rawJsonPayload) {
        this.id = id;
        this.provider = provider;
        this.rawJsonPayload = rawJsonPayload;
        this.receivedAt = LocalDateTime.now();
        this.status = Status.PENDING;
    }

    public String getId() { return id; }
    public String getProvider() { return provider; }
    public String getRawJsonPayload() { return rawJsonPayload; }
    public LocalDateTime getReceivedAt() { return receivedAt; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
}
