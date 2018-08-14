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
import project.view.model.ResultRegister;
import project.view.model.SmsResultEntities;
import project.view.model.Store;
import project.view.model.NearByStore;
import project.view.model.Type;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @GET("brands")
    Call<List<Brand>> getBrands();

    @GET("brands/top10")
    Call<List<Brand>> getBrandsTop5();

    @GET("brands/productWithBrand")
    Call<List<Product>> getProductBrand(@Query("brandId") int brandId);

    @GET("category")
    Call<List<Category>> getCategory();


    @GET("category/products")
    Call<List<Product>> getProductInCategory(@Query("page") int page, @Query("categoryId") int categoryId);
    //Store//

    @DELETE("deleteProductInStore")
    Call<Boolean> deleteProductInStore (@Query("storeId") int storeID,@Query("productId") int productID);

    @GET("getProductInStore")
    Call<List<Product>> getProductInStore(@Query("storeID") int storeID);

    @PUT("editProductInStore")
    Call<Boolean> editProductInStore (@Query("storeId") int storeID,@Query("productId") int productID,@Query("price") long price,@Query("promotion") double promotion);

    @POST("registerStore")
    Call<Store> registerStore(@Body HashMap<String,String> map);

    @GET("getStoreById")
    Call<Store> getStoreById(@Query("storeId") int storeID);

    @POST("posts")
    Call<Boolean> insertProduct(@Body Item stringJson,
                                @Query("storeId") int storeId);

    @GET("getProductForAdd")
    Call<List<Item>> getProducts(@Query("query") String query, @Query("page") int page,@Query("storeId") int storeId);

    @GET("getProductById")
    Call<Product> getProductById(@Query("productId") int productId, @Query("storeId") int storeId);
    //Store//
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
    Call<List<Product>> userSearchProduct(@Query("productName") String productName,@Query("page") int page);

    @GET("productSales/top20")
    Call<List<Product>> getSaleProductTop20();
    @GET("productSales")
    Call<List<Product>> getSaleProduct();

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

    @GET("getProductbyType")
    Call<List<Product>> getProductbyType(@Query("typeId") int typeId);

    @GET("informationUser")
    Call<User> getInformation(@Query("userId") int userId);

    @PUT("updateInformation")
    Call<User> updateInfotmation(@Body User user);

    @GET("userSearchBarcode")
    Call<List<Product>> userSearchBarcode(@Query("barcode") String barcode);

    @PUT("updateStore")
    Call<Store> updateStore(@Body HashMap<String,String> map);

    @GET("getCodeVerifyUser")
    Call<SmsResultEntities> getCodeVerify(@Query("username") String username);

    @GET("verifyUserWithCode")
    Call<Boolean> confirmOTP(@Query("code") String code,@Query("phone") String phone);

    @PUT("changePassword")
    Call<Boolean> requestChangePassword(@Query("username") String username,@Query("password") String password);
}
