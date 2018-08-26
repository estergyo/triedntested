package com.example.estergyofanny.triedntested;

import com.google.gson.annotations.SerializedName;

public class UserCheckResponse {
    @SerializedName("success")
    private int success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private String data;


    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
