package de.unidue.palaver.model;

import android.location.Address;

import java.io.Serializable;

public class PalaverLocation implements Serializable {

    private double latitude;
    private double longitude;
    private Address address;

    public PalaverLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "PalaverLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", address=" + address +
                '}';
    }

    public String getLocationUrlString() {
        return "https://maps.google.com/?q="+latitude+","+longitude;
    }
}
