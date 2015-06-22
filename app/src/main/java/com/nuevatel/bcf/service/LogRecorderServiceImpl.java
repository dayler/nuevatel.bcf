package com.nuevatel.bcf.service;

import com.nuevatel.bcf.core.dao.SessionRecordDAO;
import com.nuevatel.bcf.core.domain.SessionRecord;
import com.nuevatel.common.thread.SimpleMonitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by asalazar on 6/22/15.
 */
public class LogRecorderServiceImpl implements LogRecorderService {

    private SimpleMonitor sync = new SimpleMonitor();

    private static Logger logger = LogManager.getLogger(LogRecorderServiceImpl.class);

    private ExecutorService service;

    private int size;

    private Queue<SessionRecord>srQueue = new ConcurrentLinkedQueue<>();

    private SessionRecordDAO srDao = new SessionRecordDAO();

    private boolean sealed = false;

    private boolean running = false;

    public LogRecorderServiceImpl() {
        size = 2;
    }

    public LogRecorderServiceImpl(int size) {
        this.size = size;
    }

    private void commitSessionRecord(SessionRecord sr) throws SQLException {
        if (sr == null) {
            return;
        }

        if (SessionRecord.Flag.insert == sr.getFlag()) {
            srDao.insert(sr);
        } else if (SessionRecord.Flag.update == sr.getFlag()) {
            srDao.update(sr);
        } else if (SessionRecord.Flag.updateEndTimestamp == sr.getFlag()) {
            srDao.updateEndTimestamp(sr.getId(), sr.getEndTimestamp(), sr.getRespCode());
        } else {
            // No op
        }
    }

    private void commit() {
        try {
            if (isSealed()) {
                // Wait 500ms
                sync.doWait(500);
                return;
            }

            if (srQueue.isEmpty()) {
                // If queue is empty sleep thread.
                sync.doWait();
            }

            // Get from queue
            SessionRecord sr = srQueue.poll();
            commitSessionRecord(sr);
        } catch (InterruptedException | SQLException ex) {
            logger.warn("Failed to process new session record.",  ex);
        }
    }

    @Override
    public void appendSessionRecord(SessionRecord sr) {
        if (sr == null) {
            return;
        }

        srQueue.offer(sr);
        sync.doNotifyAll();
    }

    @Override
    public void start() {
        service = Executors.newFixedThreadPool(size);
        service.execute(()->commit());
        running = true;
    }

    @Override
    public void shutdown() {
        try {
            service.shutdown();
            service.awaitTermination(60, TimeUnit.SECONDS);
            running = false;
        } catch (InterruptedException ex) {
            logger.warn("Failed to shutdown the service...");
        }
    }

    @Override
    public synchronized void sealed() {
        sealed = true;
    }

    @Override
    public synchronized boolean isRunning() {
        return running;
    }

    @Override
    public synchronized boolean isSealed() {
        return sealed;
    }

    @Override
    public void release() {
        sealed = false;
        sync.doNotifyAll();
    }
}
