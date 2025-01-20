package com.dhondoi.nonaseblak.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.entity.Category;
import com.dhondoi.nonaseblak.service.CategoryService;
import com.dhondoi.nonaseblak.util.DatabaseUtil;
import com.dhondoi.nonaseblak.util.DialogUtil;

import java.util.List;

public class MasterDataMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set file layout
        setContentView(R.layout.activity_master_data_menu);
        // set up button on click
        setButtonOnClick();
    }

    private void setButtonOnClick() {
        // go to Category Data management
        findViewById(R.id.btnCategory).setOnClickListener(view ->
                startActivity(new Intent(this, CategoryActivity.class))
        );
        // go to product management
        findViewById(R.id.btnProduct).setOnClickListener(view -> {
            List<Category> categories = new CategoryService(this).getData();
            if (categories.size() == 0)
                DialogUtil.showDialog1Button(this, "Harap Tambahkan Data Kategori");
            else {
                showCategory(categories);
            }
        });
        // go to variant management
        findViewById(R.id.btnVariant).setOnClickListener(view ->
                startActivity(new Intent(this, VariantActivity.class))
        );
    }

    private void showCategory(List<Category> categories) {
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories);
        DialogUtil.showDialogList(this, "Pilih Kategori", adapter, (dialog, which) -> {
            Category category = categories.get(which);
            Intent intent = new Intent(this, ProductMenuActivity.class);
            intent.putExtra(DatabaseUtil.KEY_CATEGORY_ID, category.getId());
            intent.putExtra("nameVariant", category.getName());
            startActivity(intent);
        });
    }
}
