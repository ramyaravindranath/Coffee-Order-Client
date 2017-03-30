import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class CoffeeOrderClient {


    private final String baseUrl;


    public CoffeeOrderClient(String baseUrl){
        this.baseUrl = baseUrl;
    }

        public OrderResponse fulfill(OrderRequest coffeeOrder) throws IOException {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            CoffeeOrderService coffeeOrderService = retrofit.create(CoffeeOrderService.class);
            Call<OrderResponse> call = coffeeOrderService.createOrder(coffeeOrder);
            return call.execute().body();
        }

    public OrderResponse getSingleOrder(String id) throws IOException {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        CoffeeOrderService coffeeOrderService = retrofit.create(CoffeeOrderService.class);
        Call<OrderResponse> call = coffeeOrderService.getOrder(id);
        return call.execute().body();

    }

    public CoffeeResponse selectCoffee(String id,CoffeeRequest coffeeRequest) throws IOException{

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CoffeeOrderService coffeeOrderService = retrofit.create(CoffeeOrderService.class);
        Call<CoffeeResponse> call = coffeeOrderService.selectCoffeeType(id,coffeeRequest);
        return call.execute().body();
    }


    public CoffeeResponse selectOrderedCoffee(String id, String cId) throws IOException {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        CoffeeOrderService coffeeOrderService = retrofit.create(CoffeeOrderService.class);
        Call<CoffeeResponse> call = coffeeOrderService.getCoffee(id,cId);
        return call.execute().body();


    }



    public ComponentResponse selectExtra(String id, String cId, ComponentRequest componentRequest) throws IOException{

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CoffeeOrderService coffeeOrderService = retrofit.create(CoffeeOrderService.class);
        Call<ComponentResponse> call = coffeeOrderService.selectExtra(id, cId, componentRequest);
        return call.execute().body();
    }

    public String cancelOrder(String id) throws IOException{

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CoffeeOrderService coffeeOrderService = retrofit.create(CoffeeOrderService.class);
        Call<OrderResponse> call = coffeeOrderService.cancelOrder(id);
        return call.execute().message();


    }

}
