package project.retrofit;

import java.util.List;

import project.objects.User;
import project.view.AddProductToStore.Item;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {
    @POST("login")
    @FormUrlEncoded
    Call<User> savePost(@Field("username") String username,
                        @Field("password") String password);
    @POST("getProductForAdd")
    @FormUrlEncoded
    Call<List<Item>> getProducts(@Field(value  = "query", encoded = true) String query);
}
