package com.surroundsync.ashrayjoshi.draftsaver;

/**
 * Created by Ashray Joshi on 30-Jun-16.
 */
public class Drafts {
    String _id;
    String drafts;
    String latitude;
    String longitude;
    boolean image;

    public Drafts( ){
}
    // constructor
    public Drafts(String id, String drafts, Boolean image,String latitude,String longitude){
        this._id = id;
        this.image=image;
        this.drafts = drafts;
        this.latitude=latitude;
        this.longitude=longitude;

    }

    // getting ID
    public String getID(){
        return this._id;
    }

    // setting id
    public void setID(String id){
        this._id = id;
    }

    // getting name
    public String getDrafts(){
        return this.drafts;
    }

    public void setDrafts(String drafts) {
        this.drafts = drafts;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
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

