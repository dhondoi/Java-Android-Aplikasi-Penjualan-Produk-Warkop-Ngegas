package com.dhondoi.nonaseblak.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.activity.OrderProcessActivity;
import com.dhondoi.nonaseblak.entity.Receipt;
import com.dhondoi.nonaseblak.util.DateUtil;

import java.text.ParseException;
import java.util.List;

public class OrderProcessAdapter extends RecyclerView.Adapter<OrderProcessAdapter.OrderProcessHolder> {

    private Context context;
    private List<Receipt> receipts;

    public OrderProcessAdapter(Context context, List<Receipt> receipts) {
        this.context = context;
        this.receipts = receipts;
    }

    @NonNull
    @Override
    public OrderProcessHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_order_process, parent, false);
        return new OrderProcessHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProcessHolder holder, int position) {
        Receipt receipt = receipts.get(position);
        holder.textViewName.setText(receipt.getName().toUpperCase());
        String date = receipt.getDate();
        try {
            date = DateUtil.toNormalFormatDateTime(receipt.getDate());
        } catch (ParseException e) {
            Log.e(getClass().getSimpleName(), "onBindViewHolder: ", e);
        }
        holder.textViewDate.setText(date);
        holder.itemView.setOnClickListener(v -> ((OrderProcessActivity)context).changeActivity(receipt));
    }

    @Override
    public int getItemCount() {
        return receipts.size();
    }

    public class OrderProcessHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView textViewDate;

        public OrderProcessHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDate = itemView.findViewById(R.id.textViewDate);
        }
    }
}
