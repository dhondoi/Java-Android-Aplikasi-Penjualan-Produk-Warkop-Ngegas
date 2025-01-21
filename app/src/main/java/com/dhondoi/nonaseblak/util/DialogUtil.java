package com.dhondoi.nonaseblak.util;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ListAdapter;

import com.dhondoi.nonaseblak.R;

import java.util.Calendar;

public class DialogUtil {
    public static void showDialog2Button(Context context, String title, View view, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.Theme_NonaSeblak_Dialog))
                .setCancelable(false)
                .setTitle(title)
                .setPositiveButton("YA", onClickListener)
                .setNegativeButton("TIDAK", onClickListener);

        if (view != null) {
            builder.setView(view);
        }
        builder.show();
    }

    public static void showDialog1ButtonCancelable(Context context, String title, View view, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.Theme_NonaSeblak_Dialog))
                .setCancelable(true)
                .setTitle(title)
                .setPositiveButton("OK", onClickListener);

        if (view != null) {
            builder.setView(view);
        }
        builder.show();
    }

    public static void showDialog1Button(Context context, String title, View view, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.Theme_NonaSeblak_Dialog))
                .setCancelable(false)
                .setTitle(title)
                .setPositiveButton("OK", onClickListener);

        if (view != null) {
            builder.setView(view);
        }
        builder.show();
    }

    public static void showDialog1Button(Context context, String title) {
        new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.Theme_NonaSeblak_Dialog))
                .setCancelable(false)
                .setTitle(title)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    public static void showDialogList(Context context, String title, ListAdapter adapter, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.Theme_NonaSeblak_Dialog))
                .setCancelable(false)
                .setTitle(title)
                .setAdapter(adapter, onClickListener)
                .show();
    }

    public static void showDialogList1Button(Context context, String title, ListAdapter adapter, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.Theme_NonaSeblak_Dialog))
                .setCancelable(false)
                .setTitle(title)
                .setAdapter(adapter, onClickListener)
                .setPositiveButton("BATAL", onClickListener)
                .show();
    }


    public static void showDialogCheckBox1Button(Context context, String title, String[] data, DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.Theme_NonaSeblak_Dialog))
                .setCancelable(true)
                .setTitle(title)
                .setPositiveButton("OK", onClickListener)
                .setMultiChoiceItems(data, null, onMultiChoiceClickListener)
                .show();
    }

    public static void showDialogDate(Context context, DatePickerDialog.OnDateSetListener onDateSetListener) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, onDateSetListener, year, month, day);
        datePickerDialog.show();
    }
}
