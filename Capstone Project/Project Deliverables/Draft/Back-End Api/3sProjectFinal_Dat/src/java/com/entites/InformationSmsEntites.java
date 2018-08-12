/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entites;

import java.io.Serializable;

/**
 *
 * @author Tuyen
 */
public class InformationSmsEntites implements Serializable {

    private String pin_code;
    private String tranId;
    private String totalSMS;
    private String totalPrice;
    private String pin;
    private String phone;
    private boolean verified;
    private String remainingAttempts;
    public InformationSmsEntites() {
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public String getTotalSMS() {
        return totalSMS;
    }

    public void setTotalSMS(String totalSMS) {
        this.totalSMS = totalSMS;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getRemainingAttempts() {
        return remainingAttempts;
    }

    public void setRemainingAttempts(String remainingAttempts) {
        this.remainingAttempts = remainingAttempts;
    }

}
