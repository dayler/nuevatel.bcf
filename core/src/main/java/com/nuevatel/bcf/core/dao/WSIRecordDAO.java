package com.nuevatel.bcf.core.dao;

import com.nuevatel.bcf.core.domain.WSIRecord;
import com.nuevatel.bcf.core.entity.SQLQuery;
import com.nuevatel.common.ds.DataSourceManager;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by asalazar on 6/23/15.
 */
public class WSIRecordDAO implements DAO<String, WSIRecord> {
    
    private static Logger logger = LogManager.getLogger(WSIRecordDAO.class);
    
    private DataSourceManager ds = DatabaseHelper.getRecordDatasource();
    
    @Override
    public void insert(WSIRecord wsiRec) throws SQLException {
        logger.info("insert");
        if (wsiRec == null) {
            logger.info("not inserted, wsi record is null");
            return;
        }

        Connection conn = null;
        CallableStatement stm = null;

        try {
            conn = ds.getConnection();
            stm = ds.makeStatement(conn, SQLQuery.insert_new_wsi_record.query(),
                            wsiRec.getName(),
                            wsiRec.getRegex_id(),
                            wsiRec.getAction(),
                            wsiRec.getFromIpAddr(),
                            wsiRec.getResponse().value());
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
    public WSIRecord findById(String key) throws SQLException {
        logger.info("findById");
        logger.info("Not supported");
        return null;
    }

    @Override
    public void update(WSIRecord record) throws SQLException {
        logger.info("update");
        logger.info("Not supported");
    }

    @Override
    public void deleteByPK(String key) throws SQLException {
        logger.info("delete");

        if (key == null) {
            logger.info("Not find wsi record because key is null");
            return;
        }

        Connection conn = null;
        CallableStatement stm = null;

        try {
            conn = ds.getConnection();
            stm = ds.makeStatement(conn, SQLQuery.delete_wsi_record.query(), key);
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
}
