package com.nuevatel.bcf.core.dao;

import com.nuevatel.bcf.core.domain.Config;
import com.nuevatel.bcf.core.entity.SQLQuery;
import com.nuevatel.common.ds.DataSourceManager;
import com.nuevatel.common.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static com.nuevatel.common.util.Util.*;

/**
 * Created by asalazar on 6/25/15.
 */
public class ConfigDao implements DAO<String, Config> {

    private static Logger logger = LogManager.getLogger(ConfigDao.class);

    private DataSourceManager ds = DatabaseHelper.getBcfDatasource();

    @Override
    public void insert(Config record) throws SQLException {
        logger.warn("No implemented");
    }

    @Override
    public Config findById(String key) throws SQLException {
        return getConfig();
    }

    public Config getConfig() throws SQLException {
        Connection conn = null;
        CallableStatement stm = null;
        ResultSet rs = null;

        try {
            conn = ds.getConnection();
            stm = ds.makeStatement(conn, SQLQuery.query_for_config.query());
            rs = stm.executeQuery();
            Config config = new Config();
            while (rs.next()) {
                String name = rs.getString("param");
                Object value = rs.getObject("value");
                SetProperty.valueOf(name).set(config, value);
            }

            return config;
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
    public void update(Config record) throws SQLException {
        logger.warn("No implemented");
    }

    @Override
    public void deleteByPK(String key) throws SQLException {
        logger.warn("No implemented");
    }

    private enum SetProperty {
        sms_alarm {
            @Override
            public <T> void set(Config config, T value) {
                String raw = castAs(String.class, value);
                if (StringUtils.isBlank(raw)) {
                    return;
                }
                String[] arryValue = raw.split(",");
                config.setToSms(Arrays.asList(arryValue));
            }
        },
        email_alarm{
            @Override
            public <T> void set(Config config, T value) {
                String raw = castAs(String.class, value);
                if (StringUtils.isBlank(raw)) {
                    return;
                }
                String[] arryValue = raw.split(",");
                config.setToEmail(Arrays.asList(arryValue));
            }
        },
        sms_unitname{
            @Override
            public <T> void set(Config config, T value) {
                config.setFromSms(castAs(String.class, value));
            }
        },
        from_email{
            @Override
            public <T> void set(Config config, T value) {
                config.setFromEmail(castAs(String.class, value));
            }
        },
        endpoint_dispatcher_application{
            @Override
            public <T> void set(Config config, T value) {
                config.setEndpointDispatcher(castAs(String.class, value));
            }
        },
        endpoint_mail_middleware{
            @Override
            public <T> void set(Config config, T value) {
                config.setEndpointEmailMiddleware(castAs(String.class, value));
            }
        },
        alert_header{
            @Override
            public <T> void set(Config config, T value) {
                config.setAlertHeader(castAs(String.class, value));
            }
        },
        email_subject{
            @Override
            public <T> void set(Config config, T value) {
                config.setEmailSubject(castAs(String.class, value));
            }
        }
        ;

        public <T>void set(Config config, T value) {
            // No op
        }
    }
}
