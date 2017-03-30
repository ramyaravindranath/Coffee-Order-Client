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

public class CoffeeTest {
        @Rule
        public PactProviderRule mockProvider = new PactProviderRule("coffee-order-api", "localhost", 8080, this);

        @Pact(provider="coffee-order-api", consumer="select-coffee-type")
        public PactFragment createFragment(PactDslWithProvider builder) {
            return builder
                    .given("Order exists")
                    .uponReceiving(" request for selecting coffee")
                    .matchPath("/order/ead9fbfa-4eb3-2249-cd69-104592dff6ab/coffee")
                    .headers("Content-Type", "application/json")
                    .body("{\"type\": \"Latte\"}")
                    .method("POST")
                    .willRespondWith()
                    .status(200)
                    .body(responseBody())
                    .toFragment();
        }

        private PactDslJsonBody responseBody(){
            PactDslJsonBody responseBody = new PactDslJsonBody();
            responseBody
                    .stringType("id","dcc40c68-4268-657d-0e22-d76c4a9fa6c0");
            return responseBody;
        }

        @Test
        @PactVerification("coffee-order-api")
        public void getOrderedCoffee() throws IOException {
            CoffeeOrderClient coffeeOrderClient = new CoffeeOrderClient("http://localhost:8080");
            CoffeeRequest coffeeRequest = new CoffeeRequest("Latte");
            CoffeeResponse coffeeResponse = coffeeOrderClient.selectCoffee("ead9fbfa-4eb3-2249-cd69-104592dff6ab", coffeeRequest);
            assertThat(coffeeResponse.getId(), is(notNullValue()));
        }
    }


