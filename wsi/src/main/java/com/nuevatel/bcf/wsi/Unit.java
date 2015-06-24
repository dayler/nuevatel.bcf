/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nuevatel.bcf.wsi;

import com.nuevatel.bcf.core.Constants;
import com.nuevatel.bcf.core.dao.UnitDAO;
import com.nuevatel.bcf.core.domain.UnitRecord;
import com.nuevatel.bcf.core.domain.WSIRecord;
import com.nuevatel.bcf.service.LogRecorderServiceFactory;
import com.nuevatel.common.util.StringUtils;
import com.nuevatel.common.util.IntegerUtil;
import com.nuevatel.common.util.Pair;

import java.sql.SQLException;
import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author clvelarde
 */
@WebService(name="UnitWSIPort", serviceName="UnitWSI", portName="UnitWSIPort")
public class Unit {
    
    private LogRecorderServiceFactory factory = new LogRecorderServiceFactory();
    private UnitDAO unitDao = new UnitDAO();
    
    @WebMethod
    public String lock( @WebParam (name = "name") String name,
                        @WebParam (name = "regexId")  String regexId) {
        
        Integer intRegexId = IntegerUtil.tryParse(regexId);
        
        if( intRegexId == null
            || StringUtils.isEmptyOrNull(name)
            || IntegerUtil.tryParse(name) == null) {
            return "FAILED";
        }
        
        Pair<String,Integer> key = new Pair(name,intRegexId);
        UnitRecord unitRec;
        try {
            unitRec = unitDao.findById(key);
            if(unitRec != null){
                return "NOT_NULL_UNITNAME";
            }
            unitRec = new UnitRecord(name, intRegexId);
            unitDao.insert(unitRec);
            WSIRecord wsiRec = new WSIRecord(name, Constants.LOCK_NAME, "10.1.1.1", new Date(), 0);
            factory.get().appendRecord(wsiRec);
            return "OK";
            
        } catch (SQLException ex) {
            return "FAILED";
        }        
    }
    
    @WebMethod
    public String unlock(   @WebParam (name = "name") String name,
                            @WebParam (name = "regexId") String regexId) {
        Integer intRegexId = IntegerUtil.tryParse(regexId);
        
        if( intRegexId == null
            || StringUtils.isEmptyOrNull(name)
            || IntegerUtil.tryParse(name) == null) {
            return "FAILED";
        }
        Pair<String,Integer> key = new Pair(name,intRegexId);
        UnitRecord unitRec;
        try {
            unitRec = unitDao.findById(key);
            if (unitRec != null) {
                unitDao.delete(key);
                WSIRecord wsiRec = new WSIRecord(name, Constants.UNLOCK_NAME, "10.1.1.1", new Date(), 1);
                factory.get().appendRecord(wsiRec);
                return "OK";
            }
            return "NULL_UNITNAME";
            
        } catch (SQLException ex) {
            return "FAILED";
        }
    }
    
    @WebMethod
    public String dummy() {
        return "ok";
    }
    
    
}
