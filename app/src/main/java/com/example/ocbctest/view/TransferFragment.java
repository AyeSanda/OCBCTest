package com.example.ocbctest.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ocbctest.R;
import com.example.ocbctest.model.Payee;
import com.example.ocbctest.model.TransferRequest;
import com.example.ocbctest.viewmodel.AccountViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TransferFragment extends Fragment {

    private AutoCompleteTextView txtRecipient;
    private TextView txtDate;
    private EditText txtDescription;
    private EditText txtAmount;
    private TextView txtCancel;
    private TextView txtSubmit;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private ArrayAdapter adapter;
    private AccountViewModel viewModel;
    private String selectedDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtRecipient = view.findViewById(R.id.txt_recipient);
        txtDate = view.findViewById(R.id.txt_date);
        txtDescription = view.findViewById(R.id.txt_description);
        txtAmount = view.findViewById(R.id.txt_amount);
        txtCancel = view.findViewById(R.id.txt_cancel);
        txtSubmit = view.findViewById(R.id.txt_submit);

        viewModel.getPayees();
        observeViewModel();

        txtRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtRecipient.showDropDown();
            }
        });

        txtRecipient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Payee.PayeeData payeeInfo = (Payee.PayeeData)txtRecipient.getAdapter().getItem(position);
                txtRecipient.setText(payeeInfo.getAccountNo());
            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getContext(),
                        onDateSetListener,year,month,day);
                //which sets today's date as minimum date and all the past dates are disabled.
                //https://stackoverflow.com/questions/23762231/how-to-disable-past-dates-in-android-date-picker
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedDate = year+"/"+(month+1)+"/"+dayOfMonth;
                Log.e("dateString", selectedDate);
                try {
                    Date date=new SimpleDateFormat("yyyy/MM/dd").parse(selectedDate);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM YYYY");
                    txtDate.setText(simpleDateFormat.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

        txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferMoney();
            }
        });
    }

    private void observeViewModel() {
        viewModel.accountPayees.observe(getViewLifecycleOwner(), new Observer<Payee>() {
            @Override
            public void onChanged(Payee payee) {
                //Creating the instance of ArrayAdapter containing list of language names
                adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, payee.getPayeeDataList()) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                        TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                        Payee.PayeeData payeeInfo = payee.getPayeeDataList().get(position);
                        text1.setText(payeeInfo.getAccountHolderName());
                        text2.setText(payeeInfo.getAccountNo());
                        return view;
                    }
                };
                txtRecipient.setAdapter(adapter);
            }
        });
    }

    private void clear(){
        txtRecipient.setText("");
        txtDate.setText("");
        txtDescription.setText("");
        txtAmount.setText("");
    }

    private void transferMoney(){

        if(isValid()){
            TransferRequest request = new TransferRequest();
            request.setRecipientAccountNo(txtRecipient.getText().toString());
            request.setDate(formatDateString());
            request.setAmount(Double.valueOf(txtAmount.getText().toString()));
            request.setDescription(txtDescription.getText().toString());
            viewModel.transferToAcc(request);
            viewModel.isTransferSuccess.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isSuccess) {
                    if(isSuccess){
                        showAlertDialog(getContext(),"Transaction Successful");
                        clear();
                    }else {
                        showAlertDialog(getContext(),"Transaction Fail");
                    }
                }
            });

            viewModel.errorMessage.observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String errorMessage) {
                    showAlertDialog(getContext(),errorMessage);
                }
            });
        }else {
            showAlertDialog(getContext(),"Please fill all Info");
        }
    }

    public boolean isValid(){
        return !txtRecipient.getText().toString().trim().isEmpty()
                && !txtDate.getText().toString().trim().isEmpty()
                && !txtDescription.getText().toString().trim().isEmpty()
                && !txtAmount.getText().toString().trim().isEmpty();
    }

    private String formatDateString(){

        if(!selectedDate.isEmpty()){
            //"2021-09-12T00:00:00.000Z"
            try {
                Date date=new SimpleDateFormat("yyyy/MM/dd").parse(selectedDate);
                DateFormat simpleDateFormat =
                        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                return simpleDateFormat.format(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return txtDate.getText().toString();
    }

    public static void showAlertDialog(Context mContext, String message){
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