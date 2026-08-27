package repository;

import model.RawInboundMessage;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Simule une table SQL :
//
// CREATE TABLE raw_inbound_messages (
//   id                VARCHAR PRIMARY KEY,
//   provider          VARCHAR NOT NULL,
//   raw_json_payload  TEXT NOT NULL,
//   received_at       TIMESTAMP NOT NULL,
//   status            VARCHAR NOT NULL,   -- PENDING, PROCESSED, FAILED
//   failure_reason    VARCHAR
// );
//
// Remplacer ConcurrentHashMap par un vrai JpaRepository/JdbcTemplate
// le jour ou on branche une vraie base de donnees : le reste du code
// (WebhookEventDispatcher) n'aura rien a changer.
@Repository
public class RawPayloadRepository {
    private final Map<String, RawInboundMessage> store = new ConcurrentHashMap<>();

    public void save(RawInboundMessage message) {
        store.put(message.getId(), message);
    }

    public void markProcessed(String id) {
        store.get(id).setStatus(RawInboundMessage.Status.PROCESSED);
    }

    public void markFailed(String id, String reason) {
        RawInboundMessage message = store.get(id);
        message.setStatus(RawInboundMessage.Status.FAILED);
        message.setFailureReason(reason);
    }

    public RawInboundMessage findById(String id) {
        return store.get(id);
    }
}
