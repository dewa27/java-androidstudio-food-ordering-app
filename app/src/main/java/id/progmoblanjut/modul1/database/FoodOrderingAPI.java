package id.progmoblanjut.modul1.database;

import java.util.ArrayList;

import id.progmoblanjut.modul1.model.CartModel;
import id.progmoblanjut.modul1.model.FoodModel;
import id.progmoblanjut.modul1.model.PostModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FoodOrderingAPI {
    @GET("foods")
    Call<ArrayList<FoodModel>> getFood();

    @GET("posts")
    Call<ArrayList<PostModel>> getPosts();

    @GET("foods/{id}")
    Call<ArrayList<FoodModel>> getSingleFood(@Path("id") int id_food);

    @GET("foods/{food_name}")
    Call<ArrayList<FoodModel>> getFoodByName(@Path("food_name") String food_name);

    @GET("foods/category/{category}/{food_name}")
    Call<ArrayList<FoodModel>> getFoodByNameAndCategory(@Path("category") String category,@Path("food_name") String food_name);

    @GET("foods/category/{category}")
    Call<ArrayList<FoodModel>> getFoodByCategory(@Path("category") String category);

    @FormUrlEncoded
    @POST("customers/create")
    public Call<ResponseBody> createUser(@Field("customer_name") String customer_name,
                                         @Field("customer_qty") int customer_qty,
                                         @Field("status_makan") String status_makan,
                                         @Field("rekomendasi") String rekomendasi);

    @FormUrlEncoded
    @POST("payment/create")
    public Call<ResponseBody> createPayment(@Field("code") String code,
                                            @Field("total") Double total,
                                            @Field("uuid") String uuid,
                                            @Field("cartData") String cartData);

    @FormUrlEncoded
    @POST("payment/check")
    Call<Result> checkPayment(@Field("code") String code);
}
