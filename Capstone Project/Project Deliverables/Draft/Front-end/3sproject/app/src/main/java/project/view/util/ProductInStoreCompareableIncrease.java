package project.view.util;


import java.util.Comparator;

import project.view.model.ProductInStore;

public class ProductInStoreCompareableIncrease implements Comparator<ProductInStore> {
    private Double price1,price2;

    public int compare(ProductInStore productInStore1,ProductInStore productInStore2) {
        if(productInStore1.getPromotionPercent()==0){
            price1 = Double.parseDouble(String.valueOf(productInStore1.getProductPrice()));
        } else {
            price1 = Double.parseDouble(String.valueOf(productInStore1.getProductPrice() - (productInStore1.getProductPrice()* productInStore1.getPromotionPercent()/100)));
        }

        if(productInStore2.getPromotionPercent()==0){
            price2 = Double.parseDouble(String.valueOf(productInStore2.getProductPrice()));
        } else {
            price2 = Double.parseDouble(String.valueOf(productInStore2.getProductPrice() - (productInStore2.getProductPrice()* productInStore2.getPromotionPercent()/100)));
        }

        if (price1 == price2)
            return 0;
        else if (price1 < price2)
            return 1;
        else
            return -1;
    }

}
