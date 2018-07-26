package project.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import project.objects.User;

public class Login {
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("store")
    @Expose
    private Store store;

    public Login() {
        user = new User();
        store = new Store();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
