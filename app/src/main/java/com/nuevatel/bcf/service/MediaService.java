package com.nuevatel.bcf.service;

import com.nuevatel.cf.appconn.Action;
import com.nuevatel.cf.appconn.Id;
import com.nuevatel.cf.appconn.SessionArg;
import com.nuevatel.cf.appconn.Type;

/**
 * Responsible to dispatch Media messages to appconnClient.
 *
 * @author Ariel Salazar
 */
public interface MediaService {
    /**
     * Initialize the service.
     */
    void start();

    /**
     * Stop service, stop all threads. Await 60 to finalize in progress process.
     */
    void shutdown();

    /**
     * Schedule to deliver Media messages.
     *
     * @param nodeId Client Id. The Id of the client to connect with server.
     * @param id Id for the dialog message.
     * @param type
     * @param action
     * @param args
     * @param mediaArg2
     */
    void schedule(Integer nodeId, Id id, Type type, Action action, SessionArg args, Integer mediaArg2);

    /**
     * Cancel task related to dispatch Media message to belongs Id.
     *
     * @param id Id to identify the message.
     */
    void invalidate(String id);

    /**
     * Cancel all pending tasks queued in the service.
     */
    void invalidateAll();
}
