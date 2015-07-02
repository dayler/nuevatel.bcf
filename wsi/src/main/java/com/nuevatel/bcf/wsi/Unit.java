package com.nuevatel.bcf.wsi;

import com.nuevatel.bcf.core.Constants;
import com.nuevatel.bcf.core.dao.UnitDAO;
import com.nuevatel.bcf.core.domain.WSIRecord;
import com.nuevatel.bcf.core.entity.EResponseWS;
import com.nuevatel.bcf.service.LogRecorderServiceFactory;
import com.nuevatel.bcf.service.UnitServiceFactory;
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

        if (validateParameters(name, regexId, Constants.LOCK_NAME)) {
            return EResponseWS.INVALID_PARAMETERS.name();
        }

        if (validateNameParam(name, regexId, Constants.LOCK_NAME)) {
            return EResponseWS.INVALID_NAME.name();
        }

        if (validateRegexIdParam(name, regexId, Constants.LOCK_NAME)) {
            return EResponseWS.INVALID_REGEX_ID.name();
        }
        
        try {
            if(unitDAO.existsUnitForNameAndRegexId(name, intRegexId)) {
                saveWSIRecord(  name,
                                intRegexId,
                                Constants.LOCK_NAME,
                                getClientIPAddr(),
                                EResponseWS.NOT_NULL_UNITNAME);
                logger.info(String.format("NOT_NULL_UNITNAME, This unit with name=%s and regexId=%s is already locked",
                            name,
                            regexId));
                return EResponseWS.NOT_NULL_UNITNAME.name();
            }

            com.nuevatel.bcf.core.domain.Unit unit = new com.nuevatel.bcf.core.domain.Unit(name);
            unit.addRegexId(intRegexId);
            unit.addTimespan(intRegexId, new Date(), null);
            unitDAO.insert(unit);
            logger.info(
                    String.format("SUCCESSFUL, unit with name=%s and regexId=%s was locked",
                            name,
                            regexId));
            saveWSIRecord(  name,
                            intRegexId,
                            Constants.LOCK_NAME,
                            getClientIPAddr(),
                            EResponseWS.SUCCESSFUL);
            unitServiceFactory.getCache().refresh(name);
            return EResponseWS.SUCCESSFUL.name();

        } catch (SQLException ex) {
            logger.error(
                    String.format("FAILED_INSERT_UNIT, name =%s and regexId=%s can not be inserted into database",
                    name,
                    regexId),
                    ex);
            saveWSIRecord(  name,
                            intRegexId,
                            Constants.LOCK_NAME,
                            getClientIPAddr(),
                            EResponseWS.FAILED_INSERT_UNIT);
            return  EResponseWS.FAILED_INSERT_UNIT.name();
        }
    }
   
    @WebMethod
    public String unlock(   @WebParam (name = "name") String name,
                            @WebParam (name = "regexId") String regexId) {
        logger.info("unlock");        
        Integer intRegexId = IntegerUtil.tryParse(regexId);     
        
        if (validateParameters(name, regexId, Constants.UNLOCK_NAME)) {
            return EResponseWS.INVALID_PARAMETERS.name();
        }

        if (validateNameParam(name, regexId, Constants.UNLOCK_NAME)) {
            return EResponseWS.INVALID_NAME.name();
        }

        if (validateRegexIdParam(name, regexId, Constants.UNLOCK_NAME)) {
            return EResponseWS.INVALID_REGEX_ID.name();
        }

        if (intRegexId == null) {
            logger.info(
                    String.format("INVALID REGEX ID, regexId=%s does not have correct value",
                            name,
                            regexId));
            saveWSIRecord(  name,
                            intRegexId,
                            Constants.LOCK_NAME,
                            getClientIPAddr(),
                            EResponseWS.INVALID_REGEX_ID);
            return EResponseWS.INVALID_REGEX_ID.name();
        }

        try {
            if (unitDAO.existsUnitForNameAndRegexId(name, intRegexId)) {
                unitDAO.deleteByNameAndRegexId(name, intRegexId);
                logger.info(
                    String.format("SUCCESSFUL, unit with name=%s and regexId=%s was unlocked",
                    name,
                    regexId));
                
                saveWSIRecord(  name,
                                intRegexId,
                                Constants.UNLOCK_NAME,
                                getClientIPAddr(),
                                EResponseWS.SUCCESSFUL);
                unitServiceFactory.getCache().invalidate(name);
                return EResponseWS.SUCCESSFUL.name();
            }
            saveWSIRecord(  name,
                            intRegexId,
                            Constants.UNLOCK_NAME,
                            getClientIPAddr(),
                            EResponseWS.NULL_UNITNAME);
            logger.info(
                    String.format("NULL_UNITNAME name=%s and regexId=%s is not locked",
                    name,
                    regexId));            
            return EResponseWS.NULL_UNITNAME.name();
            
        } catch (SQLException ex) {
            logger.error(
                    String.format("FAILED_INSERT_UNIT, name =%s and regexId=%s can not be inserted into database",
                            name,
                            regexId),
                    ex);
            saveWSIRecord(  name,
                            intRegexId, Constants.UNLOCK_NAME,
                            getClientIPAddr(),
                            EResponseWS.FAILED_INSERT_UNIT);
            return EResponseWS.FAILED_INSERT_UNIT.name();
        }
    }
    
    private boolean validateParameters(String name, String regexId, String action) {
        Integer intRegexId = IntegerUtil.tryParse(regexId);
        if ((IntegerUtil.tryParse(name) == null || name.length() != Constants.NAME_LENGTH)
                && intRegexId == null) {
            logger.info(
                    String.format("INVALID_PARAMETERS, name =%s and regexId=%s do not have correct values",
                            name,
                            regexId));
            saveWSIRecord(  name,
                            intRegexId,
                            action,
                            getClientIPAddr(),
                            EResponseWS.INVALID_PARAMETERS);
            return true;
        }
        return false;
    }

    private boolean validateNameParam(String name, String regexId, String action) {
        Integer intRegexId = IntegerUtil.tryParse(regexId);
        if (IntegerUtil.tryParse(name) == null || name.length() != Constants.NAME_LENGTH) {
            logger.info(
                    String.format("INVALID_NAME, name =%s does not have correct value",
                            name,
                            regexId));
            saveWSIRecord(  name,
                            intRegexId,
                            action,
                            getClientIPAddr(),
                            EResponseWS.INVALID_NAME);
            return true;
        }
        return false;
    }
    
    private boolean validateRegexIdParam(String name, String regexId, String action) {
        Integer intRegexId = IntegerUtil.tryParse(regexId);
        if (intRegexId == null) {
            logger.info(
                    String.format("INVALID_REGEX_ID, regexId=%s does not have correct value",
                            name,
                            regexId));
            saveWSIRecord(  name,
                            intRegexId,
                            action,
                            getClientIPAddr(),
                            EResponseWS.INVALID_REGEX_ID);
            return true;
        }
        return false;
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
