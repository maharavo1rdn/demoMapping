package model;

import java.math.BigDecimal;

public class TransactionRequest {
    private String externalRef;
    private BigDecimal amount;
    private String currency;
    private String customerMsisdn;
    private String status;

    public TransactionRequest(String externalRef, BigDecimal amount, String currency, String customerMsisdn, String status) {
        this.externalRef = externalRef;
        this.amount = amount;
        this.currency = currency;
        this.customerMsisdn = customerMsisdn;
        this.status = status;
    }

    public String getExternalRef() { return externalRef; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getCustomerMsisdn() { return customerMsisdn; }
    public String getStatus() { return status; }
}
