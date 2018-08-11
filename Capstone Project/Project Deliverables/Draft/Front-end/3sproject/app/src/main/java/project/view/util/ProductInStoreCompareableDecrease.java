package project.view.util;

import java.util.Comparator;

import project.view.model.Product;

public class ProductInStoreCompareableDecrease implements Comparator<Product> {
    private Double price1,price2;

    public int compare(Product productInStore1,Product productInStore2) {
        if(productInStore1.getPromotion()==0){
            price1 = Double.parseDouble(String.valueOf(productInStore1.getPrice()));
        } else {
            price1 = Double.parseDouble(String.valueOf(productInStore1.getPrice() - (productInStore1.getPrice()* productInStore1.getPromotion()/100)));
        }

        if(productInStore2.getPromotion()==0){
            price2 = Double.parseDouble(String.valueOf(productInStore2.getPrice()));
        } else {
            price2 = Double.parseDouble(String.valueOf(productInStore2.getPrice() - (productInStore2.getPrice()* productInStore2.getPromotion()/100)));
        }

        if (price1 == price2)
            return 0;
        else if (price1 > price2)
            return 1;
        else
            return -1;
    }

}
