package project.googleMapAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Viewport implements Serializable {
    @SerializedName("northeast")
    @Expose
    private BoundEntity northeast;
    @SerializedName("southwest")
    @Expose
    private BoundEntity southwest;

    public Viewport(BoundEntity northeast, BoundEntity southwest) {
        this.northeast = northeast;
        this.southwest = southwest;
    }

    public BoundEntity getNortheast() {
        return northeast;
    }

    public void setNortheast(BoundEntity northeast) {
        this.northeast = northeast;
    }

    public BoundEntity getSouthwest() {
        return southwest;
    }

    public void setSouthwest(BoundEntity southwest) {
        this.southwest = southwest;
    }
}
