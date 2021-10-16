package com.example.ocbctest.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ocbctest.OCBCTestApplication;
import com.example.ocbctest.R;
import com.example.ocbctest.adapter.TransactionAdapter;
import com.example.ocbctest.model.Balance;
import com.example.ocbctest.model.Transaction;
import com.example.ocbctest.viewmodel.AccountViewModel;

import java.util.ArrayList;
import java.util.Formatter;

public class AccountFragment extends Fragment {

    private TextView txtAccountBalance;
    private TextView txtLogout;
    private TextView txtTransfer;
    private RecyclerView transactionListView;
    private TransactionAdapter adapter;
    private AccountViewModel viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtAccountBalance = view.findViewById(R.id.txt_accBalance);
        txtLogout = view.findViewById(R.id.txt_logout);
        txtTransfer = view.findViewById(R.id.txt_transfer);
        transactionListView = view.findViewById(R.id.activity_container);
        adapter = new TransactionAdapter(new ArrayList<>());
        transactionListView.setLayoutManager(new LinearLayoutManager(getContext()));
        transactionListView.setAdapter(adapter);

        viewModel.getAccountBalance();
        viewModel.getTransactions();
        observeViewModel();
        setOnClickListener();
    }

    private void observeViewModel(){
        viewModel.accountbalance.observe(getViewLifecycleOwner(), new Observer<Balance>() {
            @Override
            public void onChanged(Balance balance) {
                if(balance != null){
                    Formatter decimalFormatter = new Formatter();
                    String value = decimalFormatter.format("%.2f", balance.getBalance()).toString();
                    String amount = String.format("SGD %s",value);
                    txtAccountBalance.setText(amount);
                }
//                viewModel.accountbalance.removeObservers(getViewLifecycleOwner());
            }
        });

        viewModel.accountTransaction.observe(getViewLifecycleOwner(), new Observer<Transaction>() {
            @Override
            public void onChanged(Transaction transaction) {
                if(transaction != null)
                    adapter.updateTransactionList(transaction.getTransactionDataList());
            }
        });
    }

    private void setOnClickListener(){
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OCBCTestApplication.setToken("");
                getParentFragmentManager().popBackStackImmediate();
            }
        });

        txtTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.layout_container, new TransferFragment());
                fragmentTransaction.addToBackStack(TransferFragment.class.getName());
                fragmentTransaction.commit();
            }
        });
    }
}