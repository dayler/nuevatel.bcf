package com.nuevatel.bcf.core.domain;

import java.util.Date;

/**
 * Entity for <b>bcf_record.session_record</b>
 *
 * @author Ariel Salazar
 */
public class SessionRecord {

    private String id;

    private String name;

    private Integer regexId = null;

    private Integer newMediaId = null;

    private Integer endMediaId = null;

    private Integer swapId = null;

    private Date startTimestamp = null;

    private Date endTimestamp = null;

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

    public Integer getRegexId() {
        return regexId;
    }

    public void setRegexId(Integer regexId) {
        this.regexId = regexId;
    }

    public Integer getNewMediaId() {
        return newMediaId;
    }

    public void setNewMediaId(Integer newMediaId) {
        this.newMediaId = newMediaId;
    }

    public Integer getEndMediaId() {
        return endMediaId;
    }

    public void setEndMediaId(Integer endMediaId) {
        this.endMediaId = endMediaId;
    }

    public Integer getSwapId() {
        return swapId;
    }

    public void setSwapId(Integer swapId) {
        this.swapId = swapId;
    }

    public Date getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Date startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Date getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Date endTimestamp) {
        this.endTimestamp = endTimestamp;
    }
}
