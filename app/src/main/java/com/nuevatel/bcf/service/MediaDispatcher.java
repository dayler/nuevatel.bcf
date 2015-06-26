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
 *
 * Responsible to dispatch single media request.
 *
 * @author Ariel Salazar
 */
public class MediaDispatcher implements Runnable {

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

        this.nodeId = nodeId;
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
        Action tmpAction = new Action(Action.MEDIA_ACTION.END_MEDIA, this.action.getSessionAction());
        SetSessionCall sessionCall = new SetSessionCall(id,
                                                        type,
                                                        tmpAction,
                                                        args);
        SetSessionRet setSessionRet = null;
        try {
            setSessionRet = new SetSessionRet(appServerFactory.get().dispatch(nodeId, sessionCall.toMessage()));
            logger.debug("dispatch SetSessionRet nodeId:{} id0:{} id1:{} {} {} {}",
                         nodeId, id.getId0(), id.getId1(), toStringType(type), toStringAction(tmpAction), toStringSessionArgs(args));
        } catch (Exception ex) {
            logger.warn("Failed to dispatch Media Message through appconnServer. remoteId:{} id:{} type:{} action:{} args:{}",
                        nodeId, id, type, this.action, args, ex);
        }

        // Be sure to send terminations task.
        if ((setSessionRet == null || setSessionRet.getRet() == AppMessages.FAILED)
             && this.action.getSessionAction() != Action.SESSION_ACTION.END) {
            // Event report call
            EventReportCall eventReportCall = new EventReportCall(id, type, CFIE.EVENT_TYPE.SET_SESSION_FAILED.getType(), null);
            try {
                appServerFactory.get().dispatch(nodeId, eventReportCall.toMessage());
            } catch (Exception ex) {
                logger.warn("Failed to dispatch Media Message through appconnServer. remoteId:{} id:{} type:{} action:{} args:{}",
                            nodeId, id, type, this.action, args, ex);
            }
        }
    }

    private String toStringAction(Action action) {
        return String.format("Action[MEDIA_ACTION:%s SESSION_ACTION:%s]", action.getMediaAction(), action.getSessionAction());
    }

    private String toStringType(Type type) {
        return String.format("Type[SERVICE_TYPE:%S REQUEST_TYPE:%S]", type.getServiceType(), type.getRequestType());
    }

    private String toStringSessionArgs(SessionArg args) {
        if (args == null) {
            return "";
        }
        return String.format("SessionArgs[fromName:%s toName:%s apn:%s qos:%s uei:%s ref:%s]",
                             args.getFromName(),
                             args.getToName(),
                             args.getAPN(),
                             args.getQOS(),
                             args.getUEI(),
                             args.getReference());
    }

    public void setSchFuture(ScheduledFuture<?> schFuture) {
        this.schFuture = schFuture;
    }

    public ScheduledFuture<?> getSchFuture() {
        return schFuture;
    }

    public Action getAction() {
        return action;
    }
}
