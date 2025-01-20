package com.dhondoi.nonaseblak.repository;

import java.util.List;

interface Repository<T> {
    List<T> readData();

    long saveData(T type);

    long updateData(T type);
}
