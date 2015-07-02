package com.nuevatel.appconn.proxy;

import com.nuevatel.appconn.dto.ReportedSessionDTO;
import com.nuevatel.appconn.dto.RequestServiceType;
import com.nuevatel.appconn.dto.SessionDTO;

/**
 * Implement at high level the interface to communicate with VONE through APPCON.
 *
 * @author asalazar
 */
public interface AppConnProxy {

    /**
     * @param createSessionDTO Data model for creating session.
     * @return Created session through APPCONN
     * @throws Exception If the session could not be created.
     */
    public SessionDTO createSession(SessionDTO createSessionDTO) throws Exception;

    /**
     * Starts a session. It indicate to VONE that the communication between units was begun.
     *
     * @param sessionId Session to start.
     * @param reqSrvType Define the request service. <b>The session is incoming or outgoing</b>.
     * @return {@link ReportedSessionDTO} to belongs the session.
     * @throws Exception If the session could not be started.
     * @see RequestServiceType
     */
    ReportedSessionDTO startSession(String sessionId, RequestServiceType reqSrvType) throws Exception;

    /**
     * 
     * @param session DTO of the session to ends.
     * @param callerWasEndSession <b>true</b> if the session was ended by the caller. <b>false</b>
     *                            in other case.
     * @param endValue End value to report, it is expressed in milliseconds.
     * @param reqSrvType Define the request service. <b>The session is incoming or outgoing</b>.
     * @param endType Response code value. it is based on HTTP, 200 -> indicates everything OK.
     * @return {@link SessionDTO} response.
     * @throws Exception If an error occurred when the session is finalizing.
     * @see RequestServiceType
     */
    SessionDTO endSession(SessionDTO session, boolean callerWasEndSession, Integer endValue, RequestServiceType reqSrvType, Integer endType) throws Exception;

    /**
     * Cancel session between units. It occurred when when is needed to force close the session.
     * The reason needs to be specified in the <b>endType</b> parameter using HTTP reponses codes.
     * 
     * @param session Session to cancel.
     * @param callerWasCancelSession <b>true</b> if the session was canceled by the caller. <b>false</b>
     *                            in other case.
     * @param endType Response code based on HTTP responses codes.
     * @param reqSrvType TypeDefine the request service. <b>The session is incoming or outgoing</b>.
     * @return Return the {@link SessionDTO} response.
     * @throws Exception If an error occurs when the session is canceled.
     * @see {@link RequestServiceType}
     */
    SessionDTO cancelSession(SessionDTO session, boolean callerWasCancelSession, Integer endType, RequestServiceType reqSrvType) throws Exception;

    /**
     * Do reserve into VONE. Retrieve new values for <b>period</b> and <b>offset</b> time.
     *
     * @param sessionId Session to request reserve.
     * @param timeSpan Elapsed time of the session.
     * @return DTO for the reported session.
     * @throws Exception If an error occurs when the reserve is processing.
     */
    ReportedSessionDTO doReportWatchSession(String sessionId, Integer timeSpan) throws Exception;
}
