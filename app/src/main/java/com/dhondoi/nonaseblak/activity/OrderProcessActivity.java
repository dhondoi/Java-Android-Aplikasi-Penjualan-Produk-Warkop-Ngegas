package com.dhondoi.nonaseblak.activity;

import android.content.Intent;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.adapter.OrderProcessAdapter;
import com.dhondoi.nonaseblak.entity.Receipt;
import com.dhondoi.nonaseblak.service.ReceiptServiceImpl;
import com.dhondoi.nonaseblak.util.DatabaseUtil;

import java.util.LinkedList;
import java.util.List;

public class OrderProcessActivity extends BaseActivity {

    private TextView tvTitle;

    public void changeActivity(Receipt receipt) {
        if (getIntent().hasExtra(DatabaseUtil.KEY_NAME)) {
            Intent intent = new Intent(this, OrderDetailActivity.class);
            intent.putExtra(DatabaseUtil.KEY_ID, receipt.getId());
            intent.putExtra(DatabaseUtil.KEY_NAME, receipt.getName());
            startActivity(intent);
        } else if (getIntent().hasExtra(DatabaseUtil.KEY_ID)) {
            Intent intent = new Intent(this, OrderPaymentActivity.class);
            intent.putExtra(DatabaseUtil.KEY_ID, receipt.getId());
            intent.putExtra(DatabaseUtil.KEY_NAME, receipt.getName());
            startActivity(intent);
            finish();
        }
    }

    public List<Receipt> getDataProcess() {
        List<Receipt> receipts = new ReceiptServiceImpl(this).getData();
        List<Receipt> receipts1 = new LinkedList<>();
        for (Receipt receipt : receipts) {
            if (receipt.getStatus().equals(ReceiptServiceImpl.START)) {
                receipts1.add(receipt);
            }
        }
        return receipts1;
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_order_process;
    }

    @Override
    protected void initButtons() {
        tvTitle = findViewById(R.id.textViewTitle);

        if (getIntent().hasExtra(DatabaseUtil.KEY_NAME)) {
            tvTitle.setText("PILIH PEMBELI UNTUK RINCIAN PESANAN");
        } else if (getIntent().hasExtra(DatabaseUtil.KEY_ID)) {
            tvTitle.setText("PILIH PEMBELI YANG MAU BAYAR");
        }
    }

    @Override
    protected void initLists() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<Receipt> receipts = getDataProcess();
        OrderProcessAdapter adapter = new OrderProcessAdapter(this, receipts);
        recyclerView.setAdapter(adapter);
    }

}