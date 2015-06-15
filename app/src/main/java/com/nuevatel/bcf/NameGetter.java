package com.nuevatel.bcf;

import com.nuevatel.cf.appconn.Name;
import com.nuevatel.cf.appconn.SessionArg;
import com.nuevatel.cf.appconn.Type;

/**
 * Strategy to get the Name of the Unit.
 */
public interface NameGetter {

    /**
     *
     * @param type
     * @param name
     * @return Get session name.
     */
    Name getSessionName(Type type, Name name);

    /**
     *
     * @param type
     * @param name
     * @param args
     * @return Get to session name.
     */
    Name getToSessionName(Type type, Name name, SessionArg args);
}
