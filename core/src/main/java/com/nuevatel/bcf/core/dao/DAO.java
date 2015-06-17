package com.nuevatel.bcf.core.dao;

import java.sql.SQLException;

/**
 * Created by asalazar on 6/17/15.
 */
public interface DAO <K, T> {

    /**
     *
     * @param record Record to insert in the database.
     */
    void insert(T record) throws SQLException;

    /**
     *
     * @param key Primary key, used to find the record.
     * @return The record to corresponds with K:key
     */
    T findById(K key);

    /**
     *
     * @param record Record to update in the database.
     */
    void update(T record);
}
