package com.example.ocbctest.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ocbctest.R;
import com.example.ocbctest.model.LoginRequest;
import com.example.ocbctest.viewmodel.AccountViewModel;

import io.reactivex.Observable;

public class LoginFragment extends Fragment {

    private TextView txtUserName;
    private TextView txtPassword;
    private Button btnLogin;
    private ProgressBar loadingView;
    private AccountViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtUserName = view.findViewById(R.id.txt_username);
        txtPassword = view.findViewById(R.id.txt_password);
        btnLogin = view.findViewById(R.id.btn_login);
        loadingView = view.findViewById(R.id.loading_view);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValid()){
                    loadingView.setVisibility(View.VISIBLE);
                    LoginRequest request = new LoginRequest(
                            txtUserName.getText().toString(),
                            txtPassword.getText().toString());

                    viewModel.generateAuthToken(request);

                    viewModel.loginStatus.observe(getActivity(), new Observer<String>() {
                        @Override
                        public void onChanged(String status) {

                            Log.e(LoginFragment.class.getName(), getViewLifecycleOwner().getLifecycle().getCurrentState()
                            +" "+status);
                            if(!status.isEmpty() && status.equals("success")){
                                clear();
                                FragmentManager fragmentManager = getParentFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.layout_container, new AccountFragment());
                                fragmentTransaction.addToBackStack(AccountFragment.class.getName());
                                fragmentTransaction.commit();

                            }else {
                                showAlertDialog(getContext(), status);
                                loadingView.setVisibility(View.GONE);
                            }
                        }

                    });
                    viewModel.loading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean isLoading) {
                            if(isLoading)
                                loadingView.setVisibility(View.VISIBLE);
                            else
                                loadingView.setVisibility(View.GONE);
                        }
                    });

                }else {
                    showAlertDialog(getContext(),"Please enter the correct \n" +
                            "username and password");
                }

            }
        });
    }

    private boolean isValid(){
        return !txtUserName.getText().toString().trim().isEmpty()
                && !txtPassword.getText().toString().trim().isEmpty();
    }

    private void clear(){
        txtUserName.setText("");
        txtPassword.setText("");
    }

    public void showAlertDialog(Context mContext, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}