package com.dhondoi.nonaseblak.service;

import android.content.Context;


import com.dhondoi.nonaseblak.entity.Category;
import com.dhondoi.nonaseblak.repository.CategoryRepository;
import com.dhondoi.nonaseblak.util.StringCheckerUtil;

import java.util.List;

public class CategoryService implements Service<Category> {

    private Context context;
    private CategoryRepository categoryRepository;

    public CategoryService(Context context) {
        this.context = context;
        categoryRepository = new CategoryRepository(this.context);
    }

    @Override
    public List<Category> getData() {

        return categoryRepository.readData();
    }


    @Override
    public void edit(Category category) throws Exception {

        StringCheckerUtil.checkedEmpty(category.getName());

        category.setName(category.getName().trim().toLowerCase());
        try {
            if (categoryRepository.updateData(category) < 1)
                throw new Exception("Nama Kategori Tidak Boleh Sama");
        } catch (Exception e) {
            throw new Exception("Nama Kategori Tidak Boleh Sama");
        }
    }

    @Override
    public void add(Category category) throws Exception {

        StringCheckerUtil.checkedEmpty(category.getName());

        category.setName(category.getName().trim().toLowerCase());

        if (categoryRepository.saveData(category) < 1)
            throw new Exception("Nama Kategori Tidak Boleh Sama");

    }
}
