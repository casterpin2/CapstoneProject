package project.view.util;

import org.junit.Test;

import project.view.model.Product;

import static org.junit.Assert.*;

public class ProductInStoreCompareableIncreaseTest {

    @Test
    public void compare() {
        Product product1 = new Product(1,"Lavie 1 lít","Lavie","Đây là Lavie 1 lít","Đồ uống","Đồ uống giải khát","./firebase/lavie1l",5000,10.0);
        Product product2 = new Product(2,"Lavie 5 lít","Lavie","Đây là Lavie 5 lít","Đồ uống","Đồ uống giải khát","./firebase/lavie5l",20000,0.0);
        int output;
        int expected = -1;


        ProductInStoreCompareableDecrease compareDecrease = new ProductInStoreCompareableDecrease();
        output = compareDecrease.compare(product1, product2);

        assertEquals(expected, output);
    }
}