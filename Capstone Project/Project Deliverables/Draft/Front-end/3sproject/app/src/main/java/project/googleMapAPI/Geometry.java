package project.googleMapAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Geometry implements Serializable {
    @SerializedName("bounds")
    @Expose
    private Bounds bounds;
    @SerializedName("location")
    @Expose
    private BoundEntity location;
    @SerializedName("location_type")
    @Expose
    private String location_type;
    @SerializedName("viewport")
    @Expose
    private Viewport viewport;

    public Geometry(Bounds bounds, BoundEntity location, String location_type, Viewport viewport, String place_id, String[] types) {
        this.bounds = bounds;
        this.location = location;
        this.location_type = location_type;
        this.viewport = viewport;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public BoundEntity getLocation() {
        return location;
    }

    public void setLocation(BoundEntity location) {
        this.location = location;
    }

    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }
}
