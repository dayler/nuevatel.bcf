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
 * Created by asalazar on 6/11/15.
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
        try {
            // NewSessionCall
            NewSessionCall newSessionCall = new NewSessionCall(message);
            if (checkNewSessionCall(newSessionCall)) {
                // Get type
                Type tmpType = newSessionCall.getType();
                Name tmpName =  newSessionCall.getName();
                // Name
                Name name = NameGetterProvider.get().getSessionName(tmpType, tmpName);
                SessionArg sessionArg = newSessionCall.getSessionArg();
                // from session name
                Name fromName = sessionArg.getFromName();
                // to session name
                Name toName = sessionArg.getToName();
                // get session args
                SessionArg tmpSessionArg = null;
                // TODO Check if is needed get new instance
//                SessionArg sessionArg = new SessionArg(fromName,
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
                    // time now
                    Date now = new Date();
                    // Get toSessionName
                    Name toSessionName = NameGetterProvider.get().getToSessionName(tmpType, name, sessionArg); // Get from sessionArgs
                    for (Integer regexId : unit.getRegexIds()) {
                        Date startTimestamp = unit.getStartTimestamp(regexId);
                        Date endTimestamp = unit.getEndTimestamp(regexId);
                        if (startTimestamp.before(now) && (endTimestamp == null || endTimestamp.after(now))) {
                            // Get Regex
                            Regex regex = regexServiceFactory.getCache().getRegex(regexId);
                            // TODO regex == null ??
                            logger.debug("Execute Id:%s pattern:%s", newSessionCall.getId().getId0(), regex.getPattern());
                            // Matcher
                            Matcher matcher = regex.getPattern().matcher(toSessionName.getName());
                            if (matcher.lookingAt() && matcher.start() == 0) {
                                Media newMedia = regex.getNewMedia();
                                Media endMedia = regex.getEndMedia();
                                Swap swap = regex.getSwap();

                                if (swap != null) {
                                    logger.info("Execute Id:%s swap:%s", newSessionCall.getId().getId0(), swap.getName());
                                    SessionArg tmpSwapSessionArg = new SessionArg(null, swap.getName(), null, null, null, null);
                                }

                                if (newMedia != null) {
                                    logger.info("Execute Id:%s newMedia:%s", newSessionCall.getId().getId0(), newMedia.getName());
                                    MediaArg tmpMediaArg = new MediaArg(newMedia.getName().getName(),
                                                                        newMedia.getName().getType(),
                                                                        newMedia.getValue());

                                    // Media service
                                    Action.MEDIA_ACTION mediaAction = Action.MEDIA_ACTION.NEW_MEDIA;
                                    MediaArg mArg = new MediaArg()
//                                    tmpAction=(byte)(tmpAction | BCFTypeCollection.NEW_MEDIA);
//                                    tmpMediaArg=new MediaArg(newMedia.getName().getName(), newMedia.getName().getType(), newMedia.getValue());
//                                    // mediaService
//                                    MediaService.getMediaService().addEntry(id, type, tmpAction, tmpSessionArg, tmpMediaArg.getMediaArg2(), baseConn.getConnId());
                                }

                            } else {
                                //
                            }
                        }
                    }
                }
            }
        } catch (Throwable ex) {
            logger.error("Failed on NewSessionTask", ex);
        }

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
