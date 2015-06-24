package com.nuevatel.bcf.core.dao;

import com.nuevatel.bcf.core.domain.WSIRecord;

import java.sql.SQLException;

/**
 * Created by asalazar on 6/23/15.
 */
public class WSIRecordDAO implements DAO<String, WSIRecord> {
    @Override
    public void insert(WSIRecord record) throws SQLException {

    }

    @Override
    public WSIRecord findById(String key) throws SQLException {
        return null;
    }

    @Override
    public void update(WSIRecord record) throws SQLException {

    }

    @Override
    public void delete(String key) throws SQLException {
        
    }
}
