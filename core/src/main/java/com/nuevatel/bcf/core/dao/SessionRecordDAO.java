package com.nuevatel.bcf.core.dao;

import com.nuevatel.bcf.core.domain.SessionRecord;
import com.nuevatel.bcf.core.entity.SQLQuery;
import com.nuevatel.common.ds.DataSourceManager;
import com.nuevatel.common.ds.DataSourceManagerImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by asalazar on 6/17/15.
 */
public class SessionRecordDAO implements DAO<String, SessionRecord> {

    private DataSourceManager ds = new DataSourceManagerImpl();

    @Override
    public void insert(SessionRecord record) throws SQLException {
        if (record == null) {
            return;
        }

        Connection conn = null;
        CallableStatement stm = null;

        try {
            conn = ds.getConnection();
            stm = ds.makeStatement(conn, SQLQuery.insert_new_session_record.query(),
                            record.getName(),
                            record.getRegexId(),
                            record.getNewMediaId(),
                            record.getEndMediaId(),
                            record.getSwapId(),
                            record.getStartTimestamp(),
                            record.getEndMediaId());
            stm.execute();
        } finally {
            if (conn != null) {
                conn.close();
            }

            if (stm != null) {
                stm.close();
            }
        }
    }

    @Override
    public SessionRecord findById(String key) {
        return null;
    }

    @Override
    public void update(SessionRecord record) {

    }

}
