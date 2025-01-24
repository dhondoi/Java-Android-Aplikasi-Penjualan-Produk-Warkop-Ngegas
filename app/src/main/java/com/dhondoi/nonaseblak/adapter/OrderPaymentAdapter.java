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
import com.dhondoi.nonaseblak.activity.OrderPaymentActivity;
import com.dhondoi.nonaseblak.entity.NumberReceipt;
import com.dhondoi.nonaseblak.entity.Order;
import com.dhondoi.nonaseblak.entity.OrderHistory;
import com.dhondoi.nonaseblak.entity.Product;
import com.dhondoi.nonaseblak.service.OrderHistoryServiceImpl;
import com.dhondoi.nonaseblak.service.ProductService;
import com.dhondoi.nonaseblak.util.CurrencyUtil;

import java.util.LinkedList;
import java.util.List;

public class OrderPaymentAdapter extends RecyclerView.Adapter<OrderPaymentAdapter.OrderPaymentHolder> {

    private Context context;
    private List<NumberReceipt> numberReceipts;
    private List<OrderHistory> orderHistories;

    private List<Product> products;


    public OrderPaymentAdapter(Context context, List<NumberReceipt> numberReceipts) {
        this.context = context;
        this.numberReceipts = numberReceipts;
        this.orderHistories = new OrderHistoryServiceImpl(this.context).getDataByListNumberReceipt(this.numberReceipts);
        this.products = new ProductService(this.context).getData();
    }

    public List<OrderHistory> getOrderHistories() {
        return orderHistories;
    }

    public List<Product> getProducts() {
        return products;
    }

    @NonNull
    @Override
    public OrderPaymentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_order_payment, parent, false);
        return new OrderPaymentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderPaymentHolder holder, int position) {
        holder.tvCountOrder.setText(String.valueOf(position + 1));
        NumberReceipt numberReceipt = numberReceipts.get(position);
        List<OrderHistory> sortedOrderHistory = getSortedOrderHistory(numberReceipt.getId());
        List<Order> orders = getProductOrder(sortedOrderHistory);
        Long total = getPriceTotal(orders);
        holder.tvPriceTotal.setText(CurrencyUtil.toCurrency(total.intValue()));
        TransactionOrderAdapter orderPaymentAdapter = new TransactionOrderAdapter(context, orders);
        holder.recyclerView.setAdapter(orderPaymentAdapter);
    }

    public long getPriceTotal(List<Order> orders) {
        long price = 0;
        for (Order order : orders) {
            price += order.getTotal();
        }
        return price;
    }

    public List<Order> getProductOrder(List<OrderHistory> sortedOrderHistory) {
        List<Order> orders = new LinkedList<>();
        for (OrderHistory orderHistory : sortedOrderHistory) {
            for (Product product : products) {
                if (orderHistory.getProductId().equals(product.getId())) {
                    product.setName(orderHistory.getProductName());
                    orders.add(new Order(product, orderHistory.getQuantity(), orderHistory.getTotal()));
                    break;
                }
            }
        }

        return orders;
    }

    public List<OrderHistory> getSortedOrderHistory(Integer id) {
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

    public class OrderPaymentHolder extends RecyclerView.ViewHolder {

        private TextView tvCountOrder, tvPriceTotal;
        private RecyclerView recyclerView;

        public OrderPaymentHolder(@NonNull View itemView) {
            super(itemView);
            tvCountOrder = itemView.findViewById(R.id.textViewCountOrder);
            tvPriceTotal = itemView.findViewById(R.id.textViewPriceTotal);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
    }
}
