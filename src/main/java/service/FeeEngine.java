package service;

import model.Money;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

// Regle metier : reste en code, jamais en config ni en base.
@Service
public class FeeEngine {

    public Money applyTransactionFees(Money money) {
        if (money == null)
            return null;
        BigDecimal amountWithFees = money.getAmount().add(new BigDecimal("100.00"));
        return new Money(amountWithFees, money.getCurrency());
    }
}
