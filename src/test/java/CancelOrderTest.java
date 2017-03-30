import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CancelOrderTest {

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("coffee-order-api", "localhost", 8080, this);

    @Pact(provider="coffee-order-api", consumer="cancel-order")
    public PactFragment createFragment(PactDslWithProvider builder) {
        return builder
                .given("Order exists")
                .uponReceiving(" request for cancelling order")
                .matchPath("/order/ead9fbfa-4eb3-2249-cd69-104592dff6ab")
                .headers("Accept", "application/json")
                .method("DELETE")
                .willRespondWith()
                .status(200)
                .body("{}")
                .toFragment();
    }

    @Test
    @PactVerification("coffee-order-api")
    public void getOrderedCoffee() throws IOException {
        CoffeeOrderClient coffeeOrderClient = new CoffeeOrderClient("http://localhost:8080");
        String response = coffeeOrderClient.cancelOrder("ead9fbfa-4eb3-2249-cd69-104592dff6ab");
        assertEquals(response, "OK");
    }
}
