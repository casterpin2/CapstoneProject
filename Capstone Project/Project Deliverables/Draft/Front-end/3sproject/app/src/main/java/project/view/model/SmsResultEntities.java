package project.view.model;

import java.io.Serializable;

public class SmsResultEntities implements Serializable{

    private String status;
    private String code;
    private InformationSmsEntites data;
    private String message;
    private String username;
    private String phoneUser;
    public SmsResultEntities() {
        data = new InformationSmsEntites();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public InformationSmsEntites getData() {
        return data;
    }

    public void setData(InformationSmsEntites data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneUser() {
        return phoneUser;
    }

    public void setPhoneUser(String phoneUser) {
        this.phoneUser = phoneUser;
    }
}
