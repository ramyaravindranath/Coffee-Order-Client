import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SelectedCoffeeTest {

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("coffee-order-api", "localhost", 8080, this);

    @Pact(provider = "coffee-order-api", consumer = "get-ordered-coffee-order-sdk")
    public PactFragment createFragment(PactDslWithProvider builder) {
        return builder
                .given("coffee exists")
                .uponReceiving(" request for coffee order")
                .matchPath("/order/ead9fbfa-4eb3-2249-cd69-104592dff6ab/coffee/dcc40c68-4268-657d-0e22-d76c4a9fa6c0")
                .headers("Accept", "application/json")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(responseBody())
                .toFragment();
    }

    private PactDslJsonBody responseBody() {
        PactDslJsonBody responseBody = new PactDslJsonBody();
        responseBody
                .stringMatcher("id", "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", "dcc40c68-4268-657d-0e22-d76c4a9fa6c0")
                .array("extraComponents").closeArray();
        return responseBody;
    }

    @Test
    @PactVerification("coffee-order-api")
    public void getOrderedCoffee() throws IOException {
        CoffeeOrderClient coffeeOrderClient = new CoffeeOrderClient("http://localhost:8080");
        CoffeeResponse coffeeResponse = coffeeOrderClient.selectOrderedCoffee("ead9fbfa-4eb3-2249-cd69-104592dff6ab", "dcc40c68-4268-657d-0e22-d76c4a9fa6c0");
        assertEquals(coffeeResponse.getId(), "dcc40c68-4268-657d-0e22-d76c4a9fa6c0");
    }
}
