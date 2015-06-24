package com.nuevatel.bcf.service;

import com.nuevatel.cf.appconn.Action;
import com.nuevatel.cf.appconn.Id;
import com.nuevatel.cf.appconn.SessionArg;
import com.nuevatel.cf.appconn.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * TODO Unit test
 *
 * @author Ariel Salazar
 */
class MediaServiceImpl implements MediaService {

    private static Logger logger = LogManager.getLogger(MediaServiceImpl.class);

    private static final int INITIAL_CAPACITY = 8192;

    private static final float LOAD_FACTOR = 0.75f;

    private static final int CONCURRENCY_LEVEL = 256;

    /**
     * Map for dispatchers.
     */
    private Map<String, MediaDispatcher> dispatcherMap = null;

    private ScheduledExecutorService service = null;

    private int threadPoolSize = 16;

    public MediaServiceImpl() {
        dispatcherMap = new ConcurrentHashMap<>(INITIAL_CAPACITY, LOAD_FACTOR, CONCURRENCY_LEVEL);
    }

    public MediaServiceImpl(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
        dispatcherMap = new ConcurrentHashMap<>(INITIAL_CAPACITY, LOAD_FACTOR, CONCURRENCY_LEVEL);
    }

    @Override
    public void start() {
        service = Executors.newScheduledThreadPool(threadPoolSize);
    }

    @Override
    public void shutdown() {
        try {
            if (service == null) {
                return;
            }
            service.shutdown();
            // Invalidate all to remove all pending task.
            invalidateAll();
            // await 60s to terminate.
            service.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            logger.warn("Failed to shutdown service...");
        }
    }

    @Override
    public void schedule(Integer nodeId, Id id, Type type, Action action, SessionArg args, Integer mediaArg2) {
        // Callback remove dispatcher from the dispatcherMap
        MediaDispatcher dispatcher = new MediaDispatcher(nodeId, // Remote Id
                                                         id,
                                                         type,
                                                         action,
                                                         args,
                                                         ()->dispatcherMap.remove(id.getId0())); // callback -> Removes from dispatcherMap
        ScheduledFuture<?>schFuture = service.schedule(dispatcher, mediaArg2 * 100, TimeUnit.MILLISECONDS);
        dispatcher.setSchFuture(schFuture);
        // Register task
        MediaDispatcher oddDispatcher = dispatcherMap.put(id.getId0(), dispatcher);
        if (oddDispatcher != null && oddDispatcher.getSchFuture() != null) {
            oddDispatcher.getSchFuture().cancel(false);
        }
    }

    @Override
    public MediaDispatcher invalidate(String id) {
        MediaDispatcher d = dispatcherMap.remove(id);
        if (d != null) {
            // false to allowed to complete if the task is running.
            d.getSchFuture().cancel(false);
        }
        return d;
    }

    @Override
    public void invalidateAll() {
        for (Map.Entry<String, MediaDispatcher> entry : dispatcherMap.entrySet()) {
            // false to allowed to complete if the task is running.
            entry.getValue().getSchFuture().cancel(false);
            dispatcherMap.remove(entry.getKey());
        }
    }
}
