package com.dhondoi.nonaseblak.activity;

import android.util.Log;
import android.widget.Toast;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.entity.Order;
import com.dhondoi.nonaseblak.entity.OrderHistory;
import com.dhondoi.nonaseblak.service.NumberReceiptServiceImpl;
import com.dhondoi.nonaseblak.service.OrderHistoryServiceImpl;
import com.dhondoi.nonaseblak.service.ReceiptServiceImpl;
import com.dhondoi.nonaseblak.util.DatabaseUtil;
import com.dhondoi.nonaseblak.util.DialogUtil;

import java.util.List;
import java.util.Map;

public class OrderAddActivity extends TransactionOrderActivity {

    @Override
    protected void initButtons() {
        super.initButtons();
        findViewById(R.id.buttonCheckout).setOnClickListener(v -> updateOrder());
    }

    private void updateOrder() {
        if (orders.size() < 1) {
            try {

//                long idReceipt = new ReceiptServiceImpl(this).save(name);
                Integer idReceipt = getIntent().getIntExtra(DatabaseUtil.KEY_ID, 0);

                for (Map.Entry<Integer, List<Order>> orderMap : ordersMap.entrySet()) {

                    long idNumberReceipt = new NumberReceiptServiceImpl(this).save(idReceipt, notes.get(orderMap.getKey() - 1));

                    List<Order> orders = orderMap.getValue();
                    for (Order order : orders) {
                        OrderHistory orderHistory = new OrderHistory((int) idNumberReceipt, order.getProduct().getId(), order.getQuantity(), order.getTotal());
                        new OrderHistoryServiceImpl(this).save(orderHistory);
                    }

                }

                DialogUtil.showDialog1Button(this, "Pesanan Berhasil Tersimpan, Yuk Masak. :)", null, (dialog, which) ->
                        {
                            setResult(RESULT_OK);
                            finish();
                        }
                );
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "insertToDatabase: " + e);
                DialogUtil.showDialog1Button(this, "Terjadi Kesalahan! Hubungi Programmer.");
            }
        } else {
            DialogUtil.showDialog1Button(this, "Pesanan " + name + " Ke - " + count + " Belum Tersimpan");
        }
    }
}
