package com.nuevatel.bcf.service;

import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by asalazar on 6/25/15.
 */
public interface AlertService {

    void start(int appId) throws SQLException;

    void shutdown();

    void appendAlert(String msg);
}
