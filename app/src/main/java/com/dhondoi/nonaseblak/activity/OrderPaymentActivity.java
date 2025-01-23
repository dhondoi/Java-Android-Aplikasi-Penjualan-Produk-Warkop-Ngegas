package com.dhondoi.nonaseblak.activity;

import android.content.DialogInterface;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.adapter.OrderPaymentAdapter;
import com.dhondoi.nonaseblak.entity.NumberReceipt;
import com.dhondoi.nonaseblak.entity.Order;
import com.dhondoi.nonaseblak.entity.OrderHistory;
import com.dhondoi.nonaseblak.service.NumberReceiptServiceImpl;
import com.dhondoi.nonaseblak.service.ReceiptServiceImpl;
import com.dhondoi.nonaseblak.util.CurrencyUtil;
import com.dhondoi.nonaseblak.util.DatabaseUtil;
import com.dhondoi.nonaseblak.util.DialogUtil;

import java.util.LinkedList;
import java.util.List;

// todo buat fitur print struk

public class OrderPaymentActivity extends BaseActivity {

    private Integer receiptId;

    private String receiptName;

    @Override
    protected int initLayout() {
        receiptId = getIntent().getIntExtra(DatabaseUtil.KEY_ID, 0);
        receiptName = getIntent().getStringExtra(DatabaseUtil.KEY_NAME);
        return R.layout.activity_order_payment;
    }

    @Override
    protected void initButtons() {
        findViewById(R.id.buttonFinish).setOnClickListener(v -> showQuestionDialog());
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
        OrderPaymentAdapter orderPaymentAdapter = new OrderPaymentAdapter(this, numberReceipts);
        recyclerView.setAdapter(orderPaymentAdapter);
        // this will be notice
        ((TextView) findViewById(R.id.textViewTotal)).setText(CurrencyUtil.toCurrency(getNetTotal(numberReceipts, orderPaymentAdapter).intValue()));
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

    private void showQuestionDialog() {
        DialogUtil.showDialog2Button(this, "Selesaikan Transaksi? Pastikan Pembeli Tidak Pesan Kembali.", null, (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE)
                updateToDatabase();
        });
    }

    private void updateToDatabase() {
        long result = new ReceiptServiceImpl(this).edit(receiptId, ReceiptServiceImpl.FINISH);
        Log.i(getClass().getSimpleName(), "updateToDatabase: " + result);
        finish();
    }

    private Long getNetTotal(List<NumberReceipt> numberReceipts, OrderPaymentAdapter orderPaymentAdapter) {

        Long netTotal = 0L;

        for (NumberReceipt numberReceipt : numberReceipts) {
            List<OrderHistory> sortedOrderHistory = orderPaymentAdapter.getSortedOrderHistory(numberReceipt.getId());
            List<Order> productOrder = orderPaymentAdapter.getProductOrder(sortedOrderHistory);
            Long priceTotal = orderPaymentAdapter.getPriceTotal(productOrder);
            netTotal += priceTotal;
        }
        return netTotal;
    }
}