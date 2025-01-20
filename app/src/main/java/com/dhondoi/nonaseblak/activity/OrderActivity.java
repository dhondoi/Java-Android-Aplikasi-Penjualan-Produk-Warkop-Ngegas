package com.dhondoi.nonaseblak.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.adapter.OrderForOrderAdapter;
import com.dhondoi.nonaseblak.adapter.ProductForOrderAdapter;
import com.dhondoi.nonaseblak.entity.Category;
import com.dhondoi.nonaseblak.entity.Order;
import com.dhondoi.nonaseblak.entity.Product;
import com.dhondoi.nonaseblak.entity.Variant;
import com.dhondoi.nonaseblak.service.OrderService;
import com.dhondoi.nonaseblak.service.ReceiptServiceImpl;
import com.dhondoi.nonaseblak.util.BluetoothHelper;
import com.dhondoi.nonaseblak.util.CurrencyUtil;
import com.dhondoi.nonaseblak.util.DatabaseUtil;
import com.dhondoi.nonaseblak.util.DateUtil;
import com.dhondoi.nonaseblak.util.DialogUtil;
import com.dhondoi.nonaseblak.util.StringCheckerUtil;

import java.util.LinkedList;
import java.util.List;

public class OrderActivity extends BaseActivity {

    private String customerName;
    private TextView textViewCustomerName, textViewTotal;
    private SearchView searchView;
    private Button buttonChoiceCategory, buttonCheckOut;
    private OrderService orderService;
    private ProductForOrderAdapter productAdapter;
    private OrderForOrderAdapter orderAdapter;

    private BluetoothHelper bluetoothHelper;

    private Integer receiptId;

    public void operateQuantity(Product product, int position, int quantity) {
        orderService.quantityOrderOperation(product, position, quantity);
        showOrders();
    }

    public void removeFromOrderList(int position) {
        orderService.removeOrderFromList(position);
        showOrders();
    }

    public void addToOrderList(Product product) {
        orderService.addOrderList(product);
        showOrders();
    }

    @Override
    protected int initLayout() {
        orderService = new OrderService(this);
        bluetoothHelper = new BluetoothHelper(this, this.getApplicationContext());
        return R.layout.activity_order;
    }

    @Override
    protected void initViews() {

        textViewCustomerName = findViewById(R.id.textViewConsumerName);
        customerName = getIntent().getStringExtra(DatabaseUtil.KEY_NAME);
        textViewCustomerName.setText(customerName.toUpperCase());

        textViewTotal = findViewById(R.id.textViewTotal);

        searchView = findViewById(R.id.searchView);
//        searchView.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                showProductsBySearch(newText);
                return false;
            }
        });
    }

    @Override
    protected void initButtons() {

        buttonChoiceCategory = findViewById(R.id.buttonChoiceCategory);
        buttonChoiceCategory.setOnClickListener(v -> showCategories());


        buttonCheckOut = findViewById(R.id.buttonCheckout);
        buttonCheckOut.setOnClickListener(v -> showDialogChoiceVariant());
    }


    @Override
    protected void initLists() {
        // set up lis product
        RecyclerView recyclerViewProduct = findViewById(R.id.recyclerViewProduct);
        recyclerViewProduct.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewProduct.setLayoutManager(linearLayoutManager);

        List<Product> products = orderService.getProducts();
        productAdapter = new ProductForOrderAdapter(this, products);
        recyclerViewProduct.setAdapter(productAdapter);

        // set up lis order
        RecyclerView recyclerViewOrder = findViewById(R.id.recyclerViewOrder);
        recyclerViewOrder.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        recyclerViewOrder.setLayoutManager(linearLayoutManager2);

        List<Order> orders = orderService.getOrders();
        orderAdapter = new OrderForOrderAdapter(this, orders);
        recyclerViewOrder.setAdapter(orderAdapter);
    }

    private void showCategories() {
        List<Category> categories = orderService.getCategories();
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories);
        DialogUtil.showDialogList1Button(this, "PILIH KATEGORI", adapter, (dialog, which) -> {
            if (which != DialogInterface.BUTTON_POSITIVE) {
                Category category = categories.get(which);
                buttonChoiceCategory.setText(category.getName().toUpperCase());
                showProducts(category.getId());
            }
        });
    }

    private void showProducts(Integer id) {
        List<Product> products = orderService.getProductsByCategory(id);
        productAdapter.setProducts(products);
    }

    private void printCheckout(String note) {
        List<Order> orders = orderService.getOrders();
        Long totalPriceOrder = getTotalPriceOrder(orders);
//        new BluetoothHelper(this, this.getApplicationContext()).printOrder(customerName.toUpperCase(), orders, totalPriceOrder, note);
        doPrint(customerName.toUpperCase(), orders, totalPriceOrder, note);
    }

    private void doPrint(String customerName, List<Order> orders, Long totalPriceOrder, String note) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("-----------------------------------------------");
        stringBuilder.append("\n----------WARKOP NGEGAS---------");
        stringBuilder.append("\n-----------------------------------------------");
        stringBuilder.append("\nNama    : ").append(customerName);
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
        stringBuilder.append("\nTOTAL \t\t: ").append(CurrencyUtil.toCurrency(totalPriceOrder.intValue()));
        stringBuilder.append("\n-----------------------------------------------");
        stringBuilder.append("\nCATATAN : ").append(note.toUpperCase());
        stringBuilder.append("\n-----------------------------------------------");
        stringBuilder.append("\n------------TERIMA KASIH------------");
        stringBuilder.append("\n-----------------------------------------------");
        stringBuilder.append("\nInstagram : warkop_ngegas");
        stringBuilder.append("\nWhatsapp : 0838-6608-3415");
        stringBuilder.append("\nPassword WiFi : ").append(getPasswordWiFi());
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        String message = stringBuilder.toString();
        Bitmap bitMapText = bluetoothHelper.messageToBitmap(message, R.font.arialbd, 30F);
        bluetoothHelper.printImage(bitMapText);
//        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
//        boolean isPrinted = bluetoothHelper.printImage(bitMapText);
//        bluetoothHelper.disconnectPrinter();
//            if (isPrinted) {
//                showDialogPrint("Cetak Lagi?", note);
//            } else {
//            }
    }

    private String getPasswordWiFi() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        return sharedPreferences.getString("wifi", "-");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        bluetoothHelper.onRequestPermissionsResult(requestCode, grantResults);
    }

    private void showOrders() {
        List<Order> orders = orderService.getOrders();
        orderAdapter.setOrders(orders);
        if (orders.size() > 0) {
            buttonCheckOut.setEnabled(true);
            Long total = getTotalPriceOrder(orders);
            textViewTotal.setText(CurrencyUtil.toCurrency(total.intValue()));
        } else {
            buttonCheckOut.setEnabled(false);
            textViewTotal.setText("-");
        }
    }

    private Long getTotalPriceOrder(List<Order> orders) {
        Long total = 0L;
        for (Order order : orders) {
            total += order.getTotal();
        }
        return total;
    }

    private void showDialogChoiceVariant() {
        List<Variant> variants = orderService.getVariants();
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

            if (DialogInterface.BUTTON_POSITIVE == i && !StringCheckerUtil.isEmpty(customerName)) {
                try {
                    String paramNote = note + editText.getText().toString();
                    receiptId = orderService.saveOrderToDatabase(customerName, paramNote);
                    showDialogPrint("Bayar Sekarang?", paramNote);
                } catch (Exception e) {
                    DialogUtil.showDialog1Button(this, "Terjadi Kesalahan! Hubungi Programmer.");
                }
            }
        });
    }

    private void showDialogPrint(String titleDialog, String paramNote) {
        DialogUtil.showDialog2Button(this, titleDialog, null, (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                printCheckout(paramNote);
                new ReceiptServiceImpl(this).edit(receiptId, ReceiptServiceImpl.FINISH);
//                orderService.finishPayment(idReceipt);
                showDialogPrint("Cetak Lagi?", paramNote);
            } else {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DialogUtil.showDialog2Button(this, "Batalkan Pesanan?", null, (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                super.onBackPressed();
            }
        });
    }

    private void showProductsBySearch(String textSearch) {
        List<Product> products = orderService.getProductsBySearch(textSearch);
        productAdapter.setProducts(products);
    }
}