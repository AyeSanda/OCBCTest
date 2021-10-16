package com.example.ocbctest.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ocbctest.R;
import com.example.ocbctest.model.TransactionData;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ItemViewHolder> {

    private List<TransactionData> mTransactionList;
    public TransactionAdapter(List<TransactionData> accountTransaction){
        this.mTransactionList = accountTransaction;
    }

    public void updateTransactionList(List<TransactionData> newTransactionList){
        if(!mTransactionList.isEmpty()){
            mTransactionList.clear();
        }
        mTransactionList.addAll(newTransactionList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item,parent,false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        TransactionData transactionData = mTransactionList.get(position);

        holder.txtDate.setText(formatDateString(transactionData.getDate()));

        String description = "";
        Formatter decimalFormatter = new Formatter();
        String value = "";
        if(transactionData.getType().equals("receive")){
            description = String.format("Received from %s",transactionData.getReceivedFrom().getAccountHolderName());
            value = decimalFormatter.format("%.2f", transactionData.getAmount()).toString();
            holder.txtAmount.setText(value);
            holder.txtAmount.setTextColor(Color.parseColor("#32CD32"));
        }else{
            description = String.format("Transfer to %s",transactionData.getTransferTo().getAccountHolderName());
            value = decimalFormatter.format("%.2f", transactionData.getAmount()).toString();
            holder.txtAmount.setText("-"+value);
            holder.txtAmount.setTextColor(Color.parseColor("#FF000000"));
        }
        holder.txtDescription.setText(description);

    }

    private String formatDateString(String dateIfo){
        //"2021-09-12T00:00:00.000Z"
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date date = dateFormat.parse(dateIfo);//You will get date object relative to server/client timezone wherever it is parsed
            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("dd MMM");
            return simpleDateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateIfo;
    }

    @Override
    public int getItemCount() {
        return mTransactionList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDate;
        private TextView txtDescription;
        private TextView txtAmount;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtDescription = itemView.findViewById(R.id.txt_description);
            txtAmount = itemView.findViewById(R.id.txt_amount);
        }
    }
}
