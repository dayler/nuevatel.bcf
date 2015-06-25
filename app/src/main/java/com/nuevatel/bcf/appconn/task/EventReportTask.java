package com.nuevatel.bcf.appconn.task;

import com.nuevatel.base.appconn.CompositeIE;
import com.nuevatel.base.appconn.Conn;
import com.nuevatel.base.appconn.Message;
import com.nuevatel.base.appconn.Task;
import com.nuevatel.bcf.service.MediaDispatcher;
import com.nuevatel.bcf.service.MediaServiceFactory;
import com.nuevatel.cf.appconn.Action;
import com.nuevatel.cf.appconn.CFIE;
import com.nuevatel.cf.appconn.EventReportCall;
import com.nuevatel.cf.appconn.EventReportRet;
import com.nuevatel.cf.appconn.Id;
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

    private MediaServiceFactory mediaServiceFactory = new MediaServiceFactory();

    @Override
    public Message execute(Conn conn, Message message) throws Exception {
        try {
            if (logger.isDebugEnabled() || logger.isTraceEnabled()) {
                logger.debug("EventReportCall message:{}", message.toXML());
            }
            //EventReportCall, only to verify that the message was received by the server.
            EventReportCall eventReportCall = new EventReportCall(message);

            // id
            Id id = null;
            CompositeIE idIE = message.getComposite(CFIE.ID_IE);
            if (idIE != null) {
                id = new Id(idIE);
                logger.debug("Execute Id:{}", id.getId0());
            }
            // eventType
            Byte eventType = message.getByte(CFIE.EVENT_TYPE_IE);

            if (id != null && eventType != null) {
                if (eventType == CFIE.EVENT_TYPE.O_DISCONNECT_1.getType()||
                    eventType == CFIE.EVENT_TYPE.O_ABANDON_1.getType() ||
                    eventType == CFIE.EVENT_TYPE.T_DISCONNECT_1.getType() ||
                    eventType == CFIE.EVENT_TYPE.T_ABANDON_1.getType() ||
                    eventType == CFIE.EVENT_TYPE.U_ABORT.getType() ||
                    eventType == CFIE.EVENT_TYPE.P_ABORT.getType()) {
                    mediaServiceFactory.get().invalidate(id.getId0());
                    return new EventReportRet(new Action(null, Action.SESSION_ACTION.END), null, null).toMessage();
                }

                WatchArg watchArgs = new WatchArg(WATCH_ARCG0, null, null, null, null, null);
                return new EventReportRet(new Action(null, Action.SESSION_ACTION.ACCEPT), watchArgs, null).toMessage();
            }

            return new EventReportRet(new Action(null, Action.SESSION_ACTION.END), null, null).toMessage();

        } catch (Throwable ex) {
            logger.warn("Failed to build EventReportRet. appId:{} messageId:{}",conn.getRemoteId(), message.getCode(), ex);
            return new EventReportRet(new Action(null, Action.SESSION_ACTION.END), null, null).toMessage();
        }
    }
}
