package com.nuevatel.bcf.service;

import com.google.common.cache.LoadingCache;
import com.nuevatel.bcf.core.domain.Unit;
import com.nuevatel.common.exception.OperationException;

/**
 * Load from database and store it in cache by X time all units.
 *
 * Created by asalazar on 6/5/15.
 */
public interface UnitService {

    /**
     *
     * @param name Name of the unit to find.
     * @return The Unit to corresponds with 'name'. <b>null</b> if the Unit does not exists.
     * @throws OperationException If the Unit could not be retrieved.
     */
    Unit getUnit(String name) throws OperationException;

    /**
     * Removes the unit to corresponds with 'name'.
     * @param name Name of unit to remove.
     */
    void invalidate(String name);

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
    void setCacheEngine(LoadingCache<String, Unit> cache);
}
