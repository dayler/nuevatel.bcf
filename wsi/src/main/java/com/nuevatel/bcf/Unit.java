/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nuevatel.bcf;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 *
 * @author clvelarde
 */
@WebService(name="UnitWSIPort", serviceName="UnitWSI", portName="UnitWSIPort")
public class Unit {
    
    @WebMethod
    public String lock(String unit, int regexId) {
        return "ok";
    }
    
//    @WebMethod
//    public String unlock(String unit, int regexId) {
//        return "ok";
//    }
}
