package com.example.ocbctest.model;

import com.google.gson.annotations.SerializedName;

public class Balance {
    @SerializedName("status")
    private String status;
    @SerializedName("balance")
    private Double balance;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
