package com.example.ocbctest.api;

import com.example.ocbctest.OCBCTestApplication;
import com.example.ocbctest.model.Balance;
import com.example.ocbctest.model.LoginRequest;
import com.example.ocbctest.model.LoginResponse;
import com.example.ocbctest.model.Payee;
import com.example.ocbctest.model.Transaction;
import com.example.ocbctest.model.TransferRequest;
import com.example.ocbctest.model.TransferResponse;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountApiService {
    private AccountApi api;
    private Retrofit retrofit;
    private static String BASE_URL = "http://192.168.10.137:8080/";
    private static AccountApiService instance = null;

    public AccountApiService(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        //default network calls to be asynchronous, use createWithScheduler().
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(client)
                .build();

        api = retrofit.create(AccountApi.class);
    }

    public static AccountApiService getInstance(){
        if(instance == null){
            instance = new AccountApiService();
        }
        return instance;
    }

    public Observable<LoginResponse> login(LoginRequest loginRequest){
        return api.login(loginRequest);
    }

    public Observable<Balance> getAccountBalance(){
        return api.getAccountBalance(getAuthenticationHeaders());
    }

    public Observable<Transaction> getTransactions(){
        return api.getTransactions(getAuthenticationHeaders());
    }

    public Observable<Payee> getPayees(){
        return api.getPayees(getAuthenticationHeaders());
    }

    public Observable<TransferResponse> transferToAcc(TransferRequest transferRequest){
        return api.transferToAcc(getAuthenticationHeaders(), transferRequest);
    }

    public Map<String,String> getAuthenticationHeaders(){
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("Accept","application/json");
        headers.put("Authorization", OCBCTestApplication.getToken());
        return headers;
    }
}
