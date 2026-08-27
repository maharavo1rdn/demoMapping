package adapter;

import model.TransactionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = com.example.DemoMappingApplication.class)
public class OmTransactionAdapterTest {

    @Autowired
    private OmTransactionAdapter adapter;

    @Test
    void testParseTransaction() {
        String rawJson = """
            {
                "txnid": "TX123456",
                "amount": "5000.50",
                "currency": "XOF",
                "msisdn": "+221770000000",
                "status": "SUCCESS"
            }
            """;

        TransactionRequest request = adapter.parse(rawJson);

        assertNotNull(request);
        assertEquals("TX123456", request.getExternalRef());
        assertEquals(new BigDecimal("5000.50"), request.getAmount());
        assertEquals("XOF", request.getCurrency());
        assertEquals("+221770000000", request.getCustomerMsisdn());
        assertEquals("SUCCESS", request.getStatus());
    }
}
