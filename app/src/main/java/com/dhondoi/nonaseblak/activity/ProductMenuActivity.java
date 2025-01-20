package com.dhondoi.nonaseblak.activity;

import android.content.Intent;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.adapter.ProductAdapter;
import com.dhondoi.nonaseblak.entity.Product;
import com.dhondoi.nonaseblak.service.ProductService;
import com.dhondoi.nonaseblak.util.DatabaseUtil;

import java.util.List;

public class ProductMenuActivity extends BaseActivity {

    private ActivityResultLauncher<Intent> intentActivityResultLauncher;

    public void editProduct(Product product) {
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra(DatabaseUtil.KEY_ID, product.getId());
        intent.putExtra(DatabaseUtil.KEY_CATEGORY_ID, product.getCategoryId());
        intent.putExtra("nameVariant", getIntent().getStringExtra("nameVariant"));
        intent.putExtra(DatabaseUtil.KEY_NAME, product.getName());
        intent.putExtra(DatabaseUtil.KEY_PRICE, product.getPrice());
        intent.putExtra(DatabaseUtil.KEY_DESCRIPTION, product.getDescription());
        intentActivityResultLauncher.launch(intent);
    }

    @Override
    protected void initButtons() {
        findViewById(R.id.buttonAdd).setOnClickListener(view -> launchAddProductActivity());
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_product_menu;
    }

    @Override
    protected void initLists() {

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        Integer categoryId = getIntent().getIntExtra(DatabaseUtil.KEY_CATEGORY_ID, 0);
        List<Product> products = new ProductService(this).getDataByCategory(categoryId);
        ProductAdapter productAdapter = new ProductAdapter(this, products);
        recyclerView.setAdapter(productAdapter);
    }


    @Override
    protected void initNecessary() {
        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> initLists());
    }

    @Override
    protected void initViews() {
        TextView tvTitle = findViewById(R.id.textViewTitle);
        String title = tvTitle.getText() + " KATEGORI " + getIntent().getStringExtra("nameVariant").toUpperCase();
        tvTitle.setText(title);
    }

    private void launchAddProductActivity() {
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra(DatabaseUtil.KEY_CATEGORY_ID, getIntent().getIntExtra(DatabaseUtil.KEY_CATEGORY_ID, 0));
        intent.putExtra("nameVariant", getIntent().getStringExtra("nameVariant"));
        intentActivityResultLauncher.launch(intent);
    }
}