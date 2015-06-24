package com.nuevatel.bcf.core.domain;

import java.util.Date;

/**
 * Created by asalazar on 6/23/15.
 */
public class WSIRecord implements Record {

    private String id = null;

    private String name = null;

    private String action = null;

    private String fromIpAddr = null;

    private Date creationTimestamp = null;

    private Integer response = null;

    public WSIRecord() {
        // No op
    }

    public WSIRecord(String name, String action, String fromIpAddr, Date creationTimestamp, Integer response) {
        this.name = name;
        this.action = action;
        this.fromIpAddr = fromIpAddr;
        this.creationTimestamp = creationTimestamp;
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

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Integer getResponse() {
        return response;
    }

    public void setResponse(Integer response) {
        this.response = response;
    }
}
