package project.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Notification {

    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("notification")
    @Expose
    private NotificationDetail notification;

    public Notification() {
    }


    public Notification(String to, NotificationDetail notification) {
        this.to = to;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public NotificationDetail getNotification() {
        return notification;
    }

    public void setNotification(NotificationDetail notification) {
        this.notification = notification;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "to='" + to + '\'' +
                ", notification=" + notification +
                '}';
    }
}
