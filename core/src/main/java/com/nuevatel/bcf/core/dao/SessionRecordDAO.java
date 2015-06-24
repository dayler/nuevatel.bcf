package com.nuevatel.bcf.core.dao;

import com.nuevatel.bcf.core.domain.SessionRecord;
import com.nuevatel.bcf.core.entity.ESessionRecord;
import com.nuevatel.bcf.core.entity.SQLQuery;
import com.nuevatel.common.ds.DataSourceManager;
import com.nuevatel.common.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import static com.nuevatel.common.util.Util.*;
import java.sql.ResultSet;

/**
 * Created by asalazar on 6/17/15.
 */
public class SessionRecordDAO implements DAO<String, SessionRecord> {

    private DataSourceManager ds = DatabaseHelper.getRecordDatasource();

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
                            record.getEndMediaId(),
                            record.getRespCode());
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
    public SessionRecord findById(String key) throws SQLException {
        if (StringUtils.isBlank(key)) {
            return null;
        }

        Connection conn = null;
        CallableStatement stm = null;
        ResultSet rs = null;
        try{
            conn = ds.getConnection();
            stm = ds.makeStatement(conn, SQLQuery.get_session_record_by_id.query(), key);
            rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }

            SessionRecord record = new SessionRecord();
            record.setId(key);
            record.setName(rs.getString(ESessionRecord.name.name()));
            record.setRegexId(castAs(Integer.class, rs.getObject(ESessionRecord.regex_id.name())));
            record.setNewMediaId(castAs(Integer.class, rs.getObject(ESessionRecord.new_media_id.name())));
            record.setEndMediaId(castAs(Integer.class, rs.getObject(ESessionRecord.end_media_id.name())));
            record.setSwapId(castAs(Integer.class, rs.getObject(ESessionRecord.swap_id.name())));
            record.setStartTimestamp(rs.getDate(ESessionRecord.start_timestamp.name()));
            record.setEndTimestamp(rs.getDate(ESessionRecord.end_timestamp.name()));
            record.setRespCode(castAs(Integer.class, rs.getObject(ESessionRecord.resp_code.name())));
            return record;
        } finally {
            if (conn != null) {
                conn.close();
            }

            if (stm != null) {
                stm.close();
            }

            if (rs != null) {
                rs.close();
            }
        }
    }

    @Override
    public void update(SessionRecord record) throws SQLException {
        Connection conn = null;
        CallableStatement stm = null;

        try {
            conn = ds.getConnection();
            stm = ds.makeStatement(conn, SQLQuery.update_session_record.query(),
                                   record.getName(),
                                   record.getRegexId(),
                                   record.getNewMediaId(),
                                   record.getEndMediaId(),
                                   record.getSwapId(),
                                   record.getStartTimestamp(),
                                   record.getEndTimestamp(),
                                   record.getRespCode(),
                                   record.getId());
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

    public void updateEndTimestamp(String id, Date endTimestamp, Integer respCode) throws SQLException {
        Connection conn = null;
        CallableStatement stm = null;

        try {
            conn = ds.getConnection();
            stm = ds.makeStatement(conn, SQLQuery.update_session_record_end_timestamp.query(),
                                   endTimestamp, respCode, id);
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
    public void delete(String key) throws SQLException {
    //logs --> warning
    }
}
