package com.dhondoi.nonaseblak.activity;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhondoi.nonaseblak.R;
import com.dhondoi.nonaseblak.adapter.ReportIncomeAdapter;
import com.dhondoi.nonaseblak.entity.ReportIncome;
import com.dhondoi.nonaseblak.service.ReportIncomeService;
import com.dhondoi.nonaseblak.util.DateUtil;
import com.dhondoi.nonaseblak.util.DialogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportIncomeActivity extends BaseActivity {

    private ReportIncomeService reportIncomeService;

    private TextView textViewTotalIncome;

    private EditText editTextStartDate, editTextEndDate;

    private Button buttonSearch;

    private ReportIncomeAdapter reportIncomeAdapter;
    private String startDate, endDate;

    private SimpleDateFormat sdf;

    @Override
    protected int initLayout() {
        return R.layout.activity_report_income;
    }

    @Override
    protected void initButtons() {

        buttonSearch = findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(v -> showListByDate());
    }

    @Override
    protected void initViews() {

        sdf = new SimpleDateFormat("yyyy-MM-dd", new Locale("id", "ID"));

        textViewTotalIncome = findViewById(R.id.textViewTotalIncome);

        editTextStartDate = findViewById(R.id.editTextStartDate);
        editTextStartDate.setOnClickListener(v -> DialogUtil.showDialogDate(this, (view, year, month, dayOfMonth) -> {
            String date = year + "-" + (month + 1) + "-" + dayOfMonth;
            startDate = year + "-" + (month + 1) + "-" + (dayOfMonth - 1);
            try {
                Date parseEndDate = sdf.parse(startDate);
                startDate = sdf.format(parseEndDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            editTextStartDate.setText(date);
        }));

        editTextEndDate = findViewById(R.id.editTextEndDate);
        editTextEndDate.setOnClickListener(v -> DialogUtil.showDialogDate(this, (view, year, month, dayOfMonth) -> {
            String date = year + "-" + (month + 1) + "-" + dayOfMonth;
            endDate = year + "-" + (month + 1) + "-" + (dayOfMonth + 1);
            try {
                Date parseEndDate = sdf.parse(endDate);
                endDate = sdf.format(parseEndDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            editTextEndDate.setText(date);
        }));
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_MONTH, 1);  // menambah 1 hari
        Date tomorrow = calendar.getTime();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = calendar.getTime();
        startDate = sdf.format(yesterday);
        endDate = sdf.format(tomorrow);
        String nowDate = sdf.format(today);
        System.out.println(startDate);
        System.out.println(endDate);
        editTextStartDate.setText(nowDate);
        editTextEndDate.setText(nowDate);
    }

    @Override
    protected void initLists() {
        // set up list report income
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        reportIncomeService = new ReportIncomeService(this);
        List<ReportIncome> reportIncomes = reportIncomeService.getListReportIncomeByDate(startDate, endDate);
//        List<ReportIncome> reportIncomes = reportIncomeService.getListReportIncome();
        reportIncomeAdapter = new ReportIncomeAdapter(this, reportIncomes);
        recyclerView.setAdapter(reportIncomeAdapter);

        textViewTotalIncome.setText(reportIncomeService.getTotal(reportIncomes));
    }

    private void showListByDate() {
        List<ReportIncome> reportIncomes = reportIncomeService.getListReportIncomeByDate(startDate, endDate);
        reportIncomeAdapter.setReportIncomes(reportIncomes);

        textViewTotalIncome.setText(reportIncomeService.getTotal(reportIncomes));
    }


}