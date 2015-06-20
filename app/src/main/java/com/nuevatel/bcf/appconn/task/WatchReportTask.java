package com.nuevatel.bcf.appconn.task;

import com.nuevatel.base.appconn.Conn;
import com.nuevatel.base.appconn.Message;
import com.nuevatel.base.appconn.Task;
import com.nuevatel.cf.appconn.Action;
import com.nuevatel.cf.appconn.WatchArg;
import com.nuevatel.cf.appconn.WatchReportCall;
import com.nuevatel.cf.appconn.WatchReportRet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.nuevatel.bcf.core.Constants.WATCH_ARCG0;

/**
 * Handle WatchReportCall event.
 *
 * @author Ariel Salazar
 */
public class WatchReportTask implements Task {

    private static Logger logger = LogManager.getLogger(WatchReportTask.class);

    @Override
    public Message execute(Conn conn, Message message) throws Exception {
        try {
            if (logger.isDebugEnabled() || logger.isTraceEnabled()) {
                logger.debug("WatchReportTask message:{}", message.toXML());
            }
            // watchReportCall
            WatchReportCall watchReportCall = new WatchReportCall(message);
            WatchArg watchArgs = new WatchArg(WATCH_ARCG0, null, null, null, null, null);
            return new WatchReportRet(new Action(null, Action.SESSION_ACTION.ACCEPT), watchArgs, null).toMessage();
        } catch (Throwable ex) {
            logger.warn("Failed to build WatchReportTask. appId:{} messageId:{}",conn.getRemoteId(), message.getCode(), ex);
            return new WatchReportRet(new Action(null, Action.SESSION_ACTION.END), null, null).toMessage();
        }
    }
}
