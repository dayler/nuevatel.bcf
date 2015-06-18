package com.nuevatel.bcf.service;

import com.google.common.cache.LoadingCache;
import com.nuevatel.bcf.domain.Unit;
import com.nuevatel.bcf.exception.UnitNotFoundException;
import com.nuevatel.common.exception.OperationException;
import com.nuevatel.common.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutionException;

/**
 * Created by asalazar on 6/6/15.
 */
class UnitServiceImpl implements UnitService {

    private static Logger logger = LogManager.getLogger(UnitService.class);

    private LoadingCache<String, Unit>cache;

    @Override
    public Unit getUnit(String name) throws OperationException{
        if (StringUtils.isBlank(name)) {
            return null;
        }

        try {
            return cache.get(name);
        } catch (Throwable ex) {
            if (ex.getCause() != null && ex.getCause() instanceof UnitNotFoundException) {
                logger.warn(ex.getCause().getMessage());
                return null;
            }

            String msg = String.format("Failed to get Unit for '%s'", name);
            throw new OperationException(msg);
        }
    }

    @Override
    public void invalidate(String name) {
        if (StringUtils.isBlank(name)) {
            return;
        }

        cache.invalidate(name);
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
    public void setCacheEngine(LoadingCache<String, Unit> cache) {
        this.cache = cache;
    }
}
