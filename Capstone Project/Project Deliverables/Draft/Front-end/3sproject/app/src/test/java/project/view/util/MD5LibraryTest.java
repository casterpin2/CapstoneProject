package project.view.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class MD5LibraryTest {

    @Test
    public void md5() {
        String input = "thang@12345";
        String output;
        String expected = "62882434f560dd0a83bdb4bf648b9548";


        MD5Library md5 = new MD5Library();
        output = md5.md5(input);

        assertEquals(expected, output);
    }
}