/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nuevatel.bcf;

import com.nuevatel.common.util.StringUtils;
import com.nuevatel.common.util.IntegerUtil;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author clvelarde
 */
@WebService(name="UnitWSIPort", serviceName="UnitWSI", portName="UnitWSIPort")
public class Unit {
    
    @WebMethod
    public String lock( @WebParam (name = "name") String unit,
                        @WebParam (name = "regexId")  String regexId) {
        
        Integer intRegexId = IntegerUtil.tryParse(regexId);
        
        if( intRegexId == null
            || StringUtils.isEmptyOrNull(unit)
            || IntegerUtil.tryParse(unit) == null) {
            return "FAILED";
        }
        return "OK";
    }
    
    @WebMethod
    public String unlock(   @WebParam (name = "name") String unit,
                            @WebParam (name = "regexId") String regexId) {
        Integer intRegexId = IntegerUtil.tryParse(regexId);
        
        if( intRegexId == null
            || StringUtils.isEmptyOrNull(unit)
            || IntegerUtil.tryParse(unit) == null) {
            return "FAILED";
        }
        return "OK";
    }
}
