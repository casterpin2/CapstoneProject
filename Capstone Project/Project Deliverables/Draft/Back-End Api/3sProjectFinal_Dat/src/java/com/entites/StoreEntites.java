/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entites;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author DatNQ
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreEntites implements Serializable {
    private int id;
    private String name;
    private int user_id;
    private String phone;
    private String image_path;
    private int status;

    public StoreEntites(int id, String name, int user_id, String phone, String image_path, int status) {
        this.id = id;
        this.name = name;
        this.user_id = user_id;
        this.phone = phone;
        this.image_path = image_path;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    
    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
    
    
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public StoreEntites() {
        super();
    }
    public StoreEntites(String name) {
        this.name = name;

    }

    public StoreEntites(String name, int user_id, String phone) {
        this.name = name;
        this.user_id = user_id;
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Store{" + "id=" + id + ", name=" + name + ", user_id=" + user_id + ", phone=" + phone + ", image_path=" + image_path + ", status=" + status + '}';
    }

   
    
}
