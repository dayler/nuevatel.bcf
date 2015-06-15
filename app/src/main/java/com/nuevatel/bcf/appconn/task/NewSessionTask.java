package com.nuevatel.bcf.appconn.task;

import com.nuevatel.base.appconn.Conn;
import com.nuevatel.base.appconn.Message;
import com.nuevatel.base.appconn.Task;
import com.nuevatel.bcf.NameGetterProvider;
import com.nuevatel.bcf.domain.Media;
import com.nuevatel.bcf.domain.Regex;
import com.nuevatel.bcf.domain.Swap;
import com.nuevatel.bcf.domain.Unit;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Message execute(Conn conn, Message message) throws Exception {
        NewSessionCall newSessionCall = new NewSessionCall(message);
        try {
            // NewSessionCall
            if (checkNewSessionCall(newSessionCall)) {
                // Get type
                Type tmpType = newSessionCall.getType();
                Name tmpName =  newSessionCall.getName();
                // Name
                Name name = NameGetterProvider.get().getSessionName(tmpType, tmpName);
                SessionArg sessionArgs = newSessionCall.getSessionArg();
                // from session name
                Name fromName = sessionArgs.getFromName();
                // to session name
                Name toName = sessionArgs.getToName();
                // get session args
                SessionArg tmpSessionArgs = null;
                // TODO Check if is needed get new instance
//                SessionArg sessionArgs = new SessionArg(fromName,
//                                                       toName,
//                                                       null,
//                                                       null,
//                                                       null,
//                                                       tmpSessionArg.getReference());
                if (logger.isDebugEnabled() || logger.isTraceEnabled()) {
                    logger.debug("Name:%s fromName:%s toName:%s", name, fromName, toName);
                }

                Unit unit = unitServiceFactory.getCache().getUnit(name.getName());
                if (unit != null && (Type.REQUEST_TYPE.O.compareTo(tmpType.getRequestType()) == 0)) {
                    logger.debug("Unit:%s is in the black list.", name.getName());
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
                            logger.debug("Execute Id:%s pattern:%s", id.getId0(), regex.getPattern());
                            // Matcher
                            Matcher matcher = regex.getPattern().matcher(toSessionName.getName());
                            if (matcher.lookingAt() && matcher.start() == 0) {
                                // Prepare and dispatch NewSessionRet
                                Media newMedia = regex.getNewMedia();
                                Media endMedia = regex.getEndMedia();
                                Swap swap = regex.getSwap();
                                SessionArg tmpSwapSessionArg = null;

                                if (swap != null) {
                                    logger.info("Execute Id:%s swap:%s", id.getId0(), swap.getName());
                                    tmpSwapSessionArg = new SessionArg(null, swap.getName(), null, null, null, null);
                                }

                                MediaArg mediaArgs = null;
                                Action mediaAction = null;

                                // End session in any case.
                                // if new media
                                if (newMedia != null) {
                                    logger.info("Execute Id:%s newMedia:%s", id.getId0(), newMedia.getName());
                                    mediaAction = new Action(Action.MEDIA_ACTION.NEW_MEDIA, Action.SESSION_ACTION.END);
                                    mediaArgs = new MediaArg(newMedia.getMediaName(),
                                                                      newMedia.getName().getType(),
                                                                      newMedia.getValue());
                                }

                                // if end media
                                if (endMedia != null) {
                                    logger.info("Execute Id:%s endMedia:%s", id.getId0(), endMedia.getName());
                                    mediaAction = new Action(Action.MEDIA_ACTION.END_MEDIA, Action.SESSION_ACTION.END);
                                    mediaArgs = new MediaArg(endMedia.getMediaName(),
                                                             endMedia.getName().getType(),
                                                             endMedia.getValue());
                                }

                                // dispatch message
                                NewSessionRet newSessionRet = new NewSessionRet(mediaAction, // Action (Session|Media)
                                                                                tmpSwapSessionArg, // Session args
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
                logger.debug("Unit:%s is not in the black list.", name.getName());
                return newSessionRet.toMessage();
            }
        } catch (Throwable ex) {
            logger.error("Failed on NewSessionTask.", ex);
            logger.error("Message: %s", message.toXML());
        }

        logger.warn("No action can be determined for Unit%s. The session will stop.", newSessionCall.getName().getName());
        logger.warn("Message: %s", message.toXML());
        return new NewSessionRet(new Action(null, Action.SESSION_ACTION.END), null, null, null).toMessage();
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
