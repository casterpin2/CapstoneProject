package project.googleMapAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Address {
    @SerializedName("long_name")
    @Expose
    private String long_name;
    @SerializedName("short_name")
    @Expose
    private String short_name;
    @SerializedName("types")
    @Expose
    private String[] types;

    public Address(String long_name, String short_name, String[] types) {
        this.long_name = long_name;
        this.short_name = short_name;
        this.types = types;
    }

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "Address{" +
                "long_name='" + long_name + '\'' +
                ", short_name='" + short_name + '\'' +
                ", types=" + Arrays.toString(types) +
                '}';
    }
}
