package com.dhondoi.nonaseblak.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.util.DatabaseUtil;
import com.dhondoi.nonaseblak.util.DialogUtil;
import com.dhondoi.nonaseblak.util.StringCheckerUtil;

public class OrderMenuActivity extends BaseActivity {

    private ActivityResultLauncher<Intent> intentActivityResultLauncher;

    @Override
    protected void initButtons() {
        findViewById(R.id.buttonMakeOrder).setOnClickListener(v -> showDialogAdd());

        findViewById(R.id.buttonListOrder).setOnClickListener(v ->
                startActivity(new Intent(this, OrderProcessActivity.class).putExtra(DatabaseUtil.KEY_NAME, DatabaseUtil.KEY_NAME))
        );

        findViewById(R.id.buttonOrder).setOnClickListener(v ->
                startActivity(new Intent(this, OrderProcessActivity.class).putExtra(DatabaseUtil.KEY_ID, DatabaseUtil.KEY_ID))
        );
    }

    private void showDialogAdd() {
        EditText editText = new EditText(this);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        DialogUtil.showDialog2Button(this, "Masukkan Nama Pembeli", editText, (dialogInterface, i) -> {

            String name = editText.getText().toString();
            if (DialogInterface.BUTTON_POSITIVE == i && !StringCheckerUtil.isEmpty(name)) {

//                Intent intent = new Intent(this, TransactionOrderActivity.class);
                Intent intent = new Intent(this, OrderActivity.class);
                intent.putExtra(DatabaseUtil.KEY_NAME, name);
                intentActivityResultLauncher.launch(intent);
            }
        });
    }


    @Override
    protected void initNecessary() {
        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            if (result.getResultCode() == RESULT_OK) {
//                startActivity(new Intent(this, OrderProcessActivity.class));
//            }
        });
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_order_menu;
    }
}