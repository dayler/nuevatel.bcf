package com.nuevatel.bcf.core.domain;

import com.nuevatel.bcf.core.entity.EResponseWS;

/**
 * Created by asalazar on 6/23/15.
 */
public class WSIRecord implements Record {

    /**
     * NUll to indicate new register to insert.
     */
    private String id = null;

    private String name = null;
    
    private Integer regex_id = null;

    private String action = null;

    private String fromIpAddr = null;

    private EResponseWS response = null;

    public WSIRecord() {
        // No op
    }

    public WSIRecord(String name, Integer regexId, String action, String fromIpAddr, EResponseWS response) {
        this.name = name;
        this.regex_id = regexId;
        this.action = action;
        this.fromIpAddr = fromIpAddr;
        this.response = response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRegex_id() {
        return regex_id;
    }

    public void setRegex_id(Integer regex_id) {
        this.regex_id = regex_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFromIpAddr() {
        return fromIpAddr;
    }

    public void setFromIpAddr(String fromIpAddr) {
        this.fromIpAddr = fromIpAddr;
    }

    public EResponseWS getResponse() {
        return response;
    }

    public void setResponse(EResponseWS response) {
        this.response = response;
    }
}
