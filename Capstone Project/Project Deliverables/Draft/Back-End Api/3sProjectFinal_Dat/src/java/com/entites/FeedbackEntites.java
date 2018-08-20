/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entites;

/**
 *
 * @author AHBP
 */
public class FeedbackEntites {
    private int user_id;
    private int store_id;
    private String content;
    private int isSatisfied;
    private String registerLog;

    public String getRegisterLog() {
        return registerLog;
    }

    public void setRegisterLog(String registerLog) {
        this.registerLog = registerLog;
    }
    
    public FeedbackEntites() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsSatisfied() {
        return isSatisfied;
    }

    public void setIsSatisfied(int isSatisfied) {
        this.isSatisfied = isSatisfied;
    }
    
    
}
