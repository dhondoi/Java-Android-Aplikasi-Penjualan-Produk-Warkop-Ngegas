package com.dhondoi.nonaseblak.service;

import android.content.Context;
import android.util.Log;

import com.dhondoi.nonaseblak.entity.NumberReceipt;
import com.dhondoi.nonaseblak.entity.OrderHistory;
import com.dhondoi.nonaseblak.entity.Product;
import com.dhondoi.nonaseblak.entity.Receipt;
import com.dhondoi.nonaseblak.entity.ReportIncome;
import com.dhondoi.nonaseblak.util.CurrencyUtil;
import com.dhondoi.nonaseblak.util.DateUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ReportIncomeService {

    private List<ReportIncome> reportIncomes;
    private List<Product> products;
    private ReceiptServiceImpl receiptService;
    private NumberReceiptServiceImpl numberReceiptService;
    private OrderHistoryServiceImpl orderHistoryService;
    private ProductService productService;

    public ReportIncomeService(Context context) {
        receiptService = new ReceiptServiceImpl(context);
        numberReceiptService = new NumberReceiptServiceImpl(context);
        orderHistoryService = new OrderHistoryServiceImpl(context);
        productService = new ProductService(context);
    }

    public List<ReportIncome> getListReportIncome() {
        try {
            generateListReportIncome();
        } catch (ParseException e) {
            Log.e(getClass().getSimpleName(), "getListReportIncome: ", e);
        }
        return reportIncomes;
    }

    public String getTotal(List<ReportIncome> reportIncomes) {
        Long total = 0L;
        for (ReportIncome reportIncome : reportIncomes) {
            total += reportIncome.getTotal();
        }
        return CurrencyUtil.toCurrency(total.intValue());
    }

    public List<ReportIncome> getListReportIncomeByDate(String startDate, String endDate) {
        try {
            Log.i(getClass().getSimpleName(), startDate + "/" + endDate);
            generateListReportIncome(startDate, endDate);
        } catch (ParseException e) {
            Log.e(getClass().getSimpleName(), "getListReportIncome: ", e);
        }
        return reportIncomes;
    }


    //    public List<ReportIncome> getListReportIncomeByDate(String startDate, String endDate) {
//        List<ReportIncome> reportIncomes1 = new LinkedList<>();
//        for (ReportIncome reportIncome : reportIncomes) {
//            try {
//                Date dateStart = DateUtil.getDateFromDatePicker(startDate);
//                Date dateEnd = DateUtil.getDateFromDatePicker(endDate);
//                Date dateList = DateUtil.getDateForReport(reportIncome.getDate());
//                if (dateList.after(dateStart) && dateList.before(dateEnd)) {
//                    reportIncomes1.add(reportIncome);
//                }
//            } catch (ParseException e) {
//                Log.e(getClass().getSimpleName(), "getListReportIncomeByDate: ", e);
//            }
//        }
//        return reportIncomes1;
//    }
    private void generateListReportIncome(String startDate, String endDate) throws ParseException {
//        if (reportIncomes == null || reportIncomes.isEmpty()) {
        Log.i(getClass().getSimpleName(), "generateListReportIncome");
        reportIncomes = new ArrayList<>();
        products = productService.getData();
        List<Receipt> receipts = receiptService.getDataByDate(startDate, endDate);
        Log.i(getClass().getSimpleName(), ""+receipts.size());
        for (Receipt receipt : receipts) {
            Log.i(getClass().getSimpleName(), receipt.toString());
        }
        List<NumberReceipt> numberReceipts = numberReceiptService.getData();
        List<OrderHistory> orderHistories = orderHistoryService.getData();
        for (Receipt receipt : receipts) {
            String date = DateUtil.getStringDateForReport(receipt.getDate());
            for (NumberReceipt numberReceipt : numberReceipts) {
                if (numberReceipt.getReceiptId().equals(receipt.getId())) {
                    for (OrderHistory orderHistory : orderHistories) {
                        if (orderHistory.getNumberReceiptId().equals(numberReceipt.getId())) {
                            addToTheListReportIncome(date, orderHistory);
                        }
                    }
                    break;
                }
            }
        }
//        }
    }

    private void generateListReportIncome() throws ParseException {
        if (reportIncomes == null || reportIncomes.isEmpty()) {
            reportIncomes = new ArrayList<>();
            products = productService.getData();
            List<Receipt> receipts = receiptService.getData();
            List<NumberReceipt> numberReceipts = numberReceiptService.getData();
            List<OrderHistory> orderHistories = orderHistoryService.getData();
            for (Receipt receipt : receipts) {
                String date = DateUtil.getStringDateForReport(receipt.getDate());
                for (NumberReceipt numberReceipt : numberReceipts) {
                    if (numberReceipt.getReceiptId().equals(receipt.getId())) {
                        for (OrderHistory orderHistory : orderHistories) {
                            if (orderHistory.getNumberReceiptId().equals(numberReceipt.getId())) {
                                addToTheListReportIncome(date, orderHistory);
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    private void addToTheListReportIncome(String date, OrderHistory orderHistory) {
        Product product = getProductById(orderHistory.getProductId());
        if (product != null) {

            if (reportIncomes.size() > 0) {

                for (int i = 0; i < reportIncomes.size(); i++) {

                    if (reportIncomes.get(i).getName().equals(orderHistory.getProductName()) && reportIncomes.get(i).getDate().equals(date)) {

                        Integer amount = reportIncomes.get(i).getAmount() + orderHistory.getQuantity();
                        Long total = reportIncomes.get(i).getTotal() + orderHistory.getTotal();

                        reportIncomes.set(i, new ReportIncome(date, orderHistory.getProductName(), total / amount, amount, total));
                        return;
                    }
                }
                reportIncomes.add(new ReportIncome(date, orderHistory.getProductName(), orderHistory.getTotal() / orderHistory.getQuantity(), orderHistory.getQuantity(), orderHistory.getTotal()));
            } else {

                reportIncomes.add(new ReportIncome(date, orderHistory.getProductName(), orderHistory.getTotal() / orderHistory.getQuantity(), orderHistory.getQuantity(), orderHistory.getTotal()));
            }
        }
    }

    private Product getProductById(Integer productId) {
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }


}
