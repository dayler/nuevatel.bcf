/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nuevatel.bcf.core.dao;

import com.nuevatel.bcf.core.entity.SQLQuery;
import com.nuevatel.bcf.core.domain.UnitRecord;
import com.nuevatel.bcf.core.entity.EUnit;
import com.nuevatel.common.ds.DataSourceManager;
import com.nuevatel.common.util.Pair;
import static com.nuevatel.common.util.Util.castAs;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author clvelarde
 */
public class UnitDAO implements DAO<Pair<String,Integer>,UnitRecord>{
    
    private DataSourceManager ds = DatabaseHelper.getBcfDatasource();

    @Override
    public void insert(UnitRecord unit) throws SQLException {
        if (unit == null) {
            return;
        }

        Connection conn = null;
        CallableStatement stm = null;
        
        try {
        conn = ds.getConnection();
        Date currentDate = new Date();
        stm = ds.makeStatement(conn, SQLQuery.insert_new_unit.query(),
                unit.getName(),
                unit.getRegexId(),
                currentDate,
                currentDate,
                null);
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
    public UnitRecord findById(Pair<String,Integer> key) throws SQLException {
        if (key == null) {
            return null;
        }

        Connection conn = null;
        CallableStatement stm = null;
        ResultSet rs = null;
        
        try {
        conn = ds.getConnection();
        Date currentDate = new Date();
        stm = ds.makeStatement(conn, SQLQuery.get_unit_by_name_and_regexId.query(),
                key.getFirst(),
                key.getSecond());
        rs = stm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            UnitRecord unitRec = new UnitRecord();
            unitRec.setName(rs.getString(EUnit.name.name()));
            unitRec.setRegexId(castAs(Integer.class, rs.getObject(EUnit.regex_id.name())));

            return unitRec;
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
    public void update(UnitRecord record) throws SQLException {
        //log
    }

    @Override
    public void delete(Pair<String, Integer> key) throws SQLException {
        if (key == null) {
            return;
        }

        Connection conn = null;
        CallableStatement stm = null;
        ResultSet rs = null;
        
        try {
            conn = ds.getConnection();
            Date currentDate = new Date();
            stm = ds.makeStatement(conn, SQLQuery.delete_unit.query(),
                    key.getFirst(),
                    key.getSecond());
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
