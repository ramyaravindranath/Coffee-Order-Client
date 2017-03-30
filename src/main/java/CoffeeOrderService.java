import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;

public interface CoffeeOrderService {
    @POST("/order")
     Call<OrderResponse> createOrder(@Body OrderRequest coffeeOrder);

    @Headers("Accept: application/json")
    @GET("/order/{id}")
    Call<OrderResponse> getOrder(@Path ("id") String id);

    @POST("/order/{id}/coffee")
    Call<CoffeeResponse> selectCoffeeType(@Path("id") String id,@Body CoffeeRequest coffeeRequest);

    @Headers("Accept: application/json")
    @GET("/order/{id}/coffee/{cId}")
    Call<CoffeeResponse> getCoffee(@Path ("id") String id, @Path("cId") String cId);

    @POST("/order/{id}/coffee/{cId}/extra")
    Call<ComponentResponse> selectExtra(@Path("id") String id, @Path("cId") String cId, @Body ComponentRequest componentRequest);

    @Headers("Accept: application/json")
    @DELETE("/order/{id}")
    Call<OrderResponse> cancelOrder(@Path("id") String id);


}
