package project.retrofit;

import java.util.List;

import project.objects.User;
import project.view.AddProductToStore.Item;
import project.view.Brand.Brand;
import project.view.Category.Category;
import project.view.ProductBrandDisplay.ProductBrand;
import project.view.ProductInStore.ProductInStore;
import project.view.SaleProduct.SaleProduct;
import project.view.UserInformation.UserInformation;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
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
    @FormUrlEncoded
    Call<Boolean> insertProduct(@Field("jsonString") StringBuilder stringJson,
                           @Field("storeId") int storeId);

    @GET("brands")
    Call<List<Brand>> getBrands();

    @GET("brands/productWithBrand")
    Call<List<ProductBrand>> getProductBrand(@Query("brandId") int brandId);
    @GET("category")
    Call<List<Category>> getCategory();

    @GET("getProductInStore")
    Call<List<ProductInStore>> getProductInStore(@Query("storeID") int storeID);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://150.95.111.195:8080/3sProjectFinal/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    //UserService
    @GET("vadilateRegisterUser")
    Call<Integer> vadilator(@Query("username") String userExists,
                                       @Query("email") String email,@Query("phone") String phone,@Query("typeSearch") String typeSearch);

    @POST("registerUser")
    @FormUrlEncoded
    Call<Boolean> registerUserNew(@Field("jsonString") String jsonString);
    @GET("productSales/top20")
    Call<List<SaleProduct>> getSaleProductTop20();
    @GET("productSales")
    Call<List<SaleProduct>> getSaleProduct();
}
