package id.progmoblanjut.modul1;

import java.util.ArrayList;

import id.progmoblanjut.modul1.model.FoodModel;
import id.progmoblanjut.modul1.model.PostModel;
import retrofit2.Call;
import retrofit2.http.GET;
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
}
