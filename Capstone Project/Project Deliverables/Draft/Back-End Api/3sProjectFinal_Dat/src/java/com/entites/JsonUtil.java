/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entites;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author TUYEN
 */
public class JsonUtil {

    private static final ObjectMapper MAPPPER;

    static {
        MAPPPER = new ObjectMapper();
    }

    public static <T> List<T> converJsonToJava(String jsonString, Class<T> cls) throws ClassNotFoundException, IOException {

        Class<T[]> arrayClass = (Class<T[]>) Class.forName("[L" + cls.getName() + ";");
        T[] objects = MAPPPER.readValue(jsonString, arrayClass);
       

        return Arrays.asList(objects);
    }

}
