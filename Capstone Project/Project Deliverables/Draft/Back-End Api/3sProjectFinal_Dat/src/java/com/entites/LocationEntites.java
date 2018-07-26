/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entites;

import java.io.Serializable;

/**
 *
 * @author DatNQ
 */
public class LocationEntites implements Serializable{
    private String apartment_number;
    private String street;
    private String county;
    private String district;
    private String city;
    private String latitude;
    private String longitude;
    private int id ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public LocationEntites() {
    }

    public LocationEntites(String apartment_number, String street, String county, String district, String city, String latitude, String longitude) {
        this.apartment_number = apartment_number;
        this.street = street;
        this.county = county;
        this.district = district;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getApartment_number() {
        return apartment_number;
    }

    public void setApartment_number(String apartment_number) {
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
    
    
}
