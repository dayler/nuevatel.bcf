package com.nuevatel.bcf.service;

import com.google.common.cache.CacheLoader;
import com.nuevatel.bcf.core.dao.RegexDAO;
import com.nuevatel.bcf.core.domain.Regex;
import com.nuevatel.bcf.core.exception.RegexNotFoundException;

/**
 * Load single Regex object from the database.
 *
 * @author Ariel Salazar
 */
public class RegexCacheLoader extends CacheLoader<Integer, Regex> {

    private static RegexDAO regexDAO = new RegexDAO();

    /**
     *
     * @param regexId Id to identify the RegexId.
     * @return The unit to corresponds with the regexId.
     * @throws Exception If the Regex could not be retrieved.
     */
    @Override
    public Regex load(Integer regexId) throws Exception {
        if (regexId == null) {
            throw new RegexNotFoundException(regexId);
        }

        Regex regex = regexDAO.findById(regexId);
        if (regex == null) {
            throw new RegexNotFoundException(regexId);
        }
        return regex;
    }
}
