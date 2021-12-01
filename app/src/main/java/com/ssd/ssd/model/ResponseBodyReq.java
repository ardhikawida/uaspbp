package com.ssd.ssd.model;

import com.google.gson.annotations.SerializedName;

public class ResponseBodyReq {
    @SerializedName("message")
     String message;
    @SerializedName("data")
     UsersModel usersmodel;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UsersModel getUsersmodel() {
        return usersmodel;
    }

    public void setUsersmodel(UsersModel usersmodel) {
        this.usersmodel = usersmodel;
    }
}

