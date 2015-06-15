package com.nuevatel.core;

/**
 * @author Ariel Salazar
 */
public enum PropName {
    /**
     * Application id for BCF
     * <br/>
     * Property bcf.id
     */
    id("bcf.id"),
    /**
     * Property bcf.getter
     */
    getter("bcf.getter"),

    bcf_service_media_size("bcf.service.media.size"),

    /**
     * Property cache.service.concurrencyLevel
     */
    cache_service_concurrencyLevel("cache.service.concurrencyLevel"),
    /**
     * Property cache.service.expireAfterWrite
     */
    cache_service_expireAfterWrite("cache.service.expireAfterWrite"),
    /**
     * Property cache.service.expireAfterAccess
     */
    cache_service_expireAfterAccess("cache.service.expireAfterAccess"),

    /**
     * Property appconn.bindAddress
     */
    appconn_bindAddress("appconn.bindAddress"),
    /**
     * Property appconn.port
     */
    appconn_port("appconn.port"),
    /**
     * Property appconn.sources
     */
    appconn_sources("appconn.sources"),
    ;

    private String propName;

    private PropName(String propName) {
        this.propName = propName;
    }

    public String property() {
        return propName;
    }
}
