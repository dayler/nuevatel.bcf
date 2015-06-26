package com.nuevatel.bcf.core;

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

    bcf_logrecorder_size("bcf.logrecorder.size"),

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
    
    /**
     * Property wsi.connection.bindAddress
     */
    wsi_connection_bindAddress("wsi.connection.bindAddress"),
    /**
     * Property wsi.connection.port
     */
    wsi_connection_port("wsi.connection.port"),
    
    /**
     * Property wsi.connection.backlog
     */
    wsi_connection_backlog("wsi.connection.backlog"),
    ;

    private String propName;

    private PropName(String propName) {
        this.propName = propName;
    }

    public String property() {
        return propName;
    }
}
