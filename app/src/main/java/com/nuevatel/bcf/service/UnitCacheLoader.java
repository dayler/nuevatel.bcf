package com.nuevatel.bcf.service;

import com.google.common.cache.CacheLoader;
import com.nuevatel.bcf.core.dao.UnitDAO;
import com.nuevatel.bcf.core.domain.Unit;
import com.nuevatel.bcf.core.exception.UnitNotFoundException;
import com.nuevatel.common.util.StringUtils;

/**
 * Load single unit from the database based on its name.
 *
 * @author Ariel Salazar
 */
public class UnitCacheLoader extends CacheLoader<String, Unit> {

    /**
     * Data access object for Unit.
     */
    private UnitDAO unitDAO = new UnitDAO();

    /**
     *
     * @param name Unit name.
     * @return Return the unit to corresponds with the provided name.
     * @throws Exception If the Unit could not be retrieved.
     */
    @Override
    public Unit load(String name) throws Exception {
        if (StringUtils.isBlank(name)) {
            throw new UnitNotFoundException("Empty String or null");
        }

        Unit unit;
        if ((unit = unitDAO.findById(name)) == null) {
            throw new UnitNotFoundException(name);
        }

        return unit;
    }
}
