package com.dhondoi.nonaseblak.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.entity.NumberReceipt;
import com.dhondoi.nonaseblak.entity.Order;
import com.dhondoi.nonaseblak.entity.OrderHistory;
import com.dhondoi.nonaseblak.entity.Product;
import com.dhondoi.nonaseblak.service.OrderHistoryServiceImpl;
import com.dhondoi.nonaseblak.service.ProductService;

import java.util.LinkedList;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailHolder> {

    private Context context;
    private List<NumberReceipt> numberReceipts;
    private List<OrderHistory> orderHistories;

    private List<Product> products;


    public OrderDetailAdapter(Context context, List<NumberReceipt> numberReceipts) {
        this.context = context;
        this.numberReceipts = numberReceipts;
        this.orderHistories = new OrderHistoryServiceImpl(this.context).getData();
        this.products = new ProductService(this.context).getData();
    }

    @NonNull
    @Override
    public OrderDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_order_detail, parent, false);
        return new OrderDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailHolder holder, int position) {

        NumberReceipt numberReceipt = numberReceipts.get(position);
        holder.tvCountOrder.setText(String.valueOf(position + 1));
        holder.tvNote.setText(numberReceipt.getNote().toUpperCase());

        List<OrderHistory> sortedOrderHistory = getSortedOrderHistory(numberReceipt.getId());
        List<Order> orders = getProductOrder(sortedOrderHistory);
        OrderDetailAdapter1 orderDetailAdapter1 = new OrderDetailAdapter1(context, orders);
        holder.recyclerView.setAdapter(orderDetailAdapter1);
    }

    private List<Order> getProductOrder(List<OrderHistory> sortedOrderHistory) {
        List<Order> orders = new LinkedList<>();
        for (OrderHistory orderHistory : sortedOrderHistory) {
            for (Product product : products) {
                if (orderHistory.getProductId().equals(product.getId())) {
                    orders.add(new Order(product, orderHistory.getQuantity(), orderHistory.getTotal()));
                }
            }
        }

        return orders;
    }

    private List<OrderHistory> getSortedOrderHistory(Integer id) {
        List<OrderHistory> orderHistories1 = new LinkedList<>();
        for (OrderHistory orderHistory : orderHistories) {
            if (orderHistory.getNumberReceiptId().equals(id)) {
                orderHistories1.add(orderHistory);
            }
        }
        return orderHistories1;
    }


    @Override
    public int getItemCount() {
        return numberReceipts.size();
    }

    public class OrderDetailHolder extends RecyclerView.ViewHolder {

        private TextView tvCountOrder, tvNote;
        private RecyclerView recyclerView;

        public OrderDetailHolder(@NonNull View itemView) {
            super(itemView);
            tvCountOrder = itemView.findViewById(R.id.textViewCountOrder);
            tvNote = itemView.findViewById(R.id.textViewNote);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
    }
}
