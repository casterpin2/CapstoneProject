package project.retrofit;

import project.objects.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {
    @POST("login.web")
    @FormUrlEncoded
    Call<User> savePost(@Field("username") String username,
                        @Field("password") String password);
}
