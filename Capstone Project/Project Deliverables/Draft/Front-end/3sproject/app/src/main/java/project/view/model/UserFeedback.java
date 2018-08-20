package project.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import project.objects.User;

public class UserFeedback implements Serializable{
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("feedback")
    @Expose
    private Feedback feedback;

    public UserFeedback() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }
}
