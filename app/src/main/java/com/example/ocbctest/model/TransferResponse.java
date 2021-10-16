package com.example.ocbctest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransferResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private TransferData transferInfo;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TransferData getTransferInfo() {
        return transferInfo;
    }

    public void setTransferInfo(TransferData transferInfo) {
        this.transferInfo = transferInfo;
    }

    public class TransferData{
        @SerializedName("id")
        private String id;
        @SerializedName("recipientAccountNo")
        private String recipientAccountNo;
        @SerializedName("amount")
        private Double amount;
        @SerializedName("date")
        private String date;
        @SerializedName("description")
        private String description;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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
}
