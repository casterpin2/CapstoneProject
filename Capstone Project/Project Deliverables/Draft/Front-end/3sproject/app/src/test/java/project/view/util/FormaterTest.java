package project.view.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class FormaterTest {

    @Test
    public void formatDoubleToMoney() {
        String input = "100000";
        String output;
        String expected = "100.000 đ";


        Formater formater = new Formater();
        output = formater.formatDoubleToMoney(input);

        assertEquals(expected, output);
    }

    @Test
    public void formatDoubleToInt() {
        String input = "5";
        String output;
        String expected = "5%";


        Formater formater = new Formater();
        output = formater.formatDoubleToInt(input);

        assertEquals(expected, output);
    }
}