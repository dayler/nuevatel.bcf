package com.nuevatel.bcf.service;

import com.nuevatel.base.appconn.TaskSet;

import java.util.Properties;

/**
 * Created by asalazar on 6/14/15.
 */
public final class BCFServerFactory {

    private static BCFServer server = null;

    public BCFServerFactory(BCFServer srv) {
        BCFServerFactory.server = srv;
    }

    public BCFServerFactory() {
        // No op. used to prevent instantiation.
    }

    /**
     * Create new instance for AppServer and start it.
     *
     * @param id
     * @param taskSet
     * @param prop
     */
    public synchronized void start(Integer id, TaskSet taskSet, Properties prop) {
        server = new BCFServer(id, taskSet, prop);
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
    public synchronized BCFServer get() {
        return server;
    }
}
