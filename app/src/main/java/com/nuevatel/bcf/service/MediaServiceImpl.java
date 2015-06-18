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

    /**
     * Map for dispatchers.
     */
    private Map<String, MediaDispatcher> dispatcherMap = new ConcurrentHashMap<>();

    private ScheduledExecutorService service;

    private int threadPoolSize = 16;

    public MediaServiceImpl() {
        // No op
    }

    public MediaServiceImpl(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
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
        MediaDispatcher dispatcher = new MediaDispatcher(nodeId, id, type, action, args, /* callback */()->dispatcherMap.remove(id.getId0()));
        ScheduledFuture<?>schFuture = service.schedule(dispatcher, mediaArg2 * 100, TimeUnit.MILLISECONDS);
        dispatcher.setSchFuture(schFuture);
        // Register task
        dispatcherMap.put(id.getId0(), dispatcher);
    }

    @Override
    public void invalidate(String id) {
        MediaDispatcher d = dispatcherMap.remove(id);
        if (d != null) {
            // false to allowed to complete if the task is running.
            d.getSchFuture().cancel(false);
        }
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
