package com.dhondoi.nonaseblak.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.activity.OrderActivity;
import com.dhondoi.nonaseblak.entity.Order;
import com.dhondoi.nonaseblak.entity.Product;
import com.dhondoi.nonaseblak.util.CurrencyUtil;

import java.util.List;

public class OrderForOrderAdapter extends RecyclerView.Adapter<OrderForOrderAdapter.OrderForOrderHolder> {

    private Context context;
    private List<Order> orders;

    public OrderForOrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderForOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_order_list_order, parent, false);
        return new OrderForOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderForOrderHolder holder, int position) {
//        Toast.makeText(context, products.get(position).toString(), Toast.LENGTH_SHORT).show();
        Order order = orders.get(position);
        holder.textViewNameProduct.setText(order.getProduct().getName().toUpperCase());
        String price = CurrencyUtil.toCurrency(order.getProduct().getPrice().intValue());
        holder.textViewPriceProduct.setText(price);
        holder.textViewQuantity.setText(order.getQuantity().toString());
        String total = CurrencyUtil.toCurrency(order.getTotal().intValue());
        holder.textViewTotal.setText(total);
        holder.buttonDeleteOrder.setOnClickListener(v -> ((OrderActivity) context).removeFromOrderList(position));
        holder.imageButtonUp.setOnClickListener(v -> {
            if (order.getQuantity() >= 1) {
                int qty = order.getQuantity() + 1;
                ((OrderActivity) context).operateQuantity(order.getProduct(), position, qty);
            }
        });
        holder.imageButtonDown.setOnClickListener(v -> {
            if (order.getQuantity() > 1) {
                int qty = order.getQuantity() - 1;
                ((OrderActivity) context).operateQuantity(order.getProduct(), position, qty);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderForOrderHolder extends RecyclerView.ViewHolder {

        private TextView textViewNameProduct, textViewPriceProduct, textViewQuantity, textViewTotal;
        private Button buttonDeleteOrder;
        private ImageButton imageButtonUp, imageButtonDown;

        public OrderForOrderHolder(@NonNull View itemView) {
            super(itemView);
            textViewNameProduct = itemView.findViewById(R.id.textViewNameProduct);
            textViewPriceProduct = itemView.findViewById(R.id.textViewPriceProduct);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            textViewTotal = itemView.findViewById(R.id.textViewTotal);
            buttonDeleteOrder = itemView.findViewById(R.id.buttonDeleteOrder);
            imageButtonUp = itemView.findViewById(R.id.imageButtonUp);
            imageButtonDown = itemView.findViewById(R.id.imageButtonDown);
        }
    }
}
