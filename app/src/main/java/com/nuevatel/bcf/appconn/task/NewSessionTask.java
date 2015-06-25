package com.nuevatel.bcf.appconn.task;

import com.nuevatel.base.appconn.Conn;
import com.nuevatel.base.appconn.Message;
import com.nuevatel.base.appconn.Task;
import com.nuevatel.bcf.NameGetterProvider;
import com.nuevatel.bcf.core.domain.Media;
import com.nuevatel.bcf.core.domain.Regex;
import com.nuevatel.bcf.core.domain.Swap;
import com.nuevatel.bcf.core.domain.Unit;
import com.nuevatel.bcf.service.MediaServiceFactory;
import com.nuevatel.bcf.service.RegexServiceFactory;
import com.nuevatel.bcf.service.UnitServiceFactory;
import com.nuevatel.cf.appconn.Action;
import com.nuevatel.cf.appconn.Id;
import com.nuevatel.cf.appconn.MediaArg;
import com.nuevatel.cf.appconn.Name;
import com.nuevatel.cf.appconn.NewSessionCall;
import com.nuevatel.cf.appconn.NewSessionRet;
import com.nuevatel.cf.appconn.SessionArg;
import com.nuevatel.cf.appconn.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.nuevatel.common.util.Util.*;

/**
 * Handle <b>NewSessionCall</b> message. If the unit to request session in on blacklist, it will send a media
 * (new or end media) and finish it.
 *
 * @author Ariel Salazar
 */
public class NewSessionTask implements Task {

    private static Logger logger = LogManager.getLogger(NewSessionTask.class);

    private UnitServiceFactory unitServiceFactory = new UnitServiceFactory();

    private RegexServiceFactory regexServiceFactory = new RegexServiceFactory();

    private MediaServiceFactory mediaServiceFactory = new MediaServiceFactory();

    /**
     * {@inheritDoc}
     */
    @Override
    public Message execute(Conn conn, Message message) throws Exception {
        NewSessionCall newSessionCall = new NewSessionCall(message);
        try {
            if (logger.isDebugEnabled() || logger.isTraceEnabled()) {
                logger.debug("NewSessionTask message:{}", message.toXML());
            }
            // NewSessionCall
            if (checkNewSessionCall(newSessionCall)) {
                // Get type
                Type tmpType = newSessionCall.getType();
                Name tmpName = newSessionCall.getName();

                SessionArg tmpSessionArgs = newSessionCall.getSessionArg();
                // from session name
                Name fromName = tmpSessionArgs.getFromName();
                // to session name
                Name toName = tmpSessionArgs.getToName();
                // get session args
                SessionArg sessionArgs = new SessionArg(fromName,
                                                        toName,
                                                        null,
                                                        null,
                                                        null,
                                                        tmpSessionArgs.getReference());
                // Name
                Name name = NameGetterProvider.get().getSessionName(tmpType, tmpName);
                if (logger.isDebugEnabled() || logger.isTraceEnabled()) {
                    logger.debug("Name:{} fromName:{} toName:{}", name, fromName, toName);
                }

                // Get unit
                Unit unit = unitServiceFactory.getCache().getUnit(fixUnitName(name));
                logger.debug("##### unit is not null:{} ReqType:{}", unit != null, tmpType.getRequestType());
                // TODO
                if (unit != null && (Type.REQUEST_TYPE.O.compareTo(tmpType.getRequestType()) == 0)) {
                    logger.debug("Unit:{} is in the black list.", name.getName());
                    // time now
                    Date now = new Date();
                    // Get toSessionName
                    Name toSessionName = NameGetterProvider.get().getToSessionName(tmpType, name, sessionArgs); // Get from sessionArgs
                    for (Integer regexId : unit.getRegexIds()) {
                        Date startTimestamp = unit.getStartTimestamp(regexId);
                        Date endTimestamp = unit.getEndTimestamp(regexId);
                        if (startTimestamp.before(now) && (endTimestamp == null || endTimestamp.after(now))) {
                            // Regex should never be null. On the table 'unit' primary keys are 'name' and 'regexId'.
                            Regex regex = regexServiceFactory.getCache().getRegex(regexId);
                            // TODO regex == null ??
                            Id id = newSessionCall.getId();
                            logger.debug("Execute Id:{} pattern:{}", id.getId0(), regex.getPattern());
                            // Matcher
                            Matcher matcher = regex.getPattern().matcher(toSessionName.getName());
                            if (matcher.lookingAt() && matcher.start() == 0) {
                                // Prepare and dispatch NewSessionRet
                                Media newMedia = regex.getNewMedia();
                                Swap swap = regex.getSwap();
                                Action.MEDIA_ACTION mediaAction = null;
                                Action.SESSION_ACTION sessionAction = null;

                                // For swap operation.
                                if (swap != null) {
                                    logger.info("Execute Id:{} swap:{}", id.getId0(), swap.getName());
                                    sessionArgs = new SessionArg(fromName,
                                                                 swap.getName(),
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 tmpSessionArgs.getReference());
                                    sessionAction = Action.SESSION_ACTION.MODIFY;
                                }

                                MediaArg mediaArgs = null;

                                // end media
                                if (newMedia != null) {
                                    logger.info("Execute Id:{} newMedia:{}", id.getId0(), newMedia.getName());
                                    sessionAction = ifNull(sessionAction, Action.SESSION_ACTION.END);
                                    mediaAction = Action.MEDIA_ACTION.NEW_MEDIA; // to initialize media op
                                    mediaArgs = new MediaArg(newMedia.getName().getName(),
                                            newMedia.getName().getType(),
                                            newMedia.getValue());
                                }

                                Action tmpAction = new Action(mediaAction, sessionAction);
                                // Media service
                                if (mediaArgs != null && mediaArgs.getMediaArg2() != null) {
                                    mediaServiceFactory.get().schedule(conn.getRemoteId(),
                                                                       id,
                                                                       tmpType,
                                                                       tmpAction,
                                                                       sessionArgs,
                                                                       mediaArgs.getMediaArg2()
                                                                       );
                                }

                                // dispatch message
                                NewSessionRet newSessionRet = new NewSessionRet(tmpAction, // Action (Session|Media)
                                        sessionArgs, // Session args
                                        null, // Watch args
                                        mediaArgs // null media args
                                );
                                return newSessionRet.toMessage();
                            } else {
                                // No op
                            }
                        }
                    }
                }
                // The unit is not in the black list
                Action tmpAction = new Action(null, Action.SESSION_ACTION.ACCEPT);
                NewSessionRet newSessionRet = new NewSessionRet(tmpAction, // Action (Session|Media)
                        sessionArgs, // Session args
                        null, // null watch args
                        null); // null media args
                logger.debug("Unit:{} is not in the black list.", name.getName());
                return newSessionRet.toMessage();
            }
        } catch (Throwable ex) {
            logger.error("Failed on NewSessionTask.", ex);
            logger.error("Message: {}", message.toXML());
        }

        logger.warn("No action can be determined for Unit:{}. The session will stop.", newSessionCall.getName().getName());
        logger.warn("Message:{}", message.toXML());
        return new NewSessionRet(new Action(null, Action.SESSION_ACTION.END), null, null, null).toMessage();
    }

    /**
     *
     * @param name Unit name to find.
     * @return Fixed unit name. Remove 591 of the front.
     */
    private String fixUnitName(Name name) {
        String tmp = name.getName();
        if (Name.NAI_INTERNATIONAL == name.getType()) {
            tmp = tmp.substring("591".length());
        }
        return tmp;
    }

    /**
     * Verify NewSessionCall structure.
     *
     * @param newSessionCall NewSessionCall
     * @return boolean <b>true</b> If NewSessionCall is valid. <b>false</b> if NewSessionCall is not well formed.
     */
    boolean checkNewSessionCall(NewSessionCall newSessionCall) {
        // id
        if(newSessionCall.getId() == null || newSessionCall.getId().getId0() == null) {
            logger.warn("NULL id");
            return false;
        }
        // type
        if(newSessionCall.getType() == null) {
            logger.warn("NULL type");
            return false;
        }

        // pattern
        Pattern pattern = Pattern.compile("['\"\\\\]");

        // name
        if(newSessionCall.getName() != null) {
            String name = newSessionCall.getName().getName();
            if(name == null || pattern.matcher(name).find()) {
                logger.warn("Invalid name " + name + " id " + newSessionCall.getId().getId0());
                return false;
            }
        } else {
            logger.warn("NULL name");
            return false;
        }

        // location
        if(newSessionCall.getLocation() != null) {
            // gLocation
            String gLocation = newSessionCall.getLocation().getGLocation();
            if(gLocation!=null && pattern.matcher(gLocation).find()) {
                logger.warn("Invalid gLocation " + gLocation + " id " + newSessionCall.getId().getId0() + " name " + newSessionCall.getName());
                return false;
            }
            // nodeId
            String nodeId = newSessionCall.getLocation().getNodeId();
            if(nodeId != null && pattern.matcher(nodeId).find()) {
                logger.warn("Invalid nodeId " + nodeId + " id " + newSessionCall.getId().getId0() + " name " + newSessionCall.getName());
                return false;
            }
        }
        // sessionArg
        if(newSessionCall.getSessionArg() != null) {
            // fromName
            if(newSessionCall.getSessionArg().getFromName() != null) {
                String name = newSessionCall.getSessionArg().getFromName().getName();
                if(name != null && pattern.matcher(name).find()) {
                    logger.warn("Invalid fromName " + name + " id " + newSessionCall.getId().getId0() + " name " + newSessionCall.getName());
                    return false;
                }
            }
            // toName
            if(newSessionCall.getSessionArg().getToName() != null) {
                String name = newSessionCall.getSessionArg().getToName().getName();
                if(name != null && pattern.matcher(name).find()) {
                    logger.warn("Invalid toName " + name + " id " + newSessionCall.getId().getId0() + " name " + newSessionCall.getName());
                    return false;
                }
            }
            // apn
            String apn = newSessionCall.getSessionArg().getAPN();
            if(apn != null && pattern.matcher(apn).find()) {
                logger.warn("Invalid apn " + apn + " id " + newSessionCall.getId().getId0() + " name " + newSessionCall.getName());
                return false;
            }
            // qos
            String qos = newSessionCall.getSessionArg().getQOS();
            if(qos != null && pattern.matcher(qos).find()) {
                logger.warn("Invalid qos " + qos + " id " + newSessionCall.getId().getId0() + " name " + newSessionCall.getName());
                return false;
            }
            // uei
            String uei = newSessionCall.getSessionArg().getUEI();
            if(uei != null && pattern.matcher(uei).find()) {
                logger.warn("Invalid uei " + uei + " id " + newSessionCall.getId().getId0() + " name " + newSessionCall.getName());
                return false;
            }
            // reference
            String reference = newSessionCall.getSessionArg().getReference();
            if(reference != null && pattern.matcher(reference).find()) {
                logger.warn("Invalid reference " + reference + " id " + newSessionCall.getId().getId0() + " name " + newSessionCall.getName());
                return false;
            }
        }

        return true;
    }
}
