package com.dhondoi.nonaseblak.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.adapter.TransactionOrderAdapter;
import com.dhondoi.nonaseblak.entity.Category;
import com.dhondoi.nonaseblak.entity.Order;
import com.dhondoi.nonaseblak.entity.OrderHistory;
import com.dhondoi.nonaseblak.entity.Product;
import com.dhondoi.nonaseblak.entity.Variant;
import com.dhondoi.nonaseblak.service.CategoryService;
import com.dhondoi.nonaseblak.service.NumberReceiptServiceImpl;
import com.dhondoi.nonaseblak.service.OrderHistoryServiceImpl;
import com.dhondoi.nonaseblak.service.ProductService;
import com.dhondoi.nonaseblak.service.ReceiptServiceImpl;
import com.dhondoi.nonaseblak.service.VariantServiceImpl;
import com.dhondoi.nonaseblak.util.CurrencyUtil;
import com.dhondoi.nonaseblak.util.DatabaseUtil;
import com.dhondoi.nonaseblak.util.DialogUtil;
import com.dhondoi.nonaseblak.util.IntegerCheckerUtil;
import com.dhondoi.nonaseblak.util.StringCheckerUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransactionOrderActivity extends BaseActivity {

    private TextView tvConsumerName, tvConsumerNumber, tvTotal;
    private Button btnChoiceProduct, btnSplitBill, btnCheckout;
    protected int count = 1;
    protected String name;
    private List<Product> products;
    private List<Category> categories;
    protected List<Order> orders;
    private List<Variant> variants;
    protected List<String> notes;
    private TransactionOrderAdapter transactionOrderAdapter;
    protected Map<Integer, List<Order>> ordersMap;
    private RecyclerView recyclerView;

    @Override
    protected void initButtons() {
        btnCheckout = findViewById(R.id.buttonCheckout);
        btnCheckout.setOnClickListener(v -> insertToDatabase());

        btnChoiceProduct = findViewById(R.id.buttonChoiceProduct);
        btnChoiceProduct.setOnClickListener(v -> showCategory());

        btnSplitBill = findViewById(R.id.buttonSplitBill);
        btnSplitBill.setText(String.format("%s%d", btnSplitBill.getText().toString(), count));
        btnSplitBill.setOnClickListener(v -> checkOrderSize());
    }

    private void showDialogChoiceVariant() {
        List<String> tempVariants = new LinkedList<>();
        String[] data = new String[variants.size()];
        for (int i = 0; i < variants.size(); i++) {
            data[i] = variants.get(i).getName().toUpperCase();
        }
        DialogUtil.showDialogCheckBox1Button(this, "Pilih Varian", data,
                (dialog, which, isChecked) -> {
                    if (isChecked)
                        tempVariants.add(data[which]);
                    else
                        tempVariants.remove(data[which]);
                }, (dialog, which) -> {
                    dialog.dismiss();
                    if (DialogInterface.BUTTON_POSITIVE == which) {
                        if (tempVariants.size() != 0) {
                            String selection = "";
                            for (String tempVariant : tempVariants) {
                                selection += tempVariant + ", ";
                            }

                            showDialogNotes(selection);
                        } else {

                            DialogUtil.showDialog1Button(this, "Varian Harap Pilih Salah Satu", null, (dialog1, which1) -> showDialogChoiceVariant());
                        }
//                        Toast.makeText(this, notes.get(0), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showDialogNotes(final String note) {
        EditText editText = new EditText(this);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        editText.setHint("Optional. Kosongkan Jika Tidak Ada Catatan.");
        DialogUtil.showDialog1Button(this, "Catatan", editText, (dialogInterface, i) -> {

            if (DialogInterface.BUTTON_POSITIVE == i && !StringCheckerUtil.isEmpty(name)) {
                String paramNote = note + editText.getText().toString();
                notes.add(paramNote);
                saveOrder();
            }
        });
    }

    private void saveOrder() {
        ordersMap.put(count++, orders);
//            for (Map.Entry<Integer, List<Order>> orderMap : ordersMap.entrySet()) {
//                Log.i(getClass().getSimpleName(), "saveOrder: " + orderMap);
//            }

        orders = new LinkedList<>();
        transactionOrderAdapter = new TransactionOrderAdapter(this, orders);
        recyclerView.setAdapter(transactionOrderAdapter);

        btnSplitBill.setText("SIMPAN PESANAN KE - " + count);
        tvConsumerNumber.setText(String.valueOf(count));
        tvTotal.setText("-");
        btnCheckout.setEnabled(true);
    }

    private void checkOrderSize() {
        Log.i(getClass().getSimpleName(), "saveOrder: " + orders.size());
        if (orders.size() > 0) {
            showDialogChoiceVariant();
        } else {
            DialogUtil.showDialog1Button(this, "Pesanan " + name + " Ke - " + count + " Kosong. Checkout Aja Klo Udah Selesai.");
        }
    }

    private void insertToDatabase() {
        if (orders.size() < 1) {
            try {

                long idReceipt = new ReceiptServiceImpl(this).save(name);

                for (Map.Entry<Integer, List<Order>> orderMap : ordersMap.entrySet()) {

                    long idNumberReceipt = new NumberReceiptServiceImpl(this).save((int) idReceipt, notes.get(orderMap.getKey() - 1));

                    List<Order> orders = orderMap.getValue();
                    for (Order order : orders) {
                        OrderHistory orderHistory = new OrderHistory((int) idNumberReceipt, order.getProduct().getId(), order.getQuantity(), order.getTotal());
                        new OrderHistoryServiceImpl(this).save(orderHistory);
                    }

                    // testing
//                Log.i(getClass().getSimpleName(), "insertToDatabase: SUCCESS");
//                List<OrderHistory> orderHistories = new OrderHistoryServiceImpl(this).getData();
//                for (OrderHistory orderHistory :
//                        orderHistories) {
//                    Log.i(getClass().getSimpleName(), "insertToDatabase: " + orderHistory);
//                }
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

    private void showCategory() {
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories);
        DialogUtil.showDialogList(this, "Pilih Kategori", adapter, (dialog, which) -> {
//            Toast.makeText(this, ""+categories.get(which), Toast.LENGTH_SHORT).show();
            Category category = categories.get(which);
            showProduct(getSortedProducts(category));
        });
    }

    private void showProduct(List<Product> sortedProducts) {
        if (sortedProducts.size() > 0) {
            ArrayAdapter<Product> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sortedProducts);
            DialogUtil.showDialogList(this, "Pilih Produk", adapter, (dialog, which) -> {
//            Toast.makeText(this, ""+categories.get(which), Toast.LENGTH_SHORT).show();
                showQuantity(sortedProducts.get(which));
            });
        }else {
            DialogUtil.showDialog1Button(this,"Tidak Ada Produk Dalam Kategori Tersebut.",null,(dialog, which) -> showCategory());
        }
    }

    private List<Product> getSortedProducts(Category category) {
        List<Product> tempProducts = new LinkedList<>();
        for (Product product : products) {
            if (product.getCategoryId() == category.getId())
                tempProducts.add(product);
        }
        return tempProducts;
    }

    private void showQuantity(Product product) {
        EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        DialogUtil.showDialog2Button(this, "Jumlah", editText, (dialogInterface, i) -> {

                    if (DialogInterface.BUTTON_POSITIVE == i) {
                        try {
                            Integer quantity = Integer.valueOf(editText.getText().toString());
                            IntegerCheckerUtil.checkLessThan1(quantity.longValue());

                            addToList(product, quantity);
                            tvTotal.setText(CurrencyUtil.toCurrency(getTotal().intValue()));
                            btnSplitBill.setEnabled(true);
                        } catch (Exception e) {

                            DialogUtil.showDialog1Button(this, "Jumlah Harap Diisi", null, (dialog, which) -> showQuantity(product));
                        }
                    }
                }
        );
    }

    private Long getTotal() {
        Long total = 0L;
        for (Order order : orders) {
            total += order.getTotal();
        }
        return total;
    }

    private void addToList(Product product, Integer quantity) {
        Long total = quantity * product.getPrice();
        Order order = new Order(product, quantity, total);
        for (Order order1 : orders) {
            if (order1.getProduct().getId().equals(order.getProduct().getId())) {
                order1.setQuantity(order1.getQuantity() + order.getQuantity());
                order1.setTotal(order1.getTotal() + order.getTotal());
                transactionOrderAdapter.notifyDataSetChanged();
                return;
            }
        }
        orders.add(order);
        transactionOrderAdapter.notifyDataSetChanged();
    }


    @Override
    protected void initViews() {

        name = getIntent().getStringExtra(DatabaseUtil.KEY_NAME);

        tvConsumerName = findViewById(R.id.textViewConsumerName);
        tvConsumerName.setText(name.toUpperCase());

        tvConsumerNumber = findViewById(R.id.textViewConsumerNumber);
        tvConsumerNumber.setText(String.valueOf(count));

        tvTotal = findViewById(R.id.textViewTotal);
    }

    @Override
    protected int initLayout() {

        categories = new CategoryService(this).getData();
        products = new ProductService(this).getData();
        variants = new VariantServiceImpl(this).getData();
        notes = new LinkedList<>();

        return R.layout.activity_transaction_order;
    }

    @Override
    protected void initLists() {
        orders = new LinkedList<>();
        ordersMap = new HashMap<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        transactionOrderAdapter = new TransactionOrderAdapter(this, orders);
        recyclerView.setAdapter(transactionOrderAdapter);
    }

    @Override
    protected void initNecessary() {

    }

    @Override
    public void onBackPressed() {
        DialogUtil.showDialog2Button(this, "Batalkan Pesanan?", null, (dialog, which) -> {
            if (DialogInterface.BUTTON_POSITIVE == which) {
                super.onBackPressed();
            }
        });
    }
}