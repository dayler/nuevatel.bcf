package com.nuevatel.bcf.service;

import java.sql.SQLException;

/**
 * Created by asalazar on 6/25/15.
 */
public class AlertServiceFactory {

    private static AlertService service = null;

    public AlertServiceFactory() {
        // No op
    }

    public AlertServiceFactory(AlertService service) {
        this.service = service;
    }

    public synchronized void start(int appId) throws SQLException {
        service = new AlertServiceImpl();
        service.start(appId);
    }

    public synchronized void shutdown() {
        if (service == null) {
            return;
        }
        service.shutdown();
    }

    public synchronized AlertService get() {
        return service;
    }
}
