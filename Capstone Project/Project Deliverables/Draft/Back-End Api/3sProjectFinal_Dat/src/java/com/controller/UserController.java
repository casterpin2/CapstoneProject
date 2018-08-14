/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entites.JsonUtil;
import com.entites.NearByStore;
import com.entites.ProductAddEntites;
import com.entites.SmsResultEntities;
import com.entites.UserEntites;
import com.service.UserService;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 *
 * @author TUYEN
 */
@RestController
@RequestMapping(value = "/api")
@EnableWebMvc
public class UserController {

    @Autowired
    UserService user;
    private final String API_URL = "http://api.speedsms.vn/index.php";
    private final String ACCESSTOKEN = "kNR-Yn82kdARW7rryxYL-Naf4e4sIC0_";
    private final String APP_ID = "gLoTXGfZAUBn0ax1um_qDR7PHeNNkfuB";

    @RequestMapping(value = "/getAllUser", method = RequestMethod.GET, produces = "application/json")
    public List<UserEntites> createNewEmployee() throws SQLException {

        return user.getAllUserForAdmin();
    }

    @RequestMapping(value = "/registerUser", method = RequestMethod.POST, produces = "application/json")
    public String registerUser(@RequestBody String jsonString) throws SQLException, ClassNotFoundException, IOException {
        JsonFactory factory = new JsonFactory();
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        ObjectMapper mapper = new ObjectMapper(factory);
        UserEntites us = mapper.readValue(jsonString, UserEntites.class);
        //String result = 
        return "{\"result\":" + "\"" + user.registerUser(us) + "\"}";
    }

    @RequestMapping(value = "/vadilateRegisterUser", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Integer vadilatorUser(@RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("phone") String phone, @RequestParam("typeSearch") String typeSearch) throws SQLException, ClassNotFoundException, IOException {

        return user.userHasExists(username, email, phone, typeSearch);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = "application/json")
    public HashMap<String, Object> login(@RequestParam("username") String username, @RequestParam("password") String password) throws SQLException {

        return user.login(username, password);
    }

    @RequestMapping(value = "/loginFB", method = RequestMethod.POST, produces = "application/json")
    public HashMap<String, Object> loginFB(@RequestBody String userJSON, @RequestParam("FBId") String FBid) throws SQLException, IOException {
        JsonFactory factory = new JsonFactory();
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        ObjectMapper mapper = new ObjectMapper(factory);
        UserEntites us = mapper.readValue(userJSON, UserEntites.class);
        return user.loginFB(us, FBid);
    }
    
    @RequestMapping(value = "/loginGoogle", method = RequestMethod.POST, produces = "application/json")
    public HashMap<String, Object> loginGoogle(@RequestBody String userJSON, @RequestParam("GId") String Gid) throws SQLException, IOException {
        JsonFactory factory = new JsonFactory();
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        ObjectMapper mapper = new ObjectMapper(factory);
        UserEntites us = mapper.readValue(userJSON, UserEntites.class);
        return user.loginG(us, Gid);
    }

    @RequestMapping(value = "/userSearchProduct", method = RequestMethod.GET, produces = "application/json")
    public List<ProductAddEntites> userSearchProduct(@RequestParam("productName") String productName, @RequestParam("page") int page) throws SQLException, IOException {
        return user.userSearchProduct(productName, page);
    }

    @RequestMapping(value = "/findStore", method = RequestMethod.GET, produces = "application/json")
    public List<NearByStore> nearByStore(@RequestParam("productId") int productId, @RequestParam("latitude") String latitude, @RequestParam("longitude") String longitude) throws SQLException, IOException {
        return user.nearByStore(productId, latitude, longitude);
    }

    @RequestMapping(value = "/informationUser", method = RequestMethod.GET, produces = "application/json")
    public UserEntites getUserInformation(@RequestParam("userId") int userId) throws SQLException, IOException {
        return user.informationUser(userId);
    }

    @RequestMapping(value = "/updateInformation", method = RequestMethod.PUT, produces = "application/json")
    public UserEntites updateInformation(@RequestBody String userJson) throws SQLException, IOException {
        JsonFactory factory = new JsonFactory();
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        ObjectMapper mapper = new ObjectMapper(factory);
        UserEntites us = mapper.readValue(userJson, UserEntites.class);
        return user.updateInformation(us);
    }

    @RequestMapping(value = "/getCodeVerifyUser", method = RequestMethod.GET, produces = "application/json")
    public SmsResultEntities verifyUser(@RequestParam("username") String username) throws SQLException {
        SmsResultEntities sms = null;
        try {
            UserEntites userVerify = user.getPhoneNumberOfUser(username);
            if (userVerify != null) {
                String json = "{\"to\": \"" + userVerify.getPhone() + "\", \"content\": \"3S verification code is: {pin_code}\", \"app_id\": \"" + APP_ID + "\"}";
                String result = sendSms(json);
                JsonFactory factory = new JsonFactory();
                factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
                ObjectMapper mapper = new ObjectMapper(factory);
                sms = mapper.readValue(result, SmsResultEntities.class);
                sms.setUsername(userVerify.getUserName());
                sms.setPhoneUser(userVerify.getPhone());
                System.out.println(result);
            }

        } catch (IOException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sms;
    }

    @RequestMapping(value = "/verifyUserWithCode", method = RequestMethod.GET, produces = "application/json")
    public boolean verifyUserConfirm(@RequestParam("code") String code, @RequestParam("phone") String phone) throws SQLException {
        SmsResultEntities sms = null;
        try {
            String json = "{\"phone\": \"" + phone + "\", \"pin_code\": \"" + code + "\", \"app_id\": \"" + APP_ID + "\"}";
            String result = verifySms(json);
            JsonFactory factory = new JsonFactory();
            factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
            ObjectMapper mapper = new ObjectMapper(factory);
            sms = mapper.readValue(result, SmsResultEntities.class);
            if (sms.getStatus().equals("success")) {
                return true;
            }
            //@RequestParam("username") String username, @RequestParam("password") String password
            System.out.println(result);
        } catch (IOException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.PUT, produces = "application/json")
    public boolean changePasswordUser(@RequestParam("username") String username, @RequestParam("password") String password) throws SQLException {

        return user.changePassword(username, password);
    }

    public String sendSms(String json) {

        String smsJson = null;
        try {
            URL url = new URL(API_URL + "/pin/create");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            String userCredentials = ACCESSTOKEN + ":x";
            String basicAuth = "Basic " + DatatypeConverter.printBase64Binary(userCredentials.getBytes());
            conn.setRequestProperty("Authorization", basicAuth);
            conn.setRequestProperty("Content-Type", "application/json");

            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(json);
            wr.flush();
            wr.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine = "";
            StringBuilder buffer = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                buffer.append(inputLine);
            }
            in.close();
            smsJson = buffer.toString();
        } catch (IOException e) {
            e.getMessage();
        }
        return smsJson;
    }

    public String verifySms(String json) {

        String smsJson = null;
        try {
            URL url = new URL(API_URL + "/pin/verify");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            String userCredentials = ACCESSTOKEN + ":x";
            String basicAuth = "Basic " + DatatypeConverter.printBase64Binary(userCredentials.getBytes());
            conn.setRequestProperty("Authorization", basicAuth);
            conn.setRequestProperty("Content-Type", "application/json");

            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(json);
            wr.flush();
            wr.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine = "";
            StringBuilder buffer = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                buffer.append(inputLine);
            }
            in.close();
            smsJson = buffer.toString();
        } catch (IOException e) {
            e.getMessage();
        }
        return smsJson;
    }
}
