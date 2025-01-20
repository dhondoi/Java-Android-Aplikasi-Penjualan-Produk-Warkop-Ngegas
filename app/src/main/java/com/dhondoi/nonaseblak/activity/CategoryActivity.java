package com.dhondoi.nonaseblak.activity;


import android.content.DialogInterface;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.adapter.CategoryAdapter;
import com.dhondoi.nonaseblak.entity.Category;
import com.dhondoi.nonaseblak.service.CategoryService;
import com.dhondoi.nonaseblak.util.DialogUtil;

public class CategoryActivity extends BaseActivity {

    private CategoryService categoryService;

    @Override
    protected void initButtons() {

        findViewById(R.id.buttonAdd).setOnClickListener(view -> showDialogAdd());
    }

    public void showDialogAdd() {

        EditText editText = new EditText(this);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        DialogUtil.showDialog2Button(this, "Masukkan Nama Kategori", editText, (dialogInterface, i) -> {
            if (DialogInterface.BUTTON_POSITIVE == i) {

                String name = editText.getText().toString();
                save(name);
            }
        });
    }

    public void showDialogEdit(Category category) {

        EditText editText = new EditText(this);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        editText.setText(category.getName());
        DialogUtil.showDialog2Button(this, "Ubah " + category.getName() + " Menjadi", editText, (dialogInterface, i) -> {

            if (DialogInterface.BUTTON_POSITIVE == i) {

                String name = editText.getText().toString().trim();
                update(category, name);
            }
        });
    }

    private void update(Category category, String name) {
        try {
            Integer id = category.getId();
            Category category1 = new Category(id, name);
            categoryService.edit(category1);
            DialogUtil.showDialog1Button(this, "Sukses Mengubah Data Category", null, (dialogInterface1, i1) -> initLists());
        } catch (Exception e) {
            DialogUtil.showDialog1Button(this, e.getMessage(), null, (dialog, which) -> showDialogEdit(category));
        }
    }

    private void save(String name) {
        try {
            categoryService.add(new Category(null, name));
            DialogUtil.showDialog1Button(this, "Sukses Menambah Data Category", null, (dialogInterface1, i1) -> initLists());
        } catch (Exception e) {
            DialogUtil.showDialog1Button(this, e.getMessage(), null, (dialog, which) -> showDialogAdd());
        }
    }

    @Override
    protected int initLayout() {

        return R.layout.activity_category;
    }

    @Override
    public void initLists() {

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, categoryService.getData());
        recyclerView.setAdapter(categoryAdapter);
    }

    @Override
    protected void initViews() {
        categoryService = new CategoryService(this);
    }
}