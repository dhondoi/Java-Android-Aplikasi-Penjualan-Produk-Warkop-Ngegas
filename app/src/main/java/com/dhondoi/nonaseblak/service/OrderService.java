package com.dhondoi.nonaseblak.service;

import android.content.Context;

import com.dhondoi.nonaseblak.entity.Category;
import com.dhondoi.nonaseblak.entity.Order;
import com.dhondoi.nonaseblak.entity.OrderHistory;
import com.dhondoi.nonaseblak.entity.Product;
import com.dhondoi.nonaseblak.entity.Variant;
import com.dhondoi.nonaseblak.repository.CategoryRepository;
import com.dhondoi.nonaseblak.repository.ProductRepository;
import com.dhondoi.nonaseblak.repository.VariantRepository;

import java.util.LinkedList;
import java.util.List;

public class OrderService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private VariantRepository variantRepository;
    private ReceiptServiceImpl receiptService;
    private NumberReceiptServiceImpl numberReceiptService;
    private OrderHistoryServiceImpl orderHistoryService;
    private List<Product> products;
    private List<Category> categories;
    private List<Order> orders;
    private List<Variant> variants;

    public OrderService(Context context) {

        this.productRepository = new ProductRepository(context);
        this.categoryRepository = new CategoryRepository(context);
        this.variantRepository = new VariantRepository(context);
        this.receiptService = new ReceiptServiceImpl(context);
        this.numberReceiptService = new NumberReceiptServiceImpl(context);
        this.orderHistoryService = new OrderHistoryServiceImpl(context);
    }

    public Integer saveOrderToDatabase(String customerName, String notes) throws Exception {

        getDataOrders();

        long idReceipt = receiptService.save(customerName);

        long idNumberReceipt = numberReceiptService.save((int) idReceipt, notes);

        for (Order order : orders) {
            OrderHistory orderHistory = new OrderHistory((int) idNumberReceipt, order.getProduct().getId(), order.getQuantity(), order.getTotal());
            orderHistoryService.save(orderHistory);
        }

        // testing
//        Log.i(getClass().getSimpleName(), "insertToDatabase: SUCCESS");
//        List<OrderHistory> orderHistories = orderHistoryService.getData();
//        for (OrderHistory orderHistory :
//                orderHistories) {
//            Log.i(getClass().getSimpleName(), "insertToDatabase: " + orderHistory);
//        }

        return (int) idReceipt;
    }

    public void saveOrderToDatabase(long idReceipt, String notes) throws Exception {
        getDataOrders();
        long idNumberReceipt = numberReceiptService.save((int) idReceipt, notes);
        for (Order order : orders) {
            OrderHistory orderHistory = new OrderHistory((int) idNumberReceipt, order.getProduct().getId(), order.getQuantity(), order.getTotal());
            orderHistoryService.save(orderHistory);
        }
    }

    public List<Product> getProductsByCategory(Integer categoryId) {
        getDataProducts();
        List<Product> tempProducts = new LinkedList<>();
        for (Product product : products) {
            if (product.getCategoryId().equals(categoryId))
                tempProducts.add(product);
        }
        return tempProducts;
    }

    public List<Product> getProductsBySearch(String textSearch) {
        getDataProducts();
        List<Product> tempProducts = new LinkedList<>();
        for (Product product : products) {
            if (product.getName().contains(textSearch))
                tempProducts.add(product);
        }
        return tempProducts;
    }

    public List<Category> getCategories() {
        getDataCategories();
        return categories;
    }

    public List<Product> getProducts() {
        getDataProducts();
        return products;
    }

    public List<Order> getOrders() {
        getDataOrders();
        return orders;
    }

    public List<Variant> getVariants() {
        getDataVariants();
        return variants;
    }

    public void addOrderList(Product product) {
        int inOrderList = orderHasInList(product);
        if (inOrderList == -1) {
            Integer quantity = 1;
            Long total = product.getPrice() * quantity;
            Order order = new Order(product, quantity, total);
            orders.add(order);
        } else {
            Order order1 = orders.get(inOrderList);
            Integer quantity = order1.getQuantity() + 1;
            Long total = order1.getProduct().getPrice() * quantity;
            Order order = new Order(product, quantity, total);
            orders.set(inOrderList, order);
        }
    }

    public void removeOrderFromList(int position) {
        orders.remove(position);
    }

    public void quantityOrderOperation(Product product, int position, int quantity) {
        Long total = product.getPrice() * quantity;
        Order order = new Order(product, quantity, total);
        orders.set(position, order);
    }

    private int orderHasInList(Product product) {
        for (int i = 0; i < orders.size(); i++) {
//            Log.i(getClass().getSimpleName(), orders.get(i).getProduct().getId() + " = " + product.getId());
            if (orders.get(i).getProduct().getId().equals(product.getId())) {
                return i;
            }
        }
        return -1;
    }

    private void getDataOrders() {
        if (orders == null || orders.isEmpty()) {
            orders = new LinkedList<>();
        }
    }

    private void getDataProducts() {
        if (products == null || products.isEmpty()) {
            products = productRepository.readData();
        }
    }

    private void getDataCategories() {
        if (categories == null || categories.isEmpty()) {
            categories = categoryRepository.readData();
        }
    }

    private void getDataVariants() {
        if (variants == null || variants.isEmpty()) {
            variants = variantRepository.readData();
        }
    }

}
