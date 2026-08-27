package service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IdempotencyService {
    private final Set<String> processedKeys = ConcurrentHashMap.newKeySet();

    public void verifyAndLock(String externalRef) {
        if (processedKeys.contains(externalRef)) {
            throw new IllegalStateException("Doublon detecte : Reference " + externalRef + " deja traitee.");
        }
        processedKeys.add(externalRef);
    }
}
