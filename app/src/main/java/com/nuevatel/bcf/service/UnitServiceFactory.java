package com.nuevatel.bcf.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.nuevatel.bcf.domain.Unit;

/**
 * Created by asalazar on 6/6/15.
 */
public final class UnitServiceFactory extends AbstractCacheServiceFactory<UnitService, String, Unit> {

    private static UnitService service = null;

    public UnitServiceFactory() {
        // No op
    }

    /**
     *
     * @param service Initial value for UnitServiceFactory.service
     */
    public UnitServiceFactory(UnitService service) {
        UnitServiceFactory.service = service;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized UnitService getCache() {
        if (service != null) {
            return service;
        }
        service = new UnitServiceImpl();
        CacheBuilder<Object, Object>cacheBuilder = getCacheBuilder();
        //CacheLoader<String, Unit>
        LoadingCache<String, Unit>cache = cacheBuilder.build(cacheLoader);
        service.setCacheEngine(cache);
        return service;
    }
}
