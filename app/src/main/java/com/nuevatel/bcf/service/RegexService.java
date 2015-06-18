package com.nuevatel.bcf.service;

import com.google.common.cache.LoadingCache;
import com.nuevatel.bcf.domain.Regex;
import com.nuevatel.common.exception.OperationException;

/**
 * Load from database and store it in cache the Regex.
 *
 * @author Ariel Salazar
 */
public interface RegexService {

    /**
     *
     * @param regexId Id to identify the Regex.
     * @return Regex to corresponds with regexId. <b>regexId=null</b> returns <b>null</b>.
     * @throws OperationException If an error occurred while Regex is retrieving.
     */
    Regex getRegex(Integer regexId) throws OperationException;

    /**
     * Removes the Regex of the cache.
     * @param regexId Id to idnetify the Regex.
     */
    void invalidate(Integer regexId);

    /**
     *
     * @return Approximate size of the cache.
     */
    long getSize();

    /**
     * Clear all elements of the cache.
     */
    void clear();

    /**
     * @param cache LoadingCache to use.
     */
    void setCacheEngine(LoadingCache<Integer, Regex> cache);
}
