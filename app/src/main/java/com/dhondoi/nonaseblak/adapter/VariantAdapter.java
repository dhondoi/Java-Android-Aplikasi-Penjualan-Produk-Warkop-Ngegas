package com.dhondoi.nonaseblak.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.activity.VariantActivity;
import com.dhondoi.nonaseblak.entity.Variant;

import java.util.List;

public class VariantAdapter extends RecyclerView.Adapter<VariantAdapter.VariantHolder> {

    private Context context;
    private List<Variant> variants;

    public VariantAdapter(Context context, List<Variant> variants) {
        this.context = context;
        this.variants = variants;
    }

    @NonNull
    @Override
    public VariantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_variant, parent, false);
        return new VariantHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VariantHolder categoryHolder, int position) {
        Variant variant = variants.get(position);
        categoryHolder.textViewName.setText(variant.getName().toUpperCase());
        categoryHolder.itemView.setOnClickListener(v -> {
//            Toast.makeText(context, category.getId() + " : " + category.getName(), Toast.LENGTH_LONG).show()
            ((VariantActivity) context).showDialogEdit(variant);
        });
    }

    @Override
    public int getItemCount() {
        return variants.size();
    }

    public class VariantHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;

        public VariantHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textView);
        }
    }
}
