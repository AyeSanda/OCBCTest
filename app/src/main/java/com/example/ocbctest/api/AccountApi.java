package com.example.ocbctest.api;

import com.example.ocbctest.model.Balance;
import com.example.ocbctest.model.LoginResponse;
import com.example.ocbctest.model.Payee;
import com.example.ocbctest.model.Transaction;
import com.example.ocbctest.model.TransferRequest;
import com.example.ocbctest.model.TransferResponse;
import com.example.ocbctest.model.LoginRequest;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;

//https://guides.codepath.com/android/consuming-apis-with-retrofit
//https://stackoverflow.com/questions/33774940/get-response-status-code-using-retrofit-2-0-and-rxjava
public interface AccountApi {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("authenticate/login")
    Observable<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("account/balances")
    Observable<Balance> getAccountBalance(@HeaderMap Map<String,String> requestHeader);

    @GET("account/transactions")
    Observable<Transaction> getTransactions(@HeaderMap Map<String,String> requestHeader);

    @GET("account/payees")
    Observable<Payee> getPayees(@HeaderMap Map<String,String> requestHeader);

    @POST("transfer")
    Observable<TransferResponse> transferToAcc(
            @HeaderMap Map<String,String> requestHeader,
            @Body TransferRequest transferRequest);

}
