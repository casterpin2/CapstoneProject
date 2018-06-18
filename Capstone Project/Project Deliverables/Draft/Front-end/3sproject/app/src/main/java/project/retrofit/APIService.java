package project.retrofit;

import java.util.List;

import project.objects.User;
import project.view.AddProductToStore.Item;
import project.view.Brand.Brand;
import project.view.Category.Category;
import project.view.ProductBrandDisplay.ProductBrand;
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

    @POST("posts")

    Call<Boolean> insertProduct(@Query("jsonString") StringBuilder stringJson,
                           @Query("storeId") int storeId);

    @GET("brands")
    Call<List<Brand>> getBrands();

    @GET("brands/productWithBrand")
    Call<List<ProductBrand>> getProductBrand(@Query("brandId") int brandId);
    @GET("category")
    Call<List<Category>> getCategory();
}
