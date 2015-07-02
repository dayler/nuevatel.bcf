/**
 * 
 */
package com.nuevatel.appconn.dto;

import com.nuevatel.cf.appconn.Type.REQUEST_TYPE;
import com.nuevatel.common.util.StringUtils;

/**
 * Define the session kind to create in the platform.
 * 
 * @author asalazar
 *
 */
public enum RequestServiceType {
    /**
     * Outgoing speech session.
     */
    speechO(REQUEST_TYPE.O),
    /**
     * Incoming speech session.
     */
    speechT(REQUEST_TYPE.T),
    speechFwo(REQUEST_TYPE.FWO),
    speechFwt(REQUEST_TYPE.FWT),
    messageSmO(REQUEST_TYPE.PDPCE),
    messageSmT(REQUEST_TYPE.PDPCEA),
    dataPdpce(REQUEST_TYPE.PDPCHP),
    dataPdpcea(REQUEST_TYPE.DCCA),
    ;

    /**
     * Request type selected.
     */
    private REQUEST_TYPE requestType;

    /**
     * RequestServiceType  Constructor.
     * 
     * @param requestType Request service type.
     */
    private RequestServiceType(REQUEST_TYPE requestType) {
        this.requestType = requestType;
    }

    /**
     * 
     * @return Selected request servoce type.
     */
    public REQUEST_TYPE getRequestType() {
        return requestType;
    }

    /**
     * Safe value off.
     * 
     * @param name The name to looking by RequestServiceType
     * @param defaultValue Default value to return if is not found RequestServiceType
     * @return The service type to match with <b>name</b>, if it does not match return <b>defaultValue</b>
     */
    public static RequestServiceType valueOf(String name, RequestServiceType defaultValue) {
        if (StringUtils.isBlank(name)) {
            return defaultValue;
        }

        try {
            return RequestServiceType.valueOf(name);
        } catch (Throwable ex) {
            // Util class. No logs need here.
            return defaultValue;
        }
    }
}
