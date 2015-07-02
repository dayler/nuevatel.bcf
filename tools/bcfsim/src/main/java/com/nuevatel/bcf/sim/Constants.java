package com.nuevatel.bcf.sim;

import java.math.BigDecimal;

/**
 * Created by asalazar on 7/2/15.
 */
public final class Constants {

    /**
     * Fix factor to convert milliseconds to tenths of seconds.
     */
    public static final BigDecimal FIX_MILLISECONDS_FACTOR = new BigDecimal(100);

    private Constants() {
        // No op
    }
}
