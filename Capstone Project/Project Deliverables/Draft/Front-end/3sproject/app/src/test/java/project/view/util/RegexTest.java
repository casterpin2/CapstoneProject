package project.view.util;

import android.widget.TextView;

import org.junit.Test;

import project.view.gui.RegisterPage;

import static org.junit.Assert.*;

public class RegexTest {

    @Test
    public void isPassWord() {
        String input = "thang@12345";
        boolean output;
        boolean expected = true;


        Regex regex = new Regex();
        output = regex.isPassWord(input);

        assertEquals(expected, output);
    }

    @Test
    public void isUserName() {
        String input = "thangnd16";
        boolean output;
        boolean expected = true;


        Regex regex = new Regex();
        output = regex.isUserName(input);

        assertEquals(expected, output);
    }

    @Test
    public void isName() {
        String input = "Thắng Nguyễn";
        boolean output;
        boolean expected = true;


        Regex regex = new Regex();
        output = regex.isName(input);

        assertEquals(expected, output);
    }

    @Test
    public void isStoreName() {
        String input = "Cửa hàng của Thắng";
        boolean output;
        boolean expected = true;


        Regex regex = new Regex();
        output = regex.isStoreName(input);

        assertEquals(expected, output);
    }

    @Test
    public void isEmail() {
        String input = "thangndse04441@fpt.edu.vn";
        boolean output;
        boolean expected = true;


        Regex regex = new Regex();
        output = regex.isEmail(input);

        assertEquals(expected, output);
    }


}