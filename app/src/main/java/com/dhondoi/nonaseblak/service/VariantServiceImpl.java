package com.dhondoi.nonaseblak.service;

import android.content.Context;

import com.dhondoi.nonaseblak.entity.Variant;
import com.dhondoi.nonaseblak.repository.VariantRepository;
import com.dhondoi.nonaseblak.util.StringCheckerUtil;

import java.util.List;

public class VariantServiceImpl implements VariantService {
    private Context context;
    private VariantRepository variantRepository;

    public VariantServiceImpl(Context context) {
        this.context = context;
        variantRepository = new VariantRepository(this.context);
    }

    @Override
    public List<Variant> getData() {

        return variantRepository.readData();
    }


    @Override
    public void save(String name) throws Exception {

        StringCheckerUtil.checkedEmpty(name);

        Variant variant = new Variant(null, name.trim().toLowerCase());

        if (variantRepository.saveData(variant) < 1)
            throw new Exception("Nama Varian Tidak Boleh Sama");

    }

    @Override
    public void edit(Integer id, String name) throws Exception {

        StringCheckerUtil.checkedEmpty(String.valueOf(id), name);

        Variant variant = new Variant(id, name.trim().toLowerCase());
//        variant.setName(variant.getName().trim().toLowerCase());

        if (variantRepository.updateData(variant) < 1)
            throw new Exception("Nama Varian Tidak Boleh Sama");

    }
}
