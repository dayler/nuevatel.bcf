package com.nuevatel.bcf.stub;

import com.nuevatel.bcf.core.dao.DatabaseHelper;
import com.nuevatel.common.ds.DataSourceManager;
import com.nuevatel.common.ds.DataSourceManagerConfigurator;
import com.nuevatel.common.ds.JDBCProperties;
import com.nuevatel.common.exception.InvalidPropertyValueException;
import com.nuevatel.common.util.CSVUtil;
import com.nuevatel.common.util.IntegerUtil;
import com.nuevatel.common.util.date.DateFormatter;

import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static com.nuevatel.common.util.array.ArrayUtils.*;

/**
 * Created by asalazar on 6/10/15.
 */
public final class DatasourceUtils {
    private static String CSV_UNIT = "/unit.csv";
    private static String CSV_REGEX = "/regex.csv";
    private static String CSV_MEDIA = "/media.csv";
    private static String CSV_SWAP = "/swap.csv";

    private final static String SQL_INSERT_UNIT = "INSERT INTO unit (name, regex_id, creation_timestamp, start_timestamp, end_timestamp)\n" +
                                                  "VALUES (?, ?, ?, ?, ?)";
    private final static String SQL_INSERT_REGEX = "INSERT INTO regex (regex_id, regex_name, regex, new_media_id, end_media_id, swap_id)\n" +
                                                   "VALUES (?, ?, ?, ?, ?, ?)";
    private final static String SQL_INSERT_MEDIA = "INSERT INTO media (media_id, media_name, name, type, value)\n" +
                                                   "VALUES (?, ?, ?, ?, ?)";
    private final static String SQL_INSERT_SWAP = "INSERT INTO swap (swap_id, swap_name, name, type)\n" +
                                                  "VALUES (?, ?, ?, ?)";

    public final static String SQL_DELETE_UNIT = "delete from unit where 1;";
    public final static String SQL_DELETE_REGEX = "delete from regex where 1;";
    public final static String SQL_DELETE_MEDIA = "delete from media where 1;";
    public final static String SQL_DELETE_SWAP = "delete from swap where 1;";

    private DatasourceUtils() {
        // No op
    }

    public static DataSourceManager getTestConnection(Properties prop) throws InvalidPropertyValueException, SQLException, ClassNotFoundException {
        JDBCProperties jdbcProps = new JDBCProperties(prop);
        DatabaseHelper.configure(jdbcProps, jdbcProps);
        return DatabaseHelper.getBcfDatasource();
    }

    private static void populateUnitTestData(DataSourceManager ds) throws SQLException, IOException {
        Connection conn = null;
        conn = ds.getConnection();
        CallableStatement stm = null;
        InputStream is = null;

        try {
            is = DatasourceUtils.class.getResourceAsStream(CSV_UNIT);
            conn = ds.getConnection();
            List<String[]>records = CSVUtil.read(is, 1);
            for (String[] row : records) {
                stm = ds.makeStatement(
                        conn,
                        SQL_INSERT_UNIT,
                        get(row, 0),
                        IntegerUtil.tryParse(get(row, 1)),
                        // 18/11/2013 10:31:06.000000
                        DateFormatter.CUSTOM.parse(get(row, 2), "dd/MM/yy HH:mm:ss"),
                        DateFormatter.CUSTOM.parse(get(row, 3), "dd/MM/yy HH:mm:ss"),
                        DateFormatter.CUSTOM.parse(get(row, 4), "dd/MM/yy HH:mm:ss")
                        );
                stm.execute();
                stm.close();
            }
        } finally {
            if (conn != null) {
                conn.close();
            }

            if (stm != null) {
                stm.close();
            }

            if (is != null) {
                is.close();
            }
        }
    }

    private static void populateRegexTestData(DataSourceManager ds) throws SQLException, IOException {
        Connection conn = null;
        conn = ds.getConnection();
        CallableStatement stm = null;
        InputStream is = null;

        try {
            is = DatasourceUtils.class.getResourceAsStream(CSV_REGEX);
            conn = ds.getConnection();
            List<String[]>records = CSVUtil.read(is, 1);
            for (String[] row : records) {
                stm = ds.makeStatement(
                        conn,
                        SQL_INSERT_REGEX,
                        IntegerUtil.tryParse(get(row, 0)),
                        get(row, 1),
                        get(row, 2),
                        IntegerUtil.tryParse(get(row, 3)),
                        IntegerUtil.tryParse(get(row, 4)),
                        IntegerUtil.tryParse(get(row, 5))
                );
                stm.execute();
                stm.close();
            }
        } finally {
            if (conn != null) {
                conn.close();
            }

            if (stm != null) {
                stm.close();
            }

            if (is != null) {
                is.close();
            }
        }
    }

    private static void populateMediaTestData(DataSourceManager ds) throws SQLException, IOException {
        Connection conn = null;
        conn = ds.getConnection();
        CallableStatement stm = null;
        InputStream is = null;

        try {
            is = DatasourceUtils.class.getResourceAsStream(CSV_MEDIA);
            conn = ds.getConnection();
            List<String[]>records = CSVUtil.read(is, 1);
            for (String[] row : records) {
                stm = ds.makeStatement(
                        conn,
                        SQL_INSERT_MEDIA,
                        IntegerUtil.tryParse(get(row, 0)),
                        get(row, 1),
                        get(row, 2),
                        IntegerUtil.tryParse(get(row, 3)),
                        IntegerUtil.tryParse(get(row, 4))
                );
                stm.execute();
                stm.close();
            }
        } finally {
            if (conn != null) {
                conn.close();
            }

            if (stm != null) {
                stm.close();
            }

            if (is != null) {
                is.close();
            }
        }
    }

    private static void deleteUnitTestData(DataSourceManager ds) throws SQLException {
        Connection conn = null;
        conn = ds.getConnection();
        CallableStatement stm = null;

        try {
            conn = ds.getConnection();
            stm = ds.makeStatement(conn, SQL_DELETE_UNIT);
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

    private static void deleteRegexTestData(DataSourceManager ds) throws SQLException {
        Connection conn = null;
        conn = ds.getConnection();
        CallableStatement stm = null;

        try {
            conn = ds.getConnection();
            stm = ds.makeStatement(conn, SQL_DELETE_REGEX);
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

    private static void deleteMediaTestData(DataSourceManager ds) throws SQLException {
        Connection conn = null;
        conn = ds.getConnection();
        CallableStatement stm = null;

        try {
            conn = ds.getConnection();
            stm = ds.makeStatement(conn, SQL_DELETE_MEDIA);
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

    private static void deleteSwapTestData(DataSourceManager ds) throws SQLException {
        Connection conn = null;
        conn = ds.getConnection();
        CallableStatement stm = null;

        try {
            conn = ds.getConnection();
            stm = ds.makeStatement(conn, SQL_DELETE_SWAP);
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

    private static void populateSwapTestData(DataSourceManager ds) throws SQLException, IOException {
        Connection conn = null;
        conn = ds.getConnection();
        CallableStatement stm = null;
        InputStream is = null;

        try {
            is = DatasourceUtils.class.getResourceAsStream(CSV_SWAP);
            conn = ds.getConnection();
            List<String[]>records = CSVUtil.read(is, 1);
            for (String[] row : records) {
                stm = ds.makeStatement(
                        conn,
                        SQL_INSERT_SWAP,
                        IntegerUtil.tryParse(get(row, 0)),
                        get(row, 1),
                        get(row, 2),
                        IntegerUtil.tryParse(get(row, 3))
                );
                stm.execute();
                stm.close();
            }
        } finally {
            if (conn != null) {
                conn.close();
            }

            if (stm != null) {
                stm.close();
            }

            if (is != null) {
                is.close();
            }
        }
    }

    public static void populateTestData(DataSourceManager ds) throws SQLException, IOException {
        populateSwapTestData(ds);
        populateMediaTestData(ds);
        populateRegexTestData(ds);
        populateUnitTestData(ds);
    }

    public static void deleteTestData(DataSourceManager ds) throws SQLException {
        deleteSwapTestData(ds);
        deleteMediaTestData(ds);
        deleteRegexTestData(ds);
    }
}
