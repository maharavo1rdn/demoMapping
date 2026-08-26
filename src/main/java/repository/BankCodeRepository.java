package repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BankCodeRepository {
    private final Map<String, String> currencyReferenceTable = new ConcurrentHashMap<>();

    public BankCodeRepository() {
        currencyReferenceTable.put("MGA", "MGA");
        currencyReferenceTable.put("XOF", "XOF");
    }

    public String resolveCbsCurrency(String providerCurrency) {
        if (providerCurrency == null)
            return null;
        return currencyReferenceTable.getOrDefault(providerCurrency, providerCurrency);
    }
}