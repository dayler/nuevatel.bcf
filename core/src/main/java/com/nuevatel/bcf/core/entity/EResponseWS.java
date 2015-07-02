/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nuevatel.bcf.core.entity;

/**
 *
 * @author clvelarde
 */
public enum EResponseWS {
    SUCCESSFUL(0),
    INVALID_PARAMETERS(-1),
    INVALID_NAME(-2),
    INVALID_REGEX_ID(-3),
    FAILED_INSERT_UNIT(-4),
    NULL_UNITNAME(-5),
    NOT_NULL_UNITNAME(-6),
    ;
    private Integer value;    

    private EResponseWS(Integer value) {
        this.value = value;
    }
    
    public Integer value(){
        return value;
    }
}
