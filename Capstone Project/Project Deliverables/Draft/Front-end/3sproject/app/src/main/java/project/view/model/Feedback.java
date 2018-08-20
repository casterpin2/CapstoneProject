package project.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feedback {
    @SerializedName("user_id")
    @Expose
    private int user_id;
    @SerializedName("store_id")
    @Expose
    private int store_id;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("isSatisfied")
    @Expose
    private int isSatisfied;
    @SerializedName("registerLog")
    @Expose
    private String registerLog;

    public String getRegisterLog() {
        return registerLog;
    }

    public void setRegisterLog(String registerLog) {
        this.registerLog = registerLog;
    }

    public Feedback() {
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
