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
import static org.junit.Assert.*;


public class OrderedCoffeeTest {
    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("coffee-order-api", "localhost", 8080, this);

    @Pact(provider="coffee-order-api", consumer="get-coffee-order-sdk")
    public PactFragment createFragment(PactDslWithProvider builder) {
        return builder
                .given("Order exists")
                .uponReceiving(" request for coffee order")
                .matchPath("/order/ead9fbfa-4eb3-2249-cd69-104592dff6ab")
                .headers("Accept", "application/json")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(responseBody())
                .toFragment();
    }

    private PactDslJsonBody responseBody(){
        PactDslJsonBody responseBody = new PactDslJsonBody();
        responseBody
                .stringMatcher("id","[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", "ead9fbfa-4eb3-2249-cd69-104592dff6ab")
               .array("coffeeList").closeArray();
        return responseBody;
    }

    @Test
    @PactVerification("coffee-order-api")
    public void getOrderedCoffee() throws IOException {
        CoffeeOrderClient coffeeOrderClient = new CoffeeOrderClient("http://localhost:8080");
        OrderResponse orderResponse = coffeeOrderClient.getSingleOrder("ead9fbfa-4eb3-2249-cd69-104592dff6ab");
        assertThat(orderResponse.getId(), is(notNullValue()));
    }
}
