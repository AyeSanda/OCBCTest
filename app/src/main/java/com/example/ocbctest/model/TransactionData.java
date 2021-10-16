package com.example.ocbctest.model;

import com.google.gson.annotations.SerializedName;

public class TransactionData {

    @SerializedName("id")
    private String id;
    @SerializedName("type")
    private String type;
    @SerializedName("amount")
    private Double amount;
    @SerializedName("currency")
    private String currency;
    @SerializedName("from")
    private AccountInfo receivedFrom;
    @SerializedName("to")
    private AccountInfo transferTo;
    @SerializedName("description")
    private String description;
    @SerializedName("date")
    private String date;
    @SerializedName("recipientAccountNo")
    private String recipientAccountNo;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public AccountInfo getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(AccountInfo receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    public AccountInfo getTransferTo() {
        return transferTo;
    }

    public void setTransferTo(AccountInfo transferTo) {
        this.transferTo = transferTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRecipientAccountNo() {
        return recipientAccountNo;
    }

    public void setRecipientAccountNo(String recipientAccountNo) {
        this.recipientAccountNo = recipientAccountNo;
    }

    public class AccountInfo{
        @SerializedName("accountNo")
        private String accountNo;
        @SerializedName("accountHolderName")
        private String accountHolderName;

        public String getAccountNo() {
            return accountNo;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        public String getAccountHolderName() {
            return accountHolderName;
        }

        public void setAccountHolderName(String accountHolderName) {
            this.accountHolderName = accountHolderName;
        }
    }
}
