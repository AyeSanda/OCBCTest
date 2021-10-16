package com.example.ocbctest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Transaction {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<TransactionData> transactionDataList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TransactionData> getTransactionDataList() {
        return transactionDataList;
    }

    public void setTransactionDataList(List<TransactionData> transactionDataList) {
        this.transactionDataList = transactionDataList;
    }
}
