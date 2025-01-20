package com.dhondoi.nonaseblak.activity;


import android.content.DialogInterface;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.adapter.VariantAdapter;
import com.dhondoi.nonaseblak.entity.Variant;
import com.dhondoi.nonaseblak.service.VariantServiceImpl;
import com.dhondoi.nonaseblak.util.DialogUtil;

public class VariantActivity extends BaseActivity {

    private VariantServiceImpl variantService;

    @Override
    protected int initLayout() {

        return R.layout.activity_variant;
    }

    @Override
    public void initLists() {

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        VariantAdapter variantAdapter = new VariantAdapter(this, variantService.getData());
        recyclerView.setAdapter(variantAdapter);
    }

    @Override
    protected void initViews() {
        variantService = new VariantServiceImpl(this);
    }

    @Override
    protected void initButtons() {

        findViewById(R.id.buttonAdd).setOnClickListener(view -> showDialogAdd());
    }


    public void showDialogAdd() {

        EditText editText = new EditText(this);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        DialogUtil.showDialog2Button(this, "Masukkan Nama Varian", editText, (dialogInterface, i) -> {
            if (DialogInterface.BUTTON_POSITIVE == i) {

                String name = editText.getText().toString();
                save(name);
            }
        });
    }

    private void save(String name) {
        try {
            variantService.save(name);
            DialogUtil.showDialog1Button(this, "Sukses Menambah Data Varian", null, (dialogInterface1, i1) -> initLists());
        } catch (Exception e) {
            DialogUtil.showDialog1Button(this, e.getMessage(), null, (dialog, which) -> showDialogAdd());
        }
    }

    public void showDialogEdit(Variant variant) {

        EditText editText = new EditText(this);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        editText.setText(variant.getName());
        DialogUtil.showDialog2Button(this, "Ubah " + variant.getName() + " Menjadi", editText, (dialogInterface, i) -> {

            if (DialogInterface.BUTTON_POSITIVE == i) {

                String name = editText.getText().toString().trim();
                update(variant, name);
            }
        });
    }

    private void update(Variant variant, String name) {
        try {
            Integer id = variant.getId();
            variantService.edit(id, name);
            DialogUtil.showDialog1Button(this, "Sukses Mengubah Data Varian", null, (dialogInterface1, i1) -> initLists());
        } catch (Exception e) {
            DialogUtil.showDialog1Button(this, e.getMessage(), null, (dialog, which) -> showDialogEdit(variant));
        }
    }


}