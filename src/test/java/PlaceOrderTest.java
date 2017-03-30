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

public class PlaceOrderTest {

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("coffee-order-api", "localhost", 8080, this);

    @Pact(provider="coffee-order-api", consumer="coffee-order-sdk")
    public PactFragment createFragment(PactDslWithProvider builder) {
        return builder
                .uponReceiving("coffee order")
                .path("/order")
                .matchHeader("Content-Type", "application/json; charset=UTF-8")
                .body("{\"customerName\": \"ramya\"}")
                .method("POST")
                .willRespondWith()
                .status(200)
                .body(responseBody())
                .toFragment();
    }

    private PactDslJsonBody responseBody(){
      return new PactDslJsonBody()
              .stringType("id","ead9fbfa-4eb3-2249-cd69-104592dff6ab");
    }


    @Test
    @PactVerification("coffee-order-api")
    public void orderSingleCoffee() throws IOException {
        CoffeeOrderClient coffeeOrderClient = new CoffeeOrderClient("http://localhost:8080");
        OrderRequest order = new OrderRequest("ramya");
        OrderResponse orderResponse = coffeeOrderClient.fulfill(order);
        assertThat(orderResponse.getId(), is(notNullValue()));
    }
}