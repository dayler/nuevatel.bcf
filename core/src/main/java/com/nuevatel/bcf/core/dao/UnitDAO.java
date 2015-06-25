package com.nuevatel.bcf.core.dao;

import com.nuevatel.bcf.core.domain.Unit;
import com.nuevatel.bcf.core.entity.EUnit;
import com.nuevatel.bcf.core.entity.SQLQuery;
import com.nuevatel.common.ds.DataSourceManager;
import com.nuevatel.common.util.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by asalazar on 6/24/15.
 */
public class UnitDAO implements DAO<String, Unit> {

    private static Logger logger = LogManager.getLogger(UnitDAO.class);

    private DataSourceManager ds = DatabaseHelper.getBcfDatasource();

    @Override
    public void insert(Unit unit) throws SQLException {
        if (unit == null) {
            return;
        }

        Connection conn = null;
        CallableStatement stm = null;

        try {
            Integer regexId = unit.getRegexIds().isEmpty() ? null : unit.getRegexIds().get(0);
            conn = ds.getConnection();
            stm = ds.makeStatement(conn, SQLQuery.insert_new_unit.query(),
                                   unit.getName(),
                                   regexId,
                                   unit.getStartTimestamp(regexId),
                                   unit.getEndTimestamp(regexId));
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
    public Unit findById(String name) throws SQLException {
        Parameters.checkNull(name, "name");
        Connection conn = null;
        CallableStatement stm = null;
        ResultSet rs = null;

        try {
            Unit unit = null;
            conn = ds.getConnection();
            stm = ds.makeStatement(conn, SQLQuery.select_unit_by_name.query(), name);
            rs = stm.executeQuery();
            while (rs.next()) {
                if (unit == null) {
                    unit = new Unit(name);
                }

                int regexId = rs.getInt(EUnit.regex_id.name());
                Date startTimestamp = rs.getDate(EUnit.start_timestamp.name());
                Date endTimestamp = rs.getDate(EUnit.end_timestamp.name());
                unit.addRegexId(regexId);
                unit.addTimespan(regexId, startTimestamp, endTimestamp);
            }

            return unit;
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
    public void update(Unit record) throws SQLException {
        logger.warn("No implemented");
    }

    @Override
    public void deleteByPK(String key) throws SQLException {
        logger.warn("No implemented");
    }

    public boolean existsUnitForNameAndRegexId(String name, Integer regexId) throws SQLException {
        if (name == null  || regexId == null) {
            return false;
        }

        Connection conn = null;
        CallableStatement stm = null;
        ResultSet rs = null;

        try {
            conn = ds.getConnection();
            stm = ds.makeStatement(conn, SQLQuery.exists_unit_for_name_regex_id.query(), name, regexId);
            rs = stm.executeQuery();
            return rs.next();
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

    public void deleteByNameAndRegexId(String name, Integer regexId) throws SQLException {
        if (name == null  || regexId == null) {
            return;
        }

        Connection conn = null;
        CallableStatement stm = null;
        try {
            conn = ds.getConnection();
            stm = ds.makeStatement(conn, SQLQuery.delete_unit_by_name_and_regex_id.query(), name, regexId);
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
