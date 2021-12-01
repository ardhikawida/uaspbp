package com.ssd.ssd.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseBody {
    @SerializedName("message")
     String message;
    @SerializedName("data")
     List<UsersModel> usersmodel;

    public String getMessage() {
        return message;
    }

    public List<UsersModel> getUsersmodel() {
        return usersmodel;
    }
}


