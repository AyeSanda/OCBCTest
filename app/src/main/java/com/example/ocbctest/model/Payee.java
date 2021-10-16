package com.example.ocbctest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Payee {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private List<PayeeData> payeeDataList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PayeeData> getPayeeDataList() {
        return payeeDataList;
    }

    public void setPayeeDataList(List<PayeeData> payeeDataList) {
        this.payeeDataList = payeeDataList;
    }

    public class PayeeData{
        @SerializedName("id")
        private String id;
        @SerializedName("accountNo")
        private String accountNo;
        @SerializedName("accountHolderName")
        private String accountHolderName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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
