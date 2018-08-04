package project.retrofit;

import java.util.HashMap;
import java.util.List;

import project.googleMapAPI.GoogleMapJSON;
import project.objects.User;
import project.view.model.Brand;
import project.view.model.Item;
import project.view.model.Product;
import project.view.model.Category;
import project.view.model.Login;
import project.view.model.ProductInStore;
import project.view.model.ResultRegister;
import project.view.model.Store;
import project.view.model.SaleProduct;
import project.view.model.NearByStore;
import project.view.model.Type;
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
    //API Login
    @GET("login")
    Call<Login> login(@Query("username") String username,
                      @Query("password") String password);
    @POST("loginFB")
    Call<Login> loginFB(@Body User user,
                        @Query("FBId") String FBId);
    ////////////////////////////////////////////////

    @POST("registerStore")
    Call<Store> registerStore(@Body HashMap<String,String> map);

    @GET("getProductForAdd")
    Call<List<Item>> getProducts(@Query("query") String query, @Query("page") int page,@Query("storeId") int storeId);

    @POST("posts")
    Call<Boolean> insertProduct(@Body Item stringJson,
                           @Query("storeId") int storeId);

    @GET("brands")
    Call<List<Brand>> getBrands();

    @GET("brands/productWithBrand")
    Call<List<Product>> getProductBrand(@Query("brandId") int brandId);
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
    Call<ResultRegister> registerUserNew(@Body User user);

    //User search product
    @GET("userSearchProduct")
    Call<List<Product>> userSearchProduct(@Query("productName") String productName);

    @GET("productSales/top20")
    Call<List<SaleProduct>> getSaleProductTop20();
    @GET("productSales")
    Call<List<SaleProduct>> getSaleProduct();

    //Google Map Service
    @GET("json")
    Call<GoogleMapJSON> getLocation(@Query("latlng") String latlng,
                                    @Query("key") String key);


    @GET("findStore")
    Call<List<NearByStore>> nearByStore(@Query("productId") int productId,@Query("latitude") String latitude,@Query("longitude") String longitude);

    @GET("getType")
    Call<List<Type>> getType(@Query("categoryId") int procategoryId);

    @GET("getProductWithBarcode")
    Call<List<Item>> getProductWithBarcode(@Query("barcode") String barcode,@Query("store") int storeId);

}
