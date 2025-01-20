package com.dhondoi.nonaseblak.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.entity.Order;
import com.dhondoi.nonaseblak.util.CurrencyUtil;

import java.util.List;

public class TransactionOrderAdapter extends RecyclerView.Adapter<TransactionOrderAdapter.TransactionOrderHolder> {


    private Context context;
    private List<Order> orders;

    public TransactionOrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public TransactionOrderAdapter.TransactionOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_transaction_order, parent, false);
        return new TransactionOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionOrderAdapter.TransactionOrderHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvName.setText(order.getProduct().getName().toUpperCase());
        holder.tvPrice.setText(CurrencyUtil.toCurrency(order.getProduct().getPrice().intValue()));
        holder.tvQuantity.setText(String.valueOf(order.getQuantity()));
        holder.tvPriceTotal.setText(CurrencyUtil.toCurrency(order.getTotal().intValue()));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class TransactionOrderHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvPrice, tvQuantity, tvPriceTotal;

        public TransactionOrderHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textViewName);
            tvPrice = itemView.findViewById(R.id.textViewPrice);
            tvQuantity = itemView.findViewById(R.id.textViewQuantity);
            tvPriceTotal = itemView.findViewById(R.id.textViewPriceTotal);
        }
    }
}
