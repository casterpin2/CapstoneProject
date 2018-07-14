package project.view.ZTest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class BuyProductInStore {
    @SerializedName("buy_id")
    @Expose
    private int productID;
    @SerializedName("buy_name")
    @Expose
    private String productName;
    @SerializedName("image_buy")
    @Expose
    private String productImage;
    @SerializedName("buyproductImage")
    @Expose
    private String categoryName;
    @SerializedName("buyproductCategory")
    @Expose
    private String brandName;
    @SerializedName("buySaleproduct")
    @Expose
    private long productPrice;
    @SerializedName("promotion")
    @Expose
    private double promotionPercent;
}
