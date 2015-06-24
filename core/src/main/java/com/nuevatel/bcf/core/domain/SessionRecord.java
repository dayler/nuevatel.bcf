package com.nuevatel.bcf.core.domain;

import java.util.Date;

/**
 * Entity for <b>bcf_record.session_record</b>
 *
 * @author Ariel Salazar
 */
public class SessionRecord implements Record {

    /**
     * NUll to indicate new register to insert.
     */
    private String id = null;

    private String name;

    private Integer regexId = null;

    private Integer newMediaId = null;

    private Integer endMediaId = null;

    private Integer swapId = null;

    private Date startTimestamp = null;

    private Date endTimestamp = null;

    private Integer respCode = null;

    private Flag flag = Flag.none;

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

    public void setRespCode(Integer respCode) {
        this.respCode = respCode;
    }

    public Integer getRespCode() {
        return respCode;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public Flag getFlag() {
        return flag;
    }

    public enum Flag {
        insert,
        update,
        updateEndTimestamp,
        none,
        ;
    }
}
