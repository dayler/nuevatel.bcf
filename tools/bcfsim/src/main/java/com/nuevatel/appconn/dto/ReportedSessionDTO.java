package com.nuevatel.appconn.dto;

import com.nuevatel.base.appconn.IE;
import com.nuevatel.base.appconn.Message;
import com.nuevatel.cf.appconn.Action;
import com.nuevatel.cf.appconn.CFIE;
import com.nuevatel.cf.appconn.WatchArg;
import com.nuevatel.common.util.Parameters;

import static com.nuevatel.bcf.sim.Constants.*;

/**
 * Event response for WatchEventReportCall.
 *
 * @author asalazar
 */
public class ReportedSessionDTO {

    /**
     * Period for executing watch report call. It is in milliseconds. if it is null indicates that
     * is not possible get more holds for the unit.
     */
    private Integer watchPeriod;

    /**
     * Time to end the call. It is in milliseconds. It is different to null when the user do not
     * have more credit to do holds.
     */
    private Integer watchOffset;

    private boolean accepted;

    /**
     * ReportedSessionDTO Constructor. Gets from <b>EventReportRet</b> the period and offset time.
     *
     * @param message <b>EventReportRet</b> message.
     */
    public ReportedSessionDTO(Message message) {
        Parameters.checkNull(message, "message");

        IE rawWatchArg = message.getIE(CFIE.WATCH_ARG_IE);
        Action action = new Action(message.getIE(CFIE.ACTION_IE));
        accepted = Action.SESSION_ACTION.ACCEPT == action.getSessionAction();

        if (!accepted) {
            throw new IllegalStateException(String.format("Session was not accepted. Acction: %s", action));
        }

        if (rawWatchArg != null) {
            WatchArg watchArg = new WatchArg(rawWatchArg);

            Integer watchArg0 = watchArg.getWatchArg0();
            watchPeriod = watchArg0 == null ? null : watchArg0 * FIX_MILLISECONDS_FACTOR.intValueExact();

            Integer watchArg5 = watchArg.getWatchArg5();
            watchOffset = watchArg5 == null ? null : watchArg5 * FIX_MILLISECONDS_FACTOR.intValueExact();
        }
    }

    /**
     * It is in milliseconds. if it is null indicates that is not possible get more holds for the unit
     *
     * @return Period for executing watch report call.
     */
    public Integer getWatchPeriod() {
        return watchPeriod;
    }

    /**
     * It is in milliseconds. It is different to null when the user do not have more credit to do holds.
     *
     * @return Time to end the call
     */
    public Integer getWatchOffset() {
        return watchOffset;
    }

    /**
     * @return the accepted
     */
    public boolean isAccepted() {
        return accepted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("ReportedSessionDTO{watchPeriod:%s watchOffset:%s}", watchPeriod, watchOffset);
    }
}
