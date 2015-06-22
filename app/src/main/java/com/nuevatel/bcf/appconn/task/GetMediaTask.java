package com.nuevatel.bcf.appconn.task;

import com.nuevatel.base.appconn.Conn;
import com.nuevatel.base.appconn.Message;
import com.nuevatel.base.appconn.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TODO Not needed, remove me!!
 */
public class GetMediaTask implements Task {

    private static Logger logger = LogManager.getLogger(GetMediaTask.class);

    @Override
    public Message execute(Conn conn, Message message) throws Exception {
//        logger.info("****************************************");
        if (logger.isDebugEnabled() || logger.isTraceEnabled()) {
            logger.debug("GetMediaTask message:{}", message.toXML());
        }
        return null;
    }
}
