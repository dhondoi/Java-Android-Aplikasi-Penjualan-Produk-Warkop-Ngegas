package com.dhondoi.nonaseblak.service;

import java.util.List;

interface Service<T> {
    List<T> getData();

    void add(T type) throws Exception;

    void edit(T type) throws Exception;
}
