package com.example.ocbctest.viewmodel;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ocbctest.OCBCTestApplication;
import com.example.ocbctest.api.AccountApiService;
import com.example.ocbctest.model.Balance;
import com.example.ocbctest.model.LoginRequest;
import com.example.ocbctest.model.LoginResponse;
import com.example.ocbctest.model.Payee;
import com.example.ocbctest.model.Transaction;
import com.example.ocbctest.model.TransferRequest;
import com.example.ocbctest.model.TransferResponse;
import com.rugovit.eventlivedata.MutableEventLiveData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okio.Buffer;
import retrofit2.HttpException;

//Ref:https://proandroiddev.com/understanding-rxjava-subscribeon-and-observeon-744b0c6a41ea
public class AccountViewModel extends ViewModel {
    private String TAG = AccountViewModel.class.getName();
    private String STATUS_SUCCESS = "success";
    AccountApiService mAccountApiService;

    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();
    public MutableLiveData<Balance> accountbalance = new MutableLiveData<Balance>();
    public MutableLiveData<Transaction> accountTransaction = new MutableLiveData<Transaction>();
    public MutableLiveData<Payee> accountPayees = new MutableLiveData<Payee>();

    public MutableEventLiveData<Boolean> isTransferSuccess = new MutableEventLiveData<Boolean>();
    public MutableEventLiveData<String> errorMessage = new MutableEventLiveData<String>();
    public MutableEventLiveData<String> loginStatus = new MutableEventLiveData<String>();

    private CompositeDisposable disposable = new CompositeDisposable();  //to prevent memory loss becuse service will do in background thread

    public AccountViewModel(){
        mAccountApiService = AccountApiService.getInstance();
    }

    public void generateAuthToken(LoginRequest loginRequest){
        disposable.add(
                mAccountApiService.login(loginRequest)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<LoginResponse>() {
                            @Override
                            public void onNext(@NonNull LoginResponse loginResponse) {
                                if(loginResponse != null
                                        && loginResponse.getStatus().equals(STATUS_SUCCESS)
                                        && loginResponse.getToken() !=null){
                                    OCBCTestApplication.setToken(loginResponse.getToken());
                                }
                                loginStatus.setValue(loginResponse.getStatus());
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.e(TAG, e.getLocalizedMessage());
                                loginStatus.setValue(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                loading.setValue(false);
                            }
                        })
        );

    }

    public void getAccountBalance(){
        disposable.add(
                mAccountApiService.getAccountBalance()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<Balance>() {
                            @Override
                            public void onNext(@NonNull Balance Balance) {
                                if(Balance != null)
                                    accountbalance.setValue(Balance);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.e(TAG, e.getLocalizedMessage());
                                errorMessage.setValue(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {

                            }
                        })
        );
    }

    public void getTransactions(){
        disposable.add(
                mAccountApiService.getTransactions()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<Transaction>() {
                            @Override
                            public void onNext(@NonNull Transaction transaction) {
                                if(transaction != null
                                        && transaction.getStatus().equals(STATUS_SUCCESS)
                                        &&!transaction.getTransactionDataList().isEmpty()){
                                    accountTransaction.setValue(transaction);
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.e(TAG, e.getLocalizedMessage());
                                errorMessage.setValue(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {

                            }
                        })
        );

    }

    public void getPayees(){
        disposable.add(
                mAccountApiService.getPayees()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<Payee>() {
                            @Override
                            public void onNext(@NonNull Payee payee) {
                                if(payee != null && payee.getStatus().equals(STATUS_SUCCESS)
                                        && !payee.getPayeeDataList().isEmpty()){
                                    accountPayees.setValue(payee);
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.e(TAG, e.getLocalizedMessage());
                                errorMessage.setValue(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {

                            }
                        })
        );
    }

    public void transferToAcc(TransferRequest transferRequest){
        disposable.add(
                mAccountApiService.transferToAcc(transferRequest)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<TransferResponse>() {
                            @Override
                            public void onNext(@NonNull TransferResponse transferResponse) {
                                if(transferResponse != null &&
                                        transferResponse.getStatus().equals(STATUS_SUCCESS)){
                                    isTransferSuccess.setValue(true);
                                }else {
                                    isTransferSuccess.setValue(false);
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.e(AccountViewModel.class.getName(), e.getLocalizedMessage());
                                errorMessage.setValue(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {

                            }
                        })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
        disposable.dispose();
    }
}
