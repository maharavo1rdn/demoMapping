package model;

public class SubscriptionRequest {
    private String externalRef;
    private String planId;
    private String customerMsisdn;
    private String status;
    private String nextBillingDate;

    public SubscriptionRequest(String externalRef, String planId, String customerMsisdn, String status, String nextBillingDate) {
        this.externalRef = externalRef;
        this.planId = planId;
        this.customerMsisdn = customerMsisdn;
        this.status = status;
        this.nextBillingDate = nextBillingDate;
    }

    public String getExternalRef() { return externalRef; }
    public String getPlanId() { return planId; }
    public String getCustomerMsisdn() { return customerMsisdn; }
    public String getStatus() { return status; }
    public String getNextBillingDate() { return nextBillingDate; }
}
