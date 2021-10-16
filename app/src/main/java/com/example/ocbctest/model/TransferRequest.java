package com.example.ocbctest.model;

import com.google.gson.annotations.SerializedName;

public class TransferRequest {
    @SerializedName("recipientAccountNo")
    private String recipientAccountNo;
    @SerializedName("amount")
    private Double amount;
    @SerializedName("date")
    private String date;
    @SerializedName("description")
    private String description;

    public String getRecipientAccountNo() {
        return recipientAccountNo;
    }

    public void setRecipientAccountNo(String recipientAccountNo) {
        this.recipientAccountNo = recipientAccountNo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
