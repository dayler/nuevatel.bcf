package com.nuevatel.bcf.appconn.task;

import com.nuevatel.base.appconn.Conn;
import com.nuevatel.base.appconn.Message;
import com.nuevatel.base.appconn.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handle test TestSessionAsyncRet event. Return null to responds.
 *
 * @author Ariel Salazar
 */
public class TestSessionAsyncTask implements Task {

    private static Logger logger = LogManager.getLogger(TestSessionAsyncTask.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public Message execute(Conn conn, Message message) throws Exception {
        if (logger.isDebugEnabled() || logger.isTraceEnabled()) {
            logger.debug("TestSessionAsyncTask message:%s", message == null ? null : message.toXML());
        }
        return null;
    }
}
