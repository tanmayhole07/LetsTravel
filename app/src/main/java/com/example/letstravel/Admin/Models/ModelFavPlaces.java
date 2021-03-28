package com.example.letstravel.Admin.Models;

public class ModelFavPlaces {

    String placeImage, placeName, placeDescription, countryName, placeAvailablity, price, timeStamp;

    public ModelFavPlaces() {
    }

    public ModelFavPlaces(String placeImage, String placeName, String placeDescription, String countryName, String placeAvailablity, String price, String timeStamp) {
        this.placeImage = placeImage;
        this.placeName = placeName;
        this.placeDescription = placeDescription;
        this.countryName = countryName;
        this.placeAvailablity = placeAvailablity;
        this.price = price;
        this.timeStamp = timeStamp;
    }

    public String getPlaceImage() {
        return placeImage;
    }

    public void setPlaceImage(String placeImage) {
        this.placeImage = placeImage;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceDescription() {
        return placeDescription;
    }

    public void setPlaceDescription(String placeDescription) {
        this.placeDescription = placeDescription;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getPlaceAvailablity() {
        return placeAvailablity;
    }

    public void setPlaceAvailablity(String placeAvailablity) {
        this.placeAvailablity = placeAvailablity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
