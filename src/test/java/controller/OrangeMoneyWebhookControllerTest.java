package controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = com.example.DemoMappingApplication.class)
@AutoConfigureMockMvc
public class OrangeMoneyWebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHandleTransactionWebhook() throws Exception {
        String rawJson = """
            {
                "txnid": "TX999",
                "amount": "1000",
                "currency": "XOF",
                "msisdn": "+221770000000",
                "status": "SUCCESS"
            }
            """;

        mockMvc.perform(post("/api/v1/webhooks/orange-money/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rawJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    void testHandleSubscriptionWebhook() throws Exception {
        String rawJson = """
            {
                "sub_id": "SUB111",
                "plan_code": "BASIC",
                "subscriber": "+221770000000",
                "sub_status": "ACTIVE",
                "next_billing": "2026-10-01"
            }
            """;

        mockMvc.perform(post("/api/v1/webhooks/orange-money/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rawJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }
}
