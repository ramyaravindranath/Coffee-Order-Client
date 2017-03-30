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
import static org.junit.Assert.assertThat;

public class SelectComponentTest {

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("coffee-order-api", "localhost", 8080, this);

    @Pact(provider="coffee-order-api", consumer="select-component-type")
    public PactFragment createFragment(PactDslWithProvider builder) {
        return builder
                .given("Order and Coffee exists")
                .uponReceiving(" request for adding extras for coffee")
                .matchPath("/order/ead9fbfa-4eb3-2249-cd69-104592dff6ab/coffee/dcc40c68-4268-657d-0e22-d76c4a9fa6c0/extra")
                .headers("Content-Type", "application/json")
                .body("{\"name\": \"soy-milk\"}")
                .method("POST")
                .willRespondWith()
                .status(200)
                .body(responseBody())
                .toFragment();
    }

    private PactDslJsonBody responseBody(){
        PactDslJsonBody responseBody = new PactDslJsonBody();
        responseBody
                .stringType("id","ea96b68a-6f67-4640-abe8-033e30cda0e3");
        return responseBody;
    }

    @Test
    @PactVerification("coffee-order-api")
    public void getOrderedCoffee() throws IOException {
        CoffeeOrderClient coffeeOrderClient = new CoffeeOrderClient("http://localhost:8080");
        ComponentRequest componentRequest = new ComponentRequest("soy-milk");
        ComponentResponse componentResponse = coffeeOrderClient.selectExtra("ead9fbfa-4eb3-2249-cd69-104592dff6ab", "dcc40c68-4268-657d-0e22-d76c4a9fa6c0", componentRequest);
        assertThat(componentResponse.getId(), is(notNullValue()));
    }
}
