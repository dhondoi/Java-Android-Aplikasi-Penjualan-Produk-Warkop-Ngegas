package com.dhondoi.nonaseblak.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.activity.OrderActivity;
import com.dhondoi.nonaseblak.entity.Product;
import com.dhondoi.nonaseblak.util.CurrencyUtil;

import java.util.List;

public class ProductForOrderAdapter extends RecyclerView.Adapter<ProductForOrderAdapter.ProductForOrderHolder> {

    private Context context;
    private List<Product> products;

    public ProductForOrderAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductForOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_order_list_product, parent, false);
        return new ProductForOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductForOrderHolder holder, int position) {
//        Toast.makeText(context, products.get(position).toString(), Toast.LENGTH_SHORT).show();
        Product product = products.get(position);
        holder.textViewNameProduct.setText(product.getName().toUpperCase());
        String price = CurrencyUtil.toCurrency(product.getPrice().intValue());
        holder.textViewPriceProduct.setText(price);
        holder.itemView.setOnClickListener(v -> ((OrderActivity) context).addToOrderList(product));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductForOrderHolder extends RecyclerView.ViewHolder {

        private TextView textViewNameProduct, textViewPriceProduct;

        public ProductForOrderHolder(@NonNull View itemView) {
            super(itemView);
            textViewNameProduct = itemView.findViewById(R.id.textViewNameProduct);
            textViewPriceProduct = itemView.findViewById(R.id.textViewPriceProduct);
        }
    }
}
