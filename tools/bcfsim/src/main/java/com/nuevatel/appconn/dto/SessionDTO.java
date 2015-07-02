package com.nuevatel.appconn.dto;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.nuevatel.cf.appconn.Action;
import com.nuevatel.cf.appconn.Action.SESSION_ACTION;
import com.nuevatel.cf.appconn.Name;
import com.nuevatel.cf.appconn.SessionArg;
import com.nuevatel.common.util.ByteUtil;
import com.nuevatel.common.util.Parameters;
import com.nuevatel.common.util.StringUtils;
import com.nuevatel.common.util.encoder.Encoder;

/**
 * Session DTO model.
 */
public class SessionDTO {

    private String sessionId;

    private String name;

    private String toName;

    private String fromName;

    private String cellGlobalId;

    private String nodeId;

    private SessionServiceType serviceType = SessionServiceType.SPEECH;

    private RequestServiceType requestType = RequestServiceType.speechO;

    private String auxType;

    private boolean accepted = false;

    private boolean modify = false;

    private Byte nai = Name.NAI_NATIONAL;

    private Byte ton = Name.TON_NATIONAL;

    /**
     * Path for media action. It can produce at the begin and at the end of call.
     */
    private String mediaArg;

    /**
     * SessionDTO Constructor.
     *
     * @param sessionId Unique identifier.Cannot be null.
     * @param name Name of unit from which do the charge. Cannot be null.
     * @param toName Origin Unit Name for the call. Cannot be null.
     * @param fromName Destination Unit Name for the call. Cannot be null.
     * @param cellGlobalId Cell ID from which is originated the call. Cannot be null.
     * @param nodeId Node ID from which is originated the call. Cannot be null.
     * @param requestType Indicates if the session is incoming or outgoing. Cannot be null.
     * @param auxType String used to identify VOIP session on VONE. This parameter could be null.
     * @throws IllegalArgumentException If any of its parameter is null.
     */
    public SessionDTO(
            String sessionId,
            String name,
            String toName,
            String fromName,
            String cellGlobalId,
            String nodeId,
            RequestServiceType requestType,
            String auxType) {
        Parameters.checkBlankString(sessionId, "sessionId");
        Parameters.checkBlankString(name, "name");
        Parameters.checkBlankString(toName, "toName");
        Parameters.checkBlankString(fromName, "fromName");
        Parameters.checkNull(requestType, "requestType");

        this.sessionId = sessionId;
        this.name = name;
        this.toName = toName;
        this.fromName = fromName;
        this.cellGlobalId = cellGlobalId;
        this.nodeId = nodeId;
        this.requestType = requestType;
        this.auxType = auxType;
    }

    /**
     * Creates SessionDTO from <b>metadata</b>.
     * 
     * @param metadata Object metadata.
     * @param encodedNodeId Indicates if node id is encoded or not.
     * @throws IOException When fail to decode node id.
     */
    public SessionDTO(Map<String, String>metadata, boolean encodedNodeId) throws IOException {
        Parameters.checkNull(metadata, "metadata");

        sessionId = metadata.get(Metadata.sessionId.name());
        name = metadata.get(Metadata.name.name());
        toName = metadata.get(Metadata.toName.name());
        fromName = metadata.get(Metadata.fromName.name());
        cellGlobalId = metadata.get(Metadata.cellGlobalId.name());
        requestType = RequestServiceType.valueOf(metadata.get(Metadata.requestType.name()),
                RequestServiceType.speechO);
        Byte nai = ByteUtil.tryParse(metadata.get(Metadata.nai.name()));
        Byte ton = ByteUtil.tryParse(metadata.get(Metadata.ton.name()));
        // Default values for ton and nai -> National.
        this.nai = nai == null ? Name.NAI_NATIONAL : nai;
        this.ton = ton == null ? Name.TON_NATIONAL : ton;
        // Get aux type
        auxType = metadata.get(Metadata.auxType.name());
        mediaArg = metadata.get(Metadata.mediaArg.name());
        accepted = Boolean.parseBoolean(metadata.get(Metadata.accepted.name()));
        modify = Boolean.parseBoolean(metadata.get(Metadata.modify.name()));

        if (encodedNodeId) {
            nodeId = new String(Encoder.decodeBase64(metadata.get(Metadata.nodeId.name())));
        } else {
            nodeId = metadata.get(Metadata.nodeId.name());
        }
    }

    /**
     * SessionDTO Constructor. Based on previous sessionDTO object.
     * 
     * @param sessionToCreate Previous session DTO object. It cannot be null.
     * @param newSessionArg Session Args. response of AppConn session. Used to get some parameters
     * for new sessionDTO instance. This field could be null.
     * @param action Action response of session DTO.
     * @param mediaArg Define the media action. This field could be null.
     */
    public SessionDTO(SessionDTO sessionToCreate, SessionArg newSessionArg, Action action, String mediaArg) {
        this(
                sessionToCreate.getSessionId(), /*sessionId*/
                sessionToCreate.getName(), /*name*/
                newSessionArg == null? sessionToCreate.getToName() : newSessionArg.getToName().getName(), /*toName*/
                newSessionArg == null? sessionToCreate.getFromName() : newSessionArg.getFromName().getName(), /*fromName*/
                sessionToCreate.getCellGlobalId(), /*cellGlobalId*/
                sessionToCreate.getNodeId(), /*nodeId*/
                sessionToCreate.getRequestType(),/*requestType*/
                sessionToCreate.getAuxType() /*auxType*/);

        modify = SESSION_ACTION.MODIFY == action.getSessionAction();
        accepted = SESSION_ACTION.ACCEPT == action.getSessionAction() || modify;
        nai = sessionToCreate.getNai();
        ton = sessionToCreate.getTon();
        this.mediaArg = mediaArg;
    }

    public String getChargingVector() {
        return sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getName() {
        return name;
    }

    public String getToName() {
        return toName;
    }

    public String getFromName() {
        return fromName;
    }

    public String getCellGlobalId() {
        return cellGlobalId;
    }

    public SessionServiceType getServiceType() {
        return serviceType;
    }

    public String getNodeId() {
        return nodeId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    /**
     * @return the requestType
     */
    public RequestServiceType getRequestType() {
        return requestType;
    }

    /**
     * @return the nai
     */
    public Byte getNai() {
        return nai;
    }

    /**
     * @param nai the nai to set
     */
    public void setNai(Byte nai) {
        this.nai = nai;
    }

    /**
     * @return the ton
     */
    public Byte getTon() {
        return ton;
    }

    /**
     * @param ton the ton to set
     */
    public void setTon(Byte ton) {
        this.ton = ton;
    }

    /**
     * @return the auxType
     */
    public String getAuxType() {
        return auxType;
    }

    /**
     * @return the mediaArg
     */
    public String getMediaArg() {
        return mediaArg;
    }

    public boolean hasMediaUnit() {
        return !StringUtils.isBlank(mediaArg);
    }

    /**
     * @param mediaArg the mediaArg to set
     */
    public void setMediaArg(String mediaArg) {
        this.mediaArg = mediaArg;
    } 

    /**
     * @return the modify
     */
    public boolean isModify() {
        return modify;
    }

    /**
     * String representation for SessionDTO
     */
    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean encodeNodeId) {
        return String.format(
                "sessionId$%s;name$%s;fromName$%s;toName$%s;nai$%s;ton$%s;cellGlobalId$%s;nodeId$%s;"
                + "requestType$%s;auxType$%s;mediaArg$%s;accepted$%s;modify$%s",
                sessionId, name, fromName, toName, nai, ton, cellGlobalId,
                encodeNodeId ? Encoder.encodeBase64(nodeId.getBytes()) : nodeId, requestType, auxType,
                mediaArg, accepted, modify);
    }

    public String toMetadata() {
        return toString(true);
    }

    public static Map<String, String>parseMetadata(String rawMetadata) {
        if (StringUtils.isBlank(rawMetadata)) {
            return Collections.emptyMap();
        }

        String[] rows = rawMetadata.split(";");
        Map<String, String>metadata = new HashMap<String, String>();

        for (String rawRow : rows) {
            String[] row = rawRow.split("\\$");

            if (row.length < 2) {
                continue;
            }

            metadata.put(row[0].trim(), row[1].trim());
        }

        return metadata;
    }

    public enum Metadata {
        /**
         * Call id.
         */
        sessionId,
        name,
        fromName,
        toName,
        cellGlobalId,
        nodeId,
        requestType,
        nai,
        ton,
        auxType,
        mediaArg,
        accepted,
        modify,
        ;
    }
}
