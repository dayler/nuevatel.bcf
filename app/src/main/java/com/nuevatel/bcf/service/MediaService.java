package com.nuevatel.bcf.service;

import com.nuevatel.cf.appconn.Action;
import com.nuevatel.cf.appconn.Id;
import com.nuevatel.cf.appconn.SessionArg;
import com.nuevatel.cf.appconn.Type;

/**
 * TODO Add java docs
 *
 * @author Ariel Salazar
 */
public interface MediaService {

    void start();

    void shutdown();

    void schedule(Integer nodeId, Id id, Type type, Action action, SessionArg args, Integer mediaArg2);

    MediaDispatcher invalidate(String id);

    void invalidateAll();
}
