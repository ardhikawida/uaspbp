package com.ssd.ssd.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseBodyBarang {
    @SerializedName("message")
     String message;
    @SerializedName("data")
     List<FoodModels> foodModels;

    public ResponseBodyBarang(String message, List<FoodModels> foodModels) {
        this.message = message;
        this.foodModels = foodModels;
    }

    public String getMessage() {
        return message;
    }

    public List<FoodModels> getFoodModels() {
        return foodModels;
    }
}


