package adapter;

import model.SubscriptionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = com.example.DemoMappingApplication.class)
public class OmSubscriptionAdapterTest {

    @Autowired
    private OmSubscriptionAdapter adapter;

    @Test
    void testParseSubscription() {
        String rawJson = """
            {
                "sub_id": "SUB98765",
                "plan_code": "MONTHLY_PREMIUM",
                "subscriber": "+221771112233",
                "sub_status": "ACTIVE",
                "next_billing": "2026-09-01"
            }
            """;

        SubscriptionRequest request = adapter.parse(rawJson);

        assertNotNull(request);
        assertEquals("SUB98765", request.getExternalRef());
        assertEquals("MONTHLY_PREMIUM", request.getPlanId());
        assertEquals("+221771112233", request.getCustomerMsisdn());
        assertEquals("ACTIVE", request.getStatus());
        assertEquals("2026-09-01", request.getNextBillingDate());
    }
}
