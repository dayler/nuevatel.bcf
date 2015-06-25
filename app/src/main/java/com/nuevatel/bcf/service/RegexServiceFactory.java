package com.nuevatel.bcf.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.nuevatel.bcf.core.domain.Regex;

/**
 * Created by asalazar on 6/8/15.
 */
public class RegexServiceFactory extends AbstractCacheServiceFactory<RegexService, Integer, Regex> {

    private static RegexService service = null;

    public RegexServiceFactory() {
        // No op
    }

    /**
     *
     * @param service Initial value for RegexServiceFactory.service
     */
    public RegexServiceFactory(RegexService service) {
        RegexServiceFactory.service = service;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized RegexService getCache() {
        if (service != null) {
            return service;
        }
        service = new RegexServiceImpl();
        CacheBuilder<Object, Object>cacheBuilder = getCacheBuilder();
        // CacheLoader<Integer,Regex>
        LoadingCache<Integer, Regex>cache = cacheBuilder.build(cacheLoader);
        service.setCacheEngine(cache);
        return service;
    }
}
