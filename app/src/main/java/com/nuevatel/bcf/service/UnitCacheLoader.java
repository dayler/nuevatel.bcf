package com.nuevatel.bcf.service;

import com.google.common.cache.CacheLoader;
import com.nuevatel.bcf.domain.Unit;
import com.nuevatel.bcf.entity.SQLQuery;
import com.nuevatel.bcf.entity.EUnit;
import com.nuevatel.bcf.exception.UnitNotFoundException;
import com.nuevatel.common.ds.DataSourceManager;
import com.nuevatel.common.ds.DataSourceManagerImpl;
import com.nuevatel.common.exception.OperationException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;

/**
 * Load single unit from the database based on its name.
 *
 * @author Ariel Salazar
 */
public class UnitCacheLoader extends CacheLoader<String, Unit> {

    /**
     *
     * @param name Unit name.
     * @return Return the unit to corresponds with the provided name.
     * @throws Exception If the Unit could not be retrieved.
     */
    @Override
    public Unit load(String name) throws Exception {
        DataSourceManager ds = new DataSourceManagerImpl();
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

            // If unit is null throws exception.
            if (unit == null) {
                throw new UnitNotFoundException(name);
            }

            return unit;
        } finally {
            if (conn != null) {
                conn.close();
            }

            if (rs != null) {
                rs.close();
            }

            if (stm != null) {
                stm.close();
            }
        }
    }
}
