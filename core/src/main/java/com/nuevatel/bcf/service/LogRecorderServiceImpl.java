package com.nuevatel.bcf.service;

import com.nuevatel.bcf.core.dao.SessionRecordDAO;
import com.nuevatel.bcf.core.dao.WSIRecordDAO;
import com.nuevatel.bcf.core.domain.Record;
import com.nuevatel.bcf.core.domain.SessionRecord;
import com.nuevatel.bcf.core.domain.WSIRecord;
import com.nuevatel.common.thread.SimpleMonitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.nuevatel.common.util.Util.*;

/**
 * Created by asalazar on 6/22/15.
 */
public class LogRecorderServiceImpl implements LogRecorderService {

    private SimpleMonitor sync = new SimpleMonitor();

    private static Logger logger = LogManager.getLogger(LogRecorderServiceImpl.class);

    private ExecutorService service;

    private int size;

    private Queue<Record>srQueue = new ConcurrentLinkedQueue<>();

    private SessionRecordDAO srDao = new SessionRecordDAO();

    private WSIRecordDAO wsiDao = new WSIRecordDAO();

    private boolean sealed = false;

    private boolean running = false;

    public LogRecorderServiceImpl() {
        size = 2;
    }

    public LogRecorderServiceImpl(int size) {
        this.size = size;
    }

    private void commitSessionRecord(Record r) throws SQLException {
        if (r == null || ! (r instanceof SessionRecord)) {
            return;
        }

        SessionRecord sr = castAs(SessionRecord.class, r);
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

    private void commitWSIRecord(Record r) throws SQLException {
        if (r == null || !(r instanceof WSIRecord)) {
            return;
        }
        WSIRecord wsir = castAs(WSIRecord.class, r);
        wsiDao.insert(wsir);
    }

    private void commit() {
        try {
            while (isRunning()) {
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
                Record r = srQueue.poll();
                commitSessionRecord(r);
                commitWSIRecord(r);
            }
        } catch (InterruptedException | SQLException ex) {
            logger.warn("Failed to process new session record.",  ex);
        }
    }

    @Override
    public void appendRecord(Record record) {
        if (record == null) {
            return;
        }
        srQueue.offer(record);
        sync.doNotifyAll();
    }

    @Override
    public void start() {
        service = Executors.newFixedThreadPool(size);
        setRunning(true);
        service.execute(()->commit());
    }

    @Override
    public void shutdown() {
        try {
            service.shutdown();
            service.awaitTermination(60, TimeUnit.SECONDS);
            setRunning(false);
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

    private synchronized void setRunning(boolean running) {
        this.running = running;
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
