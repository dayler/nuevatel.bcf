package com.nuevatel.bcf.core.dao;

import com.nuevatel.common.ds.DataSourceManager;
import com.nuevatel.common.ds.DataSourceManagerConfigurator;
import com.nuevatel.common.ds.JDBCProperties;

import java.sql.SQLException;

/**
 * Created by asalazar on 6/22/15.
 */
public class DatabaseHelper {

    private static DataSourceManager dsBCF = null;

    private static DataSourceManager dsRecord = null;

    public static void configure(JDBCProperties jdbcBcfProperties, JDBCProperties jdbcRecordProperties) throws SQLException,
                                                                                                                ClassNotFoundException {
        DataSourceManagerConfigurator configurator =  new DataSourceManagerConfigurator();
        dsBCF = configurator.configure(jdbcBcfProperties).build();
        dsRecord = configurator.configure(jdbcRecordProperties).build();
    }

    public synchronized static void shutdownConnPool() {
        if (dsBCF != null) {
            dsBCF.shutdownConnPool();
            dsBCF = null;
        }

        if (dsRecord != null) {
            dsRecord.shutdownConnPool();
            dsRecord = null;
        }
    }

    public synchronized static DataSourceManager getBcfDatasource() {
        return dsBCF;
    }

    public synchronized static DataSourceManager getRecordDatasource() {
        return dsRecord;
    }
}
