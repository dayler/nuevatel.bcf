package com.nuevatel.bcf.service;

import com.nuevatel.base.appconn.Message;
import com.nuevatel.cf.appconn.Action;
import com.nuevatel.cf.appconn.Id;
import com.nuevatel.cf.appconn.SessionArg;
import com.nuevatel.cf.appconn.SetSessionCall;
import com.nuevatel.cf.appconn.Type;
import com.nuevatel.common.util.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledFuture;

/**
 * TODO Make unit test
 *
 * Responsible to dispatch single media request.
 *
 * @author Ariel Salazar
 */
class MediaDispatcher implements Runnable {

    private static Logger logger = LogManager.getLogger(MediaDispatcher.class);

    /**
     * Remote app id. The Id of the app to connect with the server.
     */
    private Integer nodeId;

    /**
     * Message ID
     */
    private Id id;

    private Type type;

    private Action action;

    private SessionArg args;

    private ScheduledFuture<?>schFuture;

    private BCFServerFactory bcfServerFactory = new BCFServerFactory();

    private Runnable callBack;

    public MediaDispatcher(Integer nodeId, Id id, Type type, Action action, SessionArg args, Runnable callBack) {
        Parameters.checkNull(nodeId, "nodeId");
        Parameters.checkNull(id, "id");
        Parameters.checkNull(type, "type");
        Parameters.checkNull(action, "action");
        Parameters.checkNull(args, "args");

        this.id = id;
        this.type = type;
        this.action = action;
        this.args = args;
        this.callBack = callBack;
    }

    @Override
    public void run() {
        try {
            if (callBack != null) {
                // Execute callback
                callBack.run();
            }
            // Session call
            SetSessionCall sessionCall = new SetSessionCall(id, type, action, args);
            Message newSessionRet = bcfServerFactory.get().dispatch(nodeId, sessionCall.toMessage());
            if (newSessionRet == null) {
                logger.warn("Failed to dispatch Media Message through appconnServer. nodeId:%s id:%s type:%s action:%s args:%s",
                        nodeId, id, type, action, args);
            }
        } catch (Exception ex) {
            logger.warn("Failed to dispatch Media Message through appconnServer. nodeId:%s id:%s type:%s action:%s args:%s",
                    nodeId, id, type, action, args, ex);
        }

    }

    public void setSchFuture(ScheduledFuture<?> schFuture) {
        this.schFuture = schFuture;
    }

    public ScheduledFuture<?> getSchFuture() {
        return schFuture;
    }
}
