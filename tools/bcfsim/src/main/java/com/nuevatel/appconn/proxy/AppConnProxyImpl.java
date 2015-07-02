package com.nuevatel.appconn.proxy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.log4j.Logger;

import com.nuevatel.appconn.dto.ReportedSessionDTO;
import com.nuevatel.appconn.dto.RequestServiceType;
import com.nuevatel.appconn.dto.SessionDTO;
import com.nuevatel.base.appconn.AppClient;
import com.nuevatel.base.appconn.IE;
import com.nuevatel.base.appconn.Message;
import com.nuevatel.cf.appconn.Action;
import com.nuevatel.cf.appconn.CFIE;
import com.nuevatel.cf.appconn.Action.MEDIA_ACTION;
import com.nuevatel.cf.appconn.Action.SESSION_ACTION;
import com.nuevatel.cf.appconn.CFIE.WATCH_TYPE;
import com.nuevatel.cf.appconn.EventArg;
import com.nuevatel.cf.appconn.EventReportCall;
import com.nuevatel.cf.appconn.GetMediaCall;
import com.nuevatel.cf.appconn.Id;
import com.nuevatel.cf.appconn.Location;
import com.nuevatel.cf.appconn.MediaArg;
import com.nuevatel.cf.appconn.Name;
import com.nuevatel.cf.appconn.NewSessionCall;
import com.nuevatel.cf.appconn.SessionArg;
import com.nuevatel.cf.appconn.Type;
import com.nuevatel.cf.appconn.WatchArg;
import com.nuevatel.cf.appconn.WatchReportCall;
import com.nuevatel.common.util.Parameters;

import static com.nuevatel.bcf.sim.Constants.*;

/**
 * Define the logic to communicate with VONE through APPCONN
 *
 * @author asalazar
 */
public class AppConnProxyImpl implements AppConnProxy {

    /**
     * Application logger.
     */
    private final static Logger logger = Logger.getLogger(AppConnProxyImpl.class);

    /**
     * App client interface.
     */
    private final AppClient appClient;

    /**
     * AppConnProxyImpl Constructor. Initialize the proxy with its appconn instance, <b>the appconn
     * instance must to start() before to set on this.</b>
     *
     * @param appClient AppConn instance.
     */
    public AppConnProxyImpl(AppClient appClient) {
        Parameters.checkNull(appClient, "appClient");

        this.appClient = appClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SessionDTO createSession(SessionDTO sessionToCreate) throws Exception {
        logger.info(String.format("Create session for: %s", sessionToCreate.toString()));
        Parameters.checkNull(sessionToCreate, "sessionToCreate");

        Id appId = new Id(sessionToCreate.getSessionId(), null);
        Type type = new Type(sessionToCreate.getServiceType().getServiceType(),
                sessionToCreate.getRequestType().getRequestType());

        // NAI -> from name
        // TON -> to name
        Name name = new Name(sessionToCreate.getName(), sessionToCreate.getNai());
        Name toName = new Name(sessionToCreate.getToName(), sessionToCreate.getTon());
        Name fromName = new Name(sessionToCreate.getFromName(), sessionToCreate.getNai());

        Location location = new Location(sessionToCreate.getCellGlobalId(), sessionToCreate.getNodeId());

        String tmpRef = sessionToCreate.getSessionId();

        SessionArg sessionArg = new SessionArg(fromName, toName, null, null, null, tmpRef);

        logger.debug(String.format("Session args - name:%s fromName:%s toName:%s null null null tmpRef:%s",
                sessionToCreate.getName(), sessionToCreate.getFromName(), sessionToCreate.getToName(),
                tmpRef));
        logger.info(String.format("New session call - sessionId:%s", sessionToCreate.getSessionId()));

        NewSessionCall newSessionCall =
                new NewSessionCall(appId, type, sessionToCreate.getAuxType(), name, location, sessionArg);
        Message newSessionRet = appClient.dispatch(newSessionCall.toMessage());
        Action action = new Action(newSessionRet.getIE(CFIE.ACTION_IE));

        // Get media action.
        String mediaArg0 = getMediaResource(newSessionRet, action, name);
        // Get new session args. If session action is not Modify, it will return null.
        SessionArg newSessionArg = getNewSessionArg(newSessionRet, action);

        return new SessionDTO(sessionToCreate, newSessionArg, action, mediaArg0);
    }

    /**
     * Gets NewSessionArg if its is applicable.
     * 
     * @param newSessionRet Response from AppClient.
     * @param action Action response.
     */
    private SessionArg getNewSessionArg(Message newSessionRet, Action action) {
        if (SESSION_ACTION.MODIFY == action.getSessionAction()) {
            IE ie = newSessionRet.getIE(CFIE.SESSION_ARG_IE);

            if (ie != null) {
                // Create new SessionDTO
                SessionArg newSessionArg = new SessionArg(newSessionRet.getIE(CFIE.SESSION_ARG_IE));

                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("Session Action was modifying. New Session Action: %s",
                            newSessionArg));
                }

                return newSessionArg;
            }
        }

        return null;
    }

    /**
     * 
     * @param mediaArg0
     * @param mediaArg1
     * @param name Unit of the session.
     * @return Media args to contains new media information. <b>null</b> if there are not media
     * unit applicable.
     * @throws Exception
     */
    private MediaArg getMediaArg(String mediaArg0, Byte mediaArg1, Name name) throws Exception {
        Name toName = new Name(mediaArg0, mediaArg1);
        GetMediaCall mediaCall = new GetMediaCall(name, toName);
        Message mediaRet = appClient.dispatch(mediaCall.toMessage());
        IE ie = mediaRet.getIE(CFIE.MEDIA_ARG_IE);

        return ie == null ? null : new MediaArg(ie);
    }

    /**
     * 
     * @param newSessionRet AppConn client response.
     * @param action Media Action response.
     * @return <b>MediaArg0</b> Media unit to execute, if it is applicable, in other case returns null.
     * @throws Exception 
     */
    private String getMediaResource(Message newSessionRet, Action action, Name name) throws Exception {
        String resource = null;

        if (MEDIA_ACTION.NEW_MEDIA == action.getMediaAction()) {
            IE ie = newSessionRet.getIE(CFIE.MEDIA_ARG_IE);

            if (ie != null) {
                MediaArg mediaArg = new MediaArg(ie);
                String mediaArg0 = mediaArg.getMediaArg0();
                Byte mediaArg1 = mediaArg.getMediaArg1();
                MediaArg newMediaArg = getMediaArg(mediaArg0, mediaArg1, name);
                resource = newMediaArg == null ? null : newMediaArg.getMediaArg0();

                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("Media Args was found. Media Resource = %s", resource));
                }
            }
        }

        return resource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReportedSessionDTO startSession(String sessionId, RequestServiceType reqSrvType) throws Exception {
        Parameters.checkBlankString(sessionId, "sessionId");
        Parameters.checkNull(reqSrvType, "reqSrvType");
        logger.info(String.format("Start session for: %s", sessionId));

        Id appId = new Id(sessionId, null);
        Type type = new Type(Type.SERVICE_TYPE.SPEECH, reqSrvType.getRequestType());

        EventReportCall eventRepCall = new EventReportCall(appId, type, Type.EVENT_TYPE.O_ANSWER_2.getType(), null);

        logger.debug(String.format("EventReportCall id: %s type: %s eventType: %s eventArgs: null",
                appId.getId0(), type, Type.EVENT_TYPE.O_ANSWER_2));

        Message eventRepRet = appClient.dispatch(eventRepCall.toMessage());

        return new ReportedSessionDTO(eventRepRet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SessionDTO endSession(SessionDTO session, boolean callerWasEndSession,
                                 Integer endValue, RequestServiceType reqSrvType,
                                 Integer sipEndType) throws Exception {
        Parameters.checkNull(session, "session");
        Parameters.checkNull(sipEndType, "sipEndType");
        Parameters.checkNull(reqSrvType, "reqSrvType");

        logger.info(String.format("End session for: session: %s", session));

        // End session.
        Type.EVENT_TYPE eventType = callerWasEndSession ?
                Type.EVENT_TYPE.O_DISCONNECT_1 : Type.EVENT_TYPE.O_DISCONNECT_2;

        String sessionId = session.getSessionId();
        Id appId = new Id(sessionId, null);
        Type type = new Type(Type.SERVICE_TYPE.SPEECH, reqSrvType.getRequestType());

        EventArg eventArg = new EventArg(sipEndType);
        EventReportCall eventRepCall = new EventReportCall(appId, type, eventType.getType(), eventArg);
        Message eventRepRet = appClient.dispatch(eventRepCall.toMessage());
        Action action = new Action(eventRepRet.getIE(CFIE.ACTION_IE));

        logger.debug(String.format(
                "End session report call was executed - sessionId:%s eventType:%s actionResult:%s",
                sessionId, eventType.name(), action.getSessionAction().name()));

        // Report end value.
        Integer fixedEndValue = fixMillisToTenthsSeconds(endValue);
        long watchArg1 = 0L;
        WatchArg watchArg = new WatchArg(fixedEndValue, watchArg1, null, null, null, null);
        WATCH_TYPE typeTimeWatch = WATCH_TYPE.TIME_WATCH;
        WatchReportCall watchRepCall = new WatchReportCall(appId, typeTimeWatch.getType(), null, watchArg);

        Message watchRepRet = appClient.dispatch(watchRepCall.toMessage());
        action = new Action(watchRepRet.getIE(CFIE.ACTION_IE));

        logger.info(String.format("End session report call - sessionId:%s watchType:%s endValue:%s watchArg1:%s actionResponse:%s",
                sessionId, typeTimeWatch.name(), fixedEndValue, watchArg1, action.getSessionAction().name()));
        Name name = new Name(session.getName(), session.getNai());
        String mediaArg0 = getMediaResource(watchRepRet, action, name);

        return new SessionDTO(session, null, action, mediaArg0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReportedSessionDTO doReportWatchSession(String sessionId, Integer timeSpan) throws Exception {
        Parameters.checkNull(sessionId, "sessionToReport");
        Parameters.checkNull(timeSpan, "timeSpan");

        logger.info(String.format("Do watch report session for sessionId:%s",
                sessionId));

        Id appId = new Id(sessionId, null);
        long watchArg1 = 0L;

        Integer fixedTimeSpan = fixMillisToTenthsSeconds(timeSpan);
        WatchArg watchArg = new WatchArg(fixedTimeSpan, watchArg1, null, null, null, null);
        WATCH_TYPE typeTimeWath = WATCH_TYPE.A_TIME_WATCH;
        WatchReportCall watchRepCall = new WatchReportCall(appId, typeTimeWath.getType(), null, watchArg);

        // Report call to CF
        Message watchRepRet = appClient.dispatch(watchRepCall.toMessage());
        Action action = new Action(watchRepRet.getIE(CFIE.ACTION_IE));

        logger.debug(String.format(
                "Watch report call - sessionId:%s watchType:%s timeSpan:%s actionResponse:%s",
                sessionId, typeTimeWath.name(), fixedTimeSpan, action.getSessionAction().name()));

        return new ReportedSessionDTO(watchRepRet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SessionDTO cancelSession(SessionDTO session, boolean callerWasCancelSession, Integer endType, RequestServiceType reqSrvType) throws Exception {
        Parameters.checkNull(session, "session");
        Parameters.checkNull(endType, "endType");
        Parameters.checkNull(reqSrvType, "reqSrvType");
        logger.info(String.format("Cancel session - session:%s endType:%s", session, endType));

        String sessionId = session.getSessionId();
        Id appId = new Id(sessionId, null);
        Type type = new Type(Type.SERVICE_TYPE.SPEECH, reqSrvType.getRequestType());

        EventArg eventArg = new EventArg(endType);
        EventReportCall eventRepCall = new EventReportCall(
                appId,
                type,
                callerWasCancelSession ? Type.EVENT_TYPE.O_ABANDON_1.getType() : Type.EVENT_TYPE.T_ABANDON_1.getType(),
                eventArg);

        Message eventRepRet = appClient.dispatch(eventRepCall.toMessage());
        Action action = new Action(eventRepRet.getIE(CFIE.ACTION_IE));

        logger.info(String.format("Session sessionId:%s was canceled. endType:%s actionResponse:%s",
                sessionId, endType, action.getSessionAction().name()));
        Name name = new Name(session.getName(), session.getNai());
        String mediaArg0 = getMediaResource(eventRepRet, action, name);

        return new SessionDTO(session, null, action, mediaArg0);
    }

    /**
     * @param value Value to fix, it is expressed in milliseconds.
     * @return Fix milliseconds to tenths of seconds.
     */
    private Integer fixMillisToTenthsSeconds(Integer value) {
        return new BigDecimal(value).setScale(0)
                .divide(FIX_MILLISECONDS_FACTOR, RoundingMode.HALF_UP)
                .intValue();
    }
}
