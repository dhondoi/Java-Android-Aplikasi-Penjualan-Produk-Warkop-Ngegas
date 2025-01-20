package com.dhondoi.nonaseblak.activity;


import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.entity.Category;
import com.dhondoi.nonaseblak.entity.Product;
import com.dhondoi.nonaseblak.service.CategoryService;
import com.dhondoi.nonaseblak.service.ProductService;
import com.dhondoi.nonaseblak.util.DatabaseUtil;
import com.dhondoi.nonaseblak.util.DialogUtil;

import java.util.LinkedList;
import java.util.List;

public class ProductActivity extends BaseActivity {

    private EditText editTextName, editTextPrice, editTextDescription;
    private TextView textViewTitle;
    private Button buttonAdd;

    @Override
    protected void initButtons() {

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(v -> save());
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_product;
    }

    @Override
    protected void initViews() {

        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextDescription = findViewById(R.id.editTextDescription);
        textViewTitle = findViewById(R.id.textViewTitle);

        String title = textViewTitle.getText().toString() + " KATEGORI " + getIntent().getStringExtra("nameVariant").toUpperCase();
        textViewTitle.setText(title);
    }

    @Override
    protected void initNecessary() {

        if (getIntent().hasExtra(DatabaseUtil.KEY_ID)) {

            String name = getIntent().getStringExtra(DatabaseUtil.KEY_NAME);
            long price = getIntent().getLongExtra(DatabaseUtil.KEY_PRICE, 0);
            String description = getIntent().getStringExtra(DatabaseUtil.KEY_DESCRIPTION);

            String title = "UBAH DATA PRODUK " + name.toUpperCase() + " KATEGORI " + getIntent().getStringExtra("nameVariant").toUpperCase();
            textViewTitle.setText(title);

            editTextName.setText(name);
            editTextPrice.setText(String.valueOf(price));
            editTextDescription.setText(description);

            buttonAdd.setOnClickListener(v -> update());
        }
    }

    private void update() {

        try {

            int id = getIntent().getIntExtra(DatabaseUtil.KEY_ID, 0);
            int categoryId = getIntent().getIntExtra(DatabaseUtil.KEY_CATEGORY_ID, 0);
            String name = editTextName.getText().toString();
            Long price = Long.valueOf(editTextPrice.getText().toString());
            String description = editTextDescription.getText().toString();

            Product product = new Product(id, categoryId, name, price, description);

            new ProductService(this).edit(product);

            DialogUtil.showDialog1Button(this, "Sukses Mengubah Data Produk", null, (dialog, which) -> finish());

        } catch (Exception e) {

            DialogUtil.showDialog1Button(this, "Tidak Boleh Kosong");
        }
    }

    private void save() {
        try {

            Integer categoryId = getIntent().getIntExtra(DatabaseUtil.KEY_CATEGORY_ID, 0);
            String name = editTextName.getText().toString();
            Long price = Long.valueOf(editTextPrice.getText().toString());
            String description = editTextDescription.getText().toString();

            Product product = new Product(null, categoryId, name, price, description);

            new ProductService(this).add(product);

            DialogUtil.showDialog1Button(this, "Sukses Menambah Data Produk", null, (dialog, which) -> finish());
        } catch (Exception e) {

            DialogUtil.showDialog1Button(this, "Tidak Boleh Kosong");
        }
    }

}