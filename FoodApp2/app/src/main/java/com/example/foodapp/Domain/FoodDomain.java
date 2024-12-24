package com.example.foodapp.Domain;

import java.io.Serializable;

public class FoodDomain implements Serializable {
    private String title = "";
    private String pic= "";
    private  String description = "";
    private Double fee =0.0;
    private int numberInCart=0;
    private int star = 0;
    private  int time = 0;
    private int calories =0;

    public FoodDomain(String title, String pic, String description, Double fee, int numberIncart, int star, int time, int calories) {
        this.title = title;
        this.pic = pic;
        this.description = description;
        this.fee = fee;
        this.numberInCart = numberIncart;
        this.star = star;
        this.time = time;
        this.calories = calories;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public int getNumberInCart() {
        return numberInCart;
    }


    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }


    }

