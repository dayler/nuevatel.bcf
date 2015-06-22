package com.nuevatel.bcf.service;

import com.nuevatel.base.appconn.AppMessages;
import com.nuevatel.cf.appconn.Action;
import com.nuevatel.cf.appconn.CFIE;
import com.nuevatel.cf.appconn.EventReportCall;
import com.nuevatel.cf.appconn.Id;
import com.nuevatel.cf.appconn.SessionArg;
import com.nuevatel.cf.appconn.SetSessionCall;
import com.nuevatel.cf.appconn.SetSessionRet;
import com.nuevatel.cf.appconn.Type;
import com.nuevatel.common.util.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private AppServerFactory appServerFactory = new AppServerFactory();

    private Runnable onPreExecute;

    public MediaDispatcher(Integer nodeId, Id id, Type type, Action action, SessionArg args, Runnable onPreExecute) {
        Parameters.checkNull(nodeId, "nodeId");
        Parameters.checkNull(id, "id");
        Parameters.checkNull(type, "type");
        Parameters.checkNull(action, "action");
        Parameters.checkNull(args, "args");

        this.id = id;
        this.type = type;
        this.action = action;
        this.args = args;
        this.onPreExecute = onPreExecute;
    }

    @Override
    public void run() {
        if (onPreExecute != null) {
             // Execute callback
            onPreExecute.run();
        }
        // Set Session call
        SetSessionCall sessionCall = new SetSessionCall(id,
                                                        type,
                                                        new Action(Action.MEDIA_ACTION.END_MEDIA, action.getSessionAction()),
                                                        args);
        SetSessionRet setSessionRet = null;
        try {
            setSessionRet = new SetSessionRet(appServerFactory.get().dispatch(nodeId, sessionCall.toMessage()));
        } catch (Exception ex) {
            logger.warn("Failed to dispatch Media Message through appconnServer. remoteId:{} id:{} type:{} action:{} args:{}",
                        nodeId, id, type, action, args, ex);
        }

        // Be sure to send terminations task.
        if ((setSessionRet == null || setSessionRet.getRet() == AppMessages.FAILED)
             && action.getSessionAction() != Action.SESSION_ACTION.END) {
            // Event report call
            EventReportCall eventReportCall = new EventReportCall(id, type, CFIE.EVENT_TYPE.SET_SESSION_FAILED.getType(), null);
            try {
                appServerFactory.get().dispatch(nodeId, eventReportCall.toMessage());
                // TODO Ask about that
                // GatewayApp.getGatewayApp().getProxyApp(toAppId).getAppClient().dispatch(eventReportCall.toMessage());
            } catch (Exception ex) {
                logger.warn("Failed to dispatch Media Message through appconnServer. remoteId:{} id:{} type:{} action:{} args:{}",
                            nodeId, id, type, action, args, ex);
            }
        }
    }

    public void setSchFuture(ScheduledFuture<?> schFuture) {
        this.schFuture = schFuture;
    }

    public ScheduledFuture<?> getSchFuture() {
        return schFuture;
    }
}
