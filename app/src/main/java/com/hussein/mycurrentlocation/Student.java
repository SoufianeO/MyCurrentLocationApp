package com.hussein.mycurrentlocation;

import android.location.Location;


public class Student {
    public int Image;
    public  String name;
    public  String des;
    public  double duree;
    public  boolean isCatch;
    public Location location;

    Student(int Image, String name, String des, double dure, double lat, double lag){
        this.Image=Image;
        this.name=name;
        this.des=des;
        this.duree=dure;
        this.isCatch=false;
        location= new Location(name);
        location.setLongitude(lag);
        location.setLatitude(lat);

    }
}
