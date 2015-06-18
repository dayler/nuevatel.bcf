package com.nuevatel.bcf;

import com.nuevatel.cf.appconn.Name;
import com.nuevatel.cf.appconn.SessionArg;
import com.nuevatel.cf.appconn.Type;

/**
 * Transparent and default implmentation for NameGetter.
 *
 * @author Ariel Salazar
 */
public class NameGetter10 implements NameGetter {

    public static final int COUNTRY_CODE_LENGTH = 3;

    public static final String COUNTRY_CODE = "591";

    /**
     * {@inheritDoc}
     */
    @Override
    public Name getSessionName(Type type, Name name) {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Name getToSessionName(Type type, Name name, SessionArg args) {
        return args.getToName();
    }
}
