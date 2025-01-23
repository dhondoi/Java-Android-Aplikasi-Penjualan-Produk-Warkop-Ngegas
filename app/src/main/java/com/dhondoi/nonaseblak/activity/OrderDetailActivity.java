package com.dhondoi.nonaseblak.activity;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.adapter.OrderDetailAdapter;
import com.dhondoi.nonaseblak.entity.NumberReceipt;
import com.dhondoi.nonaseblak.service.NumberReceiptServiceImpl;
import com.dhondoi.nonaseblak.util.DatabaseUtil;

import java.util.LinkedList;
import java.util.List;

public class OrderDetailActivity extends BaseActivity {

    private Integer receiptId;
    private String receiptName;

    private ActivityResultLauncher<Intent> intentActivityResultLauncher;

    @Override
    protected int initLayout() {
        receiptId = getIntent().getIntExtra(DatabaseUtil.KEY_ID, 0);
        receiptName = getIntent().getStringExtra(DatabaseUtil.KEY_NAME);
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initButtons() {
        findViewById(R.id.buttonAdd).setOnClickListener(v -> launchOrderAddActivity());
    }

    @Override
    protected void initViews() {
        ((TextView) findViewById(R.id.textViewConsumerName)).setText(receiptName.toUpperCase());
    }

    @Override
    protected void initLists() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

//        List<NumberReceipt> numberReceipts = getNumberReceipt();
        List<NumberReceipt> numberReceipts = new NumberReceiptServiceImpl(this).getDataByIdReceipt(receiptId);
        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(this, numberReceipts);
        recyclerView.setAdapter(orderDetailAdapter);
    }

    @Override
    protected void initNecessary() {
        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> initLists());
    }

//    private List<NumberReceipt> getNumberReceipt() {
//        List<NumberReceipt> numberReceipts = new NumberReceiptServiceImpl(this).getData();
//        List<NumberReceipt> numberReceipts1 = new LinkedList<>();
//        for (NumberReceipt numberReceipt : numberReceipts) {
//            if (numberReceipt.getReceiptId().equals(receiptId))
//                numberReceipts1.add(numberReceipt);
//        }
//        Log.i(getClass().getSimpleName(), "getNumberReceipt: " + numberReceipts1.size());
//        return numberReceipts1;
//    }

    private void launchOrderAddActivity() {
        Intent intent = new Intent(this, OrderAddActivity.class);
        intent.putExtra(DatabaseUtil.KEY_NAME, receiptName);
        intent.putExtra(DatabaseUtil.KEY_ID, receiptId);
        intentActivityResultLauncher.launch(intent);
    }
}