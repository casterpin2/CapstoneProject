package project.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultNotification {
    @SerializedName("multicast_id")
    @Expose
    private String multicast_id;
    @SerializedName("failure")
    @Expose
    private int failure;
    @SerializedName("canonical_ids")
    @Expose
    private int canonical_ids;
    @SerializedName("success")
    @Expose
    private String success;

    public ResultNotification() {
    }

    public String getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(String multicast_id) {
        this.multicast_id = multicast_id;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getCanonical_ids() {
        return canonical_ids;
    }

    public void setCanonical_ids(int canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    @Override
    public String toString() {
        return "ResultNotification{" +
                "multicast_id='" + multicast_id + '\'' +
                ", failure=" + failure +
                ", canonical_ids=" + canonical_ids +
                ", success='" + success + '\'' +
                '}';
    }
}
