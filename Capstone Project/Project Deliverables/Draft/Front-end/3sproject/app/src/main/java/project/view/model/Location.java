package project.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("apartment_number")
    @Expose
    private int apartment_number;
    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("county")
    @Expose
    private String county;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;

    public Location() {
    }

    public Location(int apartment_number, String street, String county, String district, String city, String latitude, String longitude) {
        this.apartment_number = apartment_number;
        this.street = street;
        this.county = county;
        this.district = district;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getApartment_number() {
        return apartment_number;
    }

    public void setApartment_number(int apartment_number) {
        this.apartment_number = apartment_number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "apartment_number=" + apartment_number +
                ", street='" + street + '\'' +
                ", county='" + county + '\'' +
                ", district='" + district + '\'' +
                ", city='" + city + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
