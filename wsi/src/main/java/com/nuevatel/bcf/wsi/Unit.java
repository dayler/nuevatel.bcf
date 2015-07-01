/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nuevatel.bcf.wsi;

import com.nuevatel.bcf.core.Constants;
import com.nuevatel.bcf.core.dao.UnitDAO;
import com.nuevatel.bcf.core.domain.WSIRecord;
import com.nuevatel.bcf.core.entity.EResponseWS;
import com.nuevatel.bcf.service.LogRecorderServiceFactory;
import com.nuevatel.bcf.service.UnitServiceFactory;
import com.nuevatel.common.util.StringUtils;
import com.nuevatel.common.util.IntegerUtil;
import com.sun.net.httpserver.HttpExchange;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.sql.SQLException;
import java.util.Date;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author clvelarde
 */
@WebService(name="UnitWSIPort", serviceName="UnitWSI", portName="UnitWSIPort")
public class Unit {

    private static Logger logger = LogManager.getLogger(Unit.class);
    
    private LogRecorderServiceFactory factory = new LogRecorderServiceFactory();
    
    private UnitDAO unitDAO = new UnitDAO();

    private UnitServiceFactory unitServiceFactory = new UnitServiceFactory();
    
    @Resource
    private WebServiceContext wsContext;
    
    private static final String 
            HTTP_EXCHANGE = "com.sun.xml.ws.http.exchange";
    
    @WebMethod
    public String lock( @WebParam (name = "name") String name,
                        @WebParam (name = "regexId")  String regexId) {
        logger.info("lock");
        
        Integer intRegexId = IntegerUtil.tryParse(regexId);   
        if( intRegexId == null
            || StringUtils.isEmptyOrNull(name)
            || IntegerUtil.tryParse(name) == null) {
            logger.info(
                    String.format("Params name =%s and regexId=%s do not have correct values",
                    name,
                    regexId));
            return "FAILED";
        }
        
        try {
            if(unitDAO.existsUnitForNameAndRegexId(name, intRegexId)) {
                logger.info(String.format("This unit with name=%s and regexId=%s is already locked",
                            name,
                            regexId));
                return "NOT_NULL_UNITNAME";
            }

            com.nuevatel.bcf.core.domain.Unit unit = new com.nuevatel.bcf.core.domain.Unit(name);
            unit.addRegexId(intRegexId);
            unit.addTimespan(intRegexId, new Date(), null);
            unitDAO.insert(unit);
            logger.info(
                    String.format("Successful, unit with name=%s and regexId=%s was locked",
                            name,
                            regexId));

            saveWSIRecord(name, intRegexId, Constants.LOCK_NAME, getClientIPAddr(), EResponseWS.success);
            unitServiceFactory.getCache().refresh(name);
            return "OK";
            
        } catch (SQLException ex) {
            logger.error(
                    String.format("Unit name =%s and regexId=%s can not be inserted into database",
                    name,
                    regexId),
                    ex);
            saveWSIRecord(name, intRegexId, Constants.LOCK_NAME, getClientIPAddr(), EResponseWS.failed);
            return "FAILED";
        }
    }
    
    @WebMethod
    public String unlock(   @WebParam (name = "name") String name,
                            @WebParam (name = "regexId") String regexId) {
        logger.info("unlock");
        
        Integer intRegexId = IntegerUtil.tryParse(regexId);        
        if( intRegexId == null
            || StringUtils.isEmptyOrNull(name)
            || IntegerUtil.tryParse(name) == null) {
            logger.info(
                    String.format("Params name =%s and regexId=%s do not have correct values",
                    name,
                    regexId));
            return "FAILED";
        }
        
        try {
            if (unitDAO.existsUnitForNameAndRegexId(name, intRegexId)) {
                unitDAO.deleteByNameAndRegexId(name, intRegexId);
                logger.info(
                    String.format("Successful, unit with name=%s and regexId=%s was unlocked",
                    name,
                    regexId));
                
                saveWSIRecord(name, intRegexId, Constants.UNLOCK_NAME, getClientIPAddr(), EResponseWS.success);
                unitServiceFactory.getCache().refresh(name);
                return "OK";
            }
            logger.info(
                    String.format("This unit with name=%s and regexId=%s is not locked",
                    name,
                    regexId));
            return "NULL_UNITNAME";
            
        } catch (SQLException ex) {
            logger.error(
                    String.format("Unit name =%s and regexId=%s can not be inserted into database",
                            name,
                            regexId),
                    ex);
            saveWSIRecord(name, intRegexId, Constants.UNLOCK_NAME, getClientIPAddr(), EResponseWS.failed);
            return "FAILED";
        }
    }

    private void saveWSIRecord(String name, Integer regex_id, String action, String fromIpAddr, EResponseWS response) {
        WSIRecord wsiRec =
                new WSIRecord(name, regex_id, action, fromIpAddr, response);

        factory.get().appendRecord(wsiRec);        
    }
    
    private String getClientIPAddr() {
        MessageContext mc = wsContext.getMessageContext();
        HttpExchange exchange = (HttpExchange) mc.get(HTTP_EXCHANGE);
        String ip = exchange.getRemoteAddress().getAddress().getHostAddress();

        return ip;
    }
}
