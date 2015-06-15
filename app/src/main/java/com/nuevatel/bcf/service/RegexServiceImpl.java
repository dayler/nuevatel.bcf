package com.nuevatel.bcf.service;

import com.google.common.cache.LoadingCache;
import com.nuevatel.bcf.domain.Regex;
import com.nuevatel.bcf.exception.RegexNotFoundException;
import com.nuevatel.common.exception.OperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by asalazar on 6/8/15.
 */
class RegexServiceImpl implements RegexService {

    private static Logger logger = LogManager.getLogger(RegexServiceImpl.class);

    private LoadingCache<Integer, Regex>cache;

    @Override
    public Regex getRegex(Integer regexId) throws OperationException {
        if (regexId == null) {
            return null;
        }

        try {
            return cache.get(regexId);
        } catch (Throwable ex) {
            if (ex.getCause() != null && ex.getCause() instanceof RegexNotFoundException) {
                logger.warn(ex.getCause().getMessage());
                return null;
            }

            String msg = String.format("Failed to get Regex for '%s'", regexId);
            throw new OperationException(msg, ex);
        }
    }

    @Override
    public void invalidate(Integer regexId) {
        cache.invalidate(regexId);

    }

    @Override
    public long getSize() {
        return cache.size();
    }

    @Override
    public void clear() {
        cache.invalidateAll();
    }

    @Override
    public void setCacheEngine(LoadingCache<Integer, Regex> cache) {
        this.cache = cache;
    }
}
