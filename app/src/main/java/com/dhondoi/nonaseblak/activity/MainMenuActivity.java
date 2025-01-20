package com.dhondoi.nonaseblak.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.service.ProductService;
import com.dhondoi.nonaseblak.service.ReceiptServiceImpl;
import com.dhondoi.nonaseblak.service.VariantServiceImpl;
import com.dhondoi.nonaseblak.util.BluetoothHelper;
import com.dhondoi.nonaseblak.util.CurrencyUtil;
import com.dhondoi.nonaseblak.util.DatabaseUtil;
import com.dhondoi.nonaseblak.util.DateUtil;
import com.dhondoi.nonaseblak.util.DialogUtil;
import com.dhondoi.nonaseblak.util.StringCheckerUtil;

import java.util.LinkedList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    // todo add section about yourself
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set file layout
        setContentView(R.layout.activity_main_menu);
        // set up button on click
        setButtonOnClick();
    }

    private void setButtonOnClick() {
        // go to Master Data Menu Activity
        findViewById(R.id.buttonMaster).setOnClickListener(view -> {
            startActivity(new Intent(this, MasterDataMenuActivity.class));
        });
        // goto master order menu
        findViewById(R.id.buttonOrder).setOnClickListener(view -> {
            if (new ProductService(this).getData().size() > 0) {
                if (new VariantServiceImpl(this).getData().size() > 0)
//                    startActivity(new Intent(this, OrderMenuActivity.class));
                    // todo new order system v2
                    setNameCustomer();
                else
                    DialogUtil.showDialog1Button(this, "Data Varian Kosong");
            } else
                DialogUtil.showDialog1Button(this, "Data Produk Kosong");
        });
        // setting
        findViewById(R.id.imageButtonSetting).setOnClickListener(v -> showDialogSetting());
        // report
        findViewById(R.id.buttonOrderHistory).setOnClickListener(v -> startActivity(new Intent(this, ReportIncomeActivity.class)));
        // calculator app
        findViewById(R.id.buttonCalculator).setOnClickListener(v -> launchCalculatorApp());
    }

    private void launchCalculatorApp() {
        Intent intent = new Intent();
        intent.setClassName("com.sec.android.app.popupcalculator", "com.sec.android.app.popupcalculator.Calculator");
        startActivity(intent);
    }

    private void showDialogSetting() {
        List<String> settings = new LinkedList<>();
        settings.add("RESET PRINTER BLUETOOTH");
        settings.add("INFO APLIKASI");
        settings.add("BATAL");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, settings);
        DialogUtil.showDialogList(this, "Pilih Pengaturan", adapter, (dialog, which) -> {
            if (which == 0) {
                resetPrinter();
            } else if (which == 1) {
                startActivity(new Intent(this, InfoAppActivity.class));
            }
        });
    }

    private void resetPrinter() {
        new BluetoothHelper(this, this.getApplicationContext());
    }

    private void setNameCustomer() {
        EditText editText = new EditText(this);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        editText.setHint("Optional. Kosongkan Jika Tidak Ada.");
        DialogUtil.showDialog2Button(this, "Masukkan Nama Pembeli", editText, (dialogInterface, i) -> {

            String name = editText.getText().toString();
            if (DialogInterface.BUTTON_POSITIVE == i) {

                if (StringCheckerUtil.isEmpty(name)) {
                    int number = new ReceiptServiceImpl(this).getData().size() + 1;
                    name = "Pelanggan " + number;
                }

                Intent intent = new Intent(this, OrderActivity.class);
                intent.putExtra(DatabaseUtil.KEY_NAME, name);
                startActivity(intent);
            }
        });
//                    new ReceiptServiceImpl(this).getDataByDate();
    }


}