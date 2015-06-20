/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nuevatel.bcf;

import com.nuevatel.base.wsi.EndpointSet;
import com.nuevatel.base.wsi.WSIPublisher;
import java.util.Properties;
import javax.xml.ws.Endpoint;

/**
 *
 * @author clvelarde
 */
public class WSIConnection {

     /** The wsiPublisher. */
    private final WSIPublisher wsiPublisher;
    /** The wsiPublisherProperties. */
    private final Properties wsiPublisherProperties;

    public WSIConnection() throws Exception {
        // wsiPublisherProperties
        wsiPublisherProperties=new Properties();
        wsiPublisherProperties.put(WSIPublisher.BIND_ADDRESS, "10.47.17.225");
        wsiPublisherProperties.put(WSIPublisher.PORT, 8080);
        wsiPublisherProperties.put(WSIPublisher.BACKLOG, 4);
        // endpointSet
        EndpointSet endpointSet=new EndpointSet();
        endpointSet.add("unit", Endpoint.create(new Unit()), null);
        // wsiPublisher
        wsiPublisher=new WSIPublisher(endpointSet, wsiPublisherProperties);
    }

    public static void main(String args[]) throws Exception {
        WSIConnection wsiConn=new WSIConnection();
        wsiConn.getWSIPublisher().start();
    }

    public WSIPublisher getWSIPublisher() {
        return wsiPublisher;
    }
}
