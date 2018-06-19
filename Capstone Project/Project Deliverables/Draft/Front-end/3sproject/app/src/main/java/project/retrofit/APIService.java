package project.retrofit;

import java.util.List;

import project.objects.User;
import project.view.AddProductToStore.Item;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {
    @POST("login")
    @FormUrlEncoded
    Call<User> savePost(@Field("username") String username,
                        @Field("password") String password);
    @GET("getProductForAdd")
    Call<List<Item>> getProducts(@Query("query") String query);
}
