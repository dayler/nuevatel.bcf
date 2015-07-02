package com.nuevatel.bcf.core.dao;

import com.nuevatel.bcf.core.domain.WSIRecord;
import com.nuevatel.bcf.core.entity.SQLQuery;
import com.nuevatel.common.ds.DataSourceManager;

import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.nuevatel.common.util.UniqueID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by asalazar on 6/23/15.
 */
public class WSIRecordDAO implements DAO<String, WSIRecord> {

    public static final int UNIQUEID_LENGTH = 16;

    private static Logger logger = LogManager.getLogger(WSIRecordDAO.class);
    
    private DataSourceManager ds = DatabaseHelper.getRecordDatasource();

    private UniqueID idGenerator = null;

    public WSIRecordDAO() {
        try {
            idGenerator = new UniqueID();
        } catch (NoSuchAlgorithmException ex) {
            logger.error("Failed to initialize idGenerator.", ex);
        }
    }

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
            stm = ds.makeStatement(conn, SQLQuery.insert_new_wsi_record_0.query(),
                            UniqueID.hexEncode(idGenerator.next(UNIQUEID_LENGTH)),
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
        logger.info("Not supported");
    }
}
