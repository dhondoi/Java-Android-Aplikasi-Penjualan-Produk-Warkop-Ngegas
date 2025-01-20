package com.dhondoi.nonaseblak.service;

import android.content.Context;
import android.util.Log;

import com.dhondoi.nonaseblak.entity.Product;
import com.dhondoi.nonaseblak.repository.ProductRepository;
import com.dhondoi.nonaseblak.util.IntegerCheckerUtil;
import com.dhondoi.nonaseblak.util.StringCheckerUtil;

import java.util.LinkedList;
import java.util.List;

public class ProductService implements Service<Product> {

    private Context context;
    private ProductRepository productRepository;

    public ProductService(Context context) {
        this.context = context;
        this.productRepository = new ProductRepository(this.context);
    }

    @Override
    public List<Product> getData() {
        return productRepository.readData();
    }

    @Override
    public void add(Product product) throws Exception {
        Log.i("ProductService", "add: " + product);
        IntegerCheckerUtil.checkLessThan1(product.getCategoryId().longValue(), product.getPrice());
        StringCheckerUtil.checkedEmpty(product.getName());
        product.setName(product.getName().trim().toLowerCase());
        product.setDescription(product.getDescription().trim().toLowerCase());

        if (productRepository.saveData(product) < 1)
            throw new Exception("Terjadi kesalahan, hubungi programmer.");
    }

    @Override
    public void edit(Product product) throws Exception {
        IntegerCheckerUtil.checkLessThan1(product.getCategoryId().longValue(), product.getPrice());
        StringCheckerUtil.checkedEmpty(product.getName());
        product.setName(product.getName().trim().toLowerCase());
        product.setDescription(product.getDescription().trim().toLowerCase());

        productRepository.updateData(product);
    }

    public List<Product> getDataByCategory(Integer categoryId) {
        List<Product> products = getData();
        List<Product> tempProducts = new LinkedList<>();
        for (Product product : products) {
            if (product.getCategoryId().equals(categoryId))
                tempProducts.add(product);
        }
        return tempProducts;
    }
}
