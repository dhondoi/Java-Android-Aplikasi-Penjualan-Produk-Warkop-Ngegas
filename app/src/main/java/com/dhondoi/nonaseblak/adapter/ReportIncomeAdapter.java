package com.dhondoi.nonaseblak.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.activity.ProductMenuActivity;
import com.dhondoi.nonaseblak.entity.Order;
import com.dhondoi.nonaseblak.entity.Product;
import com.dhondoi.nonaseblak.entity.ReportIncome;
import com.dhondoi.nonaseblak.util.CurrencyUtil;
import com.dhondoi.nonaseblak.util.DateUtil;

import java.util.List;

public class ReportIncomeAdapter extends RecyclerView.Adapter<ReportIncomeAdapter.ReportIncomeHolder> {

    private Context context;
    private List<ReportIncome> reportIncomes;

    public ReportIncomeAdapter(Context context, List<ReportIncome> reportIncomes) {
        this.context = context;
        this.reportIncomes = reportIncomes;
    }

    public void setReportIncomes(List<ReportIncome> reportIncomes) {
        this.reportIncomes = reportIncomes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReportIncomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_report_income, parent, false);
        return new ReportIncomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportIncomeHolder holder, int position) {
        ReportIncome reportIncome = reportIncomes.get(position);
        holder.textViewDate.setText(reportIncome.getDate().toUpperCase());
        holder.textViewName.setText(reportIncome.getName().toUpperCase());
        holder.textViewPrice.setText(CurrencyUtil.toCurrency(reportIncome.getPrice().intValue()));
        holder.textViewAmount.setText(String.valueOf(reportIncome.getAmount()));
        holder.textViewTotal.setText(CurrencyUtil.toCurrency(reportIncome.getTotal().intValue()));
    }

    @Override
    public int getItemCount() {
        return reportIncomes.size();
    }

    public class ReportIncomeHolder extends RecyclerView.ViewHolder {

        private TextView textViewDate, textViewName, textViewPrice, textViewAmount, textViewTotal;

        public ReportIncomeHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewTotal = itemView.findViewById(R.id.textViewTotal);
        }
    }
}
