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
    failed(0),
    success(1),
    ;
    private Integer value;    

    private EResponseWS(Integer value) {
        this.value = value;
    }
    
    public Integer value(){
        return value;
    }
}
