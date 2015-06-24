/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nuevatel.bcf.core.domain;

/**
 *
 * @author clvelarde
 */
public class UnitRecord implements Record {
    
    private String name;

    private Integer regexId;

    public UnitRecord() {
    }

    public UnitRecord(String name, Integer regexId) {
        this.name = name;
        this.regexId = regexId;
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
}
