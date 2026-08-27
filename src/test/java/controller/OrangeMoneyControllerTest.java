package controller;

import demo.DemoMappingApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DemoMappingApplication.class)
@AutoConfigureMockMvc
class OrangeMoneyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testReceiveNotificationTransactionSuccess() throws Exception {
        String payload = """
            {
              "transaction_id": "OM-TX-3001",
              "amount": "10000",
              "currency": "XOF",
              "sender_phone": "2250700000000",
              "receiver_iban": "CI93CI0080123456789012345678"
            }
            """;

        mockMvc.perform(post("/api/orangemoney/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().string("Notification recue et traitee."));
    }

    @Test
    void testReceiveNotificationSubscriptionSuccess() throws Exception {
        String payload = """
            {
              "subscription_id": "OM-SUB-3001",
              "customer_msisdn": "2250700000000",
              "bank_account_number": "CI93CI0080123456789012345678"
            }
            """;

        mockMvc.perform(post("/api/orangemoney/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().string("Notification recue et traitee."));
    }
}
