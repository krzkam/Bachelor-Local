package com.example.krzkam.local.model;

import io.realm.RealmObject;

/**
 * Created by krzkam on 30.01.2018.
 */

public class AddedPlace extends RealmObject{

    String name;
    String lat;
    String lng;
    String type;
    String address;
    String phone;
    String web;
    String id;

    public void setName(String name) {
        this.name = name;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAddress(String address) { this.address = address; }

    public void setPhone(String phone) { this.phone = phone; }

    public void setWeb(String web) { this.web = web; }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getType() { return type; }

    public String getAddress(){ return address;}

    public String getPhone(){ return phone;}

    public String getWeb(){ return web;}

    public String getId(){ return id;}

    @Override
    public String toString() {
        return "AddedPlace{" +
                "name='" + name + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", type='" + type + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", web='" + web + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
