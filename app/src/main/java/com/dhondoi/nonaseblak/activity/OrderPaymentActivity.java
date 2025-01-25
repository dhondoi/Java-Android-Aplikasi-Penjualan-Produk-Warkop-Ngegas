package com.dhondoi.nonaseblak.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.adapter.OrderPaymentAdapter;
import com.dhondoi.nonaseblak.entity.NumberReceipt;
import com.dhondoi.nonaseblak.entity.Order;
import com.dhondoi.nonaseblak.entity.OrderHistory;
import com.dhondoi.nonaseblak.entity.Product;
import com.dhondoi.nonaseblak.service.NumberReceiptServiceImpl;
import com.dhondoi.nonaseblak.service.ReceiptServiceImpl;
import com.dhondoi.nonaseblak.util.BluetoothHelper;
import com.dhondoi.nonaseblak.util.CurrencyUtil;
import com.dhondoi.nonaseblak.util.DatabaseUtil;
import com.dhondoi.nonaseblak.util.DateUtil;
import com.dhondoi.nonaseblak.util.DialogUtil;

import java.util.LinkedList;
import java.util.List;

public class OrderPaymentActivity extends BaseActivity {

    private Integer receiptId;

    private String receiptName;
    private List<OrderHistory> orderHistories;
    private List<Product> products;
    private List<Order> orders;
    private int price;

    private BluetoothHelper bluetoothHelper;
    private List<NumberReceipt> numberReceipts;

    @Override
    protected int initLayout() {
        receiptId = getIntent().getIntExtra(DatabaseUtil.KEY_ID, 0);
        receiptName = getIntent().getStringExtra(DatabaseUtil.KEY_NAME);
        bluetoothHelper = new BluetoothHelper(this, this.getApplicationContext());
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
        numberReceipts = new NumberReceiptServiceImpl(this).getDataByIdReceipt(receiptId);
        OrderPaymentAdapter orderPaymentAdapter = new OrderPaymentAdapter(this, numberReceipts);
        recyclerView.setAdapter(orderPaymentAdapter);
        orderHistories = orderPaymentAdapter.getOrderHistories();
        products = orderPaymentAdapter.getProducts();
        setUpPrintData();
        // this will be notice
        ((TextView) findViewById(R.id.textViewTotal)).setText(CurrencyUtil.toCurrency(price));
    }

    private void setUpPrintData() {
        orders = new LinkedList<>();
        for (OrderHistory orderHistory : orderHistories) {
            for (Product product : products) {
                if (orderHistory.getProductId().equals(product.getId())) {
                    if (orders.isEmpty()) {
                        Product product1 = new Product(product.getId(), product.getCategoryId(), orderHistory.getProductName(), product.getPrice(), product.getName());
                        orders.add(new Order(product1, orderHistory.getQuantity(), orderHistory.getTotal()));
                    } else {
                        boolean dataAvailable = false;
                        int index = 0;
                        for (int i = 0; i < orders.size(); i++) {
                            Order order = orders.get(i);
                            if (order.getProduct().getName().equals(orderHistory.getProductName())) {
                                dataAvailable = true;
                                index = i;
                                break;
                            }
                        }
                        if (dataAvailable) {
                            Order order = orders.get(index);
                            order.setQuantity(order.getQuantity() + orderHistory.getQuantity());
                            order.setTotal(order.getTotal() + orderHistory.getTotal());
                            orders.set(index, order);
                        } else {
                            Product product1 = new Product(product.getId(), product.getCategoryId(), orderHistory.getProductName(), product.getPrice(), product.getName());
                            orders.add(new Order(product1, orderHistory.getQuantity(), orderHistory.getTotal()));
                        }
                    }
                    break;
                }
            }
        }
        getPriceTotal();
    }

    private String generateNoteForPrint() {
        StringBuilder stringBuilder = new StringBuilder();
        for (NumberReceipt numberReceipt : numberReceipts) {
            stringBuilder.append(numberReceipt.getNote()).append("\n");
        }
        return stringBuilder.toString();
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
//        DialogUtil.showDialog2Button(this, "Selesaikan Transaksi? Pastikan Pembeli Tidak Pesan Kembali.", null, (dialog, which) -> {
//            if (which == DialogInterface.BUTTON_POSITIVE)
//                updateToDatabase();
//        });
        String title = "Selesaikan Transaksi? Pastikan Pembeli Tidak Pesan Kembali.";
        DialogUtil.showDialog2ButtonWithMessage(this, title, getMessage(), (dialogInterface, i) -> {
            if (i == DialogInterface.BUTTON_POSITIVE)
                updateToDatabase();
        });
    }

    private String getMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Order order : orders) {
            String nameProduct = order.getProduct().getName().toUpperCase();
            if (nameProduct.length() > 16) {
                stringBuilder.append(nameProduct.substring(0, 16));
            } else {
                stringBuilder.append(nameProduct);
            }
            if (nameProduct.length() < 9) {
                stringBuilder.append("\t");
            }
            stringBuilder.append("\t").append(order.getQuantity().toString());
            stringBuilder.append("\t").append(CurrencyUtil.toCurrency(order.getTotal().intValue()));
            stringBuilder.append("\n");
        }
        stringBuilder.append("\nTOTAL \t\t ").append(CurrencyUtil.toCurrency(price));
        return stringBuilder.toString();
    }


    private void updateToDatabase() {
        long result = new ReceiptServiceImpl(this).edit(receiptId, ReceiptServiceImpl.FINISH);
        showDialogPrint("Cetak Resi?");
//        Log.i(getClass().getSimpleName(), "updateToDatabase: " + result);
//        finish();
    }

    private void showDialogPrint(String titleDialog) {
        DialogUtil.showDialog2Button(this, titleDialog, null, (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                print();
                showDialogPrint("Cetak Lagi?");
            } else {
                finish();
            }
        });
    }

    private void print() {
//        Toast.makeText(this, getPrintMessage(), Toast.LENGTH_LONG).show();
        Bitmap bitMapText = bluetoothHelper.messageToBitmap(getPrintMessage(), R.font.arialbd, 30F);
        boolean printSuccess = bluetoothHelper.printImage(bitMapText);
        if (printSuccess){
            bluetoothHelper = new BluetoothHelper(this, this.getApplicationContext());
            print();
        }
    }

    private String getPrintMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("-----------------------------------------------");
        stringBuilder.append("\n----------WARKOP NGEGAS---------");
        stringBuilder.append("\n-----------------------------------------------");
        stringBuilder.append("\nNama    : ").append(receiptName.toUpperCase());
        stringBuilder.append("\nTanggal : ").append(DateUtil.getStringDateNowForPrint());
        stringBuilder.append("\n-----------------------------------------------");
        for (Order order : orders) {
            stringBuilder.append("\n");
            String nameProduct = order.getProduct().getName().toUpperCase();
            if (nameProduct.length() > 16) {
                stringBuilder.append(nameProduct.substring(0, 16));
            } else {
                stringBuilder.append(nameProduct);
            }
            if (nameProduct.length() < 9) {
                stringBuilder.append("\t");
            }
            stringBuilder.append("\t").append(order.getQuantity().toString());
            stringBuilder.append(" ").append(CurrencyUtil.toCurrency(order.getTotal().intValue()));
        }
        stringBuilder.append("\n-----------------------------------------------");
        stringBuilder.append("\nTOTAL \t\t: ").append(CurrencyUtil.toCurrency(price));
        stringBuilder.append("\n-----------------------------------------------");
//        stringBuilder.append("\nCATATAN : ").append(generateNoteForPrint());
        stringBuilder.append("\n-----------------------------------------------");
        stringBuilder.append("\n------------TERIMA KASIH------------");
        stringBuilder.append("\n-----------------------------------------------");
        stringBuilder.append("\nInstagram : warkop_ngegas");
        stringBuilder.append("\nWhatsapp : 0838-6608-3415");
        stringBuilder.append("\nPassword WiFi : ").append(getPasswordWiFi());
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private String getPasswordWiFi() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        return sharedPreferences.getString("wifi", "-");
    }

    public void getPriceTotal() {
        price = 0;
        for (Order order : orders) {
            price += order.getTotal();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        bluetoothHelper.onRequestPermissionsResult(requestCode, grantResults);
    }
}