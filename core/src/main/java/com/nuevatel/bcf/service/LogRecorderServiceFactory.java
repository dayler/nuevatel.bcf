package com.nuevatel.bcf.service;

/**
 * Created by asalazar on 6/22/15.
 */
public class LogRecorderServiceFactory {

    private static LogRecorderService service = null;

    public LogRecorderServiceFactory () {
        // No op
    }

    public LogRecorderServiceFactory(LogRecorderService srv) {
        service = srv;
    }

    public synchronized void start(int threadPoolSize) {
        service = new LogRecorderServiceImpl(threadPoolSize);
        service.start();
    }

    public synchronized void shutdown() {
        if (service == null) {
            return;
        }
        service.shutdown();
    }

    public synchronized LogRecorderService get() {
        return service;
    }
}
