package com.dhondoi.nonaseblak.service;

import com.dhondoi.nonaseblak.entity.Variant;

import java.util.List;

interface VariantService {
    List<Variant> getData();

    void save(String name) throws Exception;

    void edit(Integer id, String name) throws Exception;
}
