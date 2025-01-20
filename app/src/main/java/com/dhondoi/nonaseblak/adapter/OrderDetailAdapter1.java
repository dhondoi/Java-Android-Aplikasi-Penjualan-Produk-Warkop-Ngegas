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

import java.util.List;

public class OrderDetailAdapter1 extends RecyclerView.Adapter<OrderDetailAdapter1.OrderDetailHolder1> {

    private Context context;

    private List<Order> orders;

    public OrderDetailAdapter1(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderDetailHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_order_detail1, parent, false);
        return new OrderDetailHolder1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailHolder1 holder, int position) {

        Order order = orders.get(position);

        holder.tvName.setText(order.getProduct().getName().toUpperCase());
        holder.tvQuantity.setText(String.valueOf(order.getQuantity()));
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderDetailHolder1 extends RecyclerView.ViewHolder {

        private TextView tvName, tvQuantity;


        public OrderDetailHolder1(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textViewName);
            tvQuantity = itemView.findViewById(R.id.textViewQuantity);
        }
    }
}
