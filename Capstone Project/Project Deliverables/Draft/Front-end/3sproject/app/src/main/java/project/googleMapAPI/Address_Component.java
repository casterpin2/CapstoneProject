package project.googleMapAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public class Address_Component {
    @SerializedName("address_components")
    @Expose
    private List<Address> addressList;
    @SerializedName("formatted_address")
    @Expose
    private String formatted_address;
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;
    @SerializedName("place_id")
    @Expose
    private String place_id;
    @SerializedName("types")
    @Expose
    private String[] types;

    public Address_Component(List<Address> addressList, String formatted_address, Geometry geometry, String place_id, String[] types) {
        this.addressList = addressList;
        this.formatted_address = formatted_address;
        this.geometry = geometry;
        this.place_id = place_id;
        this.types = types;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "Address_Component{" +
                "addressList=" + addressList +
                ", formatted_address='" + formatted_address + '\'' +
                ", geometry=" + geometry +
                ", place_id='" + place_id + '\'' +
                ", types=" + Arrays.toString(types) +
                '}';
    }
}
