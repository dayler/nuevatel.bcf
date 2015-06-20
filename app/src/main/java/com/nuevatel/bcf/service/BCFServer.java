package com.nuevatel.bcf.service;

import com.nuevatel.base.appconn.AppServer;
import com.nuevatel.base.appconn.TaskSet;
import com.nuevatel.common.util.Parameters;

import java.util.Properties;

/**
 * Created by asalazar on 6/14/15.
 */
public class BCFServer extends AppServer {

    // appServer = new AppServer(bcfId, taskSet, prop);
    BCFServer(Integer id, TaskSet taskSet, Properties prop) {
        super(id, taskSet, prop);
    }

    public void start() {
        start();
    }

    /**
     * TODO
     *
     * Dispatches a bcfRequest.
     * @param clientId int
     * @param bcfRequest BCFRequest
     * @return Message
     */
//    public Message dispatchBCFRequest(int clientId, BCFRequest bcfRequest) {
//        BaseConn baseConn=nextOnline(clientId);
//        if(baseConn!=null) {
//            Future<Message> futureResponse=baseConn.dispatch(bcfRequest.getMessage());
//            try {
//                Message response=futureResponse.get();
//                if(response!=null && response.getType()==bcfRequest.getLinkedType())
//                    return response;
//            }
//            catch(Exception e) {}
//        }
//        return null;
//    }
}
