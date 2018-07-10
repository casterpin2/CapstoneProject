package project.googleMapAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoogleMapJSON {
    @SerializedName("results")
    @Expose
    private List<Address_Component> results;
    @SerializedName("status")
    @Expose
    private String status;


    public GoogleMapJSON(List<Address_Component> result, String status) {
        this.results = result;
        this.status = status;
    }

    public List<Address_Component> getResult() {
        return results;
    }

    public void setResult(List<Address_Component> result) {
        this.results = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "GoogleMapJSON{" +
                "result=" + results +
                ", status='" + status + '\'' +
                '}';
    }
}
