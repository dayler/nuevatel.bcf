package com.nuevatel.bcf.appconn.task;

import com.nuevatel.base.appconn.Conn;
import com.nuevatel.base.appconn.Message;
import com.nuevatel.base.appconn.Task;
import com.nuevatel.cf.appconn.Action;
import com.nuevatel.cf.appconn.EventReportCall;
import com.nuevatel.cf.appconn.EventReportRet;
import com.nuevatel.cf.appconn.WatchArg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.nuevatel.bcf.core.Constants.WATCH_ARCG0;

/**
 * Handle EventReportCall event.
 *
 * @author Ariel Salazar
 */
public class EventReportTask implements Task {

    private static Logger logger = LogManager.getLogger(EventReportTask.class);

    @Override
    public Message execute(Conn conn, Message message) throws Exception {
        try {
            if (logger.isDebugEnabled() || logger.isTraceEnabled()) {
                logger.debug("EventReportCall message:{}", message.toXML());
            }
            //EventReportCall, only to verify that the message was received by the server.
            EventReportCall eventReportCall = new EventReportCall(message);
            WatchArg watchArgs = new WatchArg(WATCH_ARCG0, null, null, null, null, null);
            return new EventReportRet(new Action(null, Action.SESSION_ACTION.ACCEPT), watchArgs, null).toMessage();
        } catch (Throwable ex) {
            logger.warn("Failed to build EventReportRet. appId:{} messageId:{}",conn.getRemoteId(), message.getCode(), ex);
            return new EventReportRet(new Action(null, Action.SESSION_ACTION.END), null, null).toMessage();
        }
    }
}
