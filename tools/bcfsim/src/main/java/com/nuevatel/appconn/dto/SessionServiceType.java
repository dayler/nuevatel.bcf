/**
 *
 */
package com.nuevatel.appconn.dto;

import com.nuevatel.cf.appconn.Type.SERVICE_TYPE;

/**
 * Wrapper for {@link SERVICE_TYPE};
 *
 * @author asalazar
 */
public enum SessionServiceType {
    SPEECH(SERVICE_TYPE.SPEECH),
    VIDEO(SERVICE_TYPE.VIDEO),
    DATA(SERVICE_TYPE.DATA),
    MESSAGE(SERVICE_TYPE.MESSAGE);

    private SERVICE_TYPE serviceType;

    private SessionServiceType(SERVICE_TYPE serviceType) {
        this.serviceType = serviceType;
    }

    public SERVICE_TYPE getServiceType() {
        return serviceType;
    }
}
