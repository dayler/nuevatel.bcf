package com.nuevatel.bcf.service;

import com.nuevatel.base.appconn.AppServer;
import com.nuevatel.base.appconn.TaskSet;

import java.util.Properties;

/**
 * Created by asalazar on 6/14/15.
 */
public final class AppServerFactory {

    private static AppServer server = null;

    public AppServerFactory(AppServer srv) {
        AppServerFactory.server = srv;
    }

    public AppServerFactory() {
        // No op. used to prevent instantiation.
    }

    /**
     * Create new instance for AppServer and start it.
     *
     * @param id
     * @param taskSet
     * @param prop
     */
    public synchronized void start(Integer id, TaskSet taskSet, Properties prop) throws Exception {
        server = new AppServer(id, taskSet, prop);
        // new AppServer(appId, appServerTaskSet, appServerProperties);
        server.start();
    }

    public synchronized void interrupt() {
        if (server == null) {
            return;
        }
        server.interrupt();
    }

    /**
     *
     * @return Return the started service. If the service is not started yet returns null.
     */
    public synchronized AppServer get() {
        return server;
    }
}
