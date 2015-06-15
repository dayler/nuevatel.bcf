package com.nuevatel.bcf.appconn.task;

import com.nuevatel.base.appconn.Conn;
import com.nuevatel.base.appconn.Message;
import com.nuevatel.base.appconn.Task;

/**
 * Created by asalazar on 6/11/15.
 */
public class EventReportTask implements Task {

    @Override
    public Message execute(Conn conn, Message message) throws Exception {
        /**
         * Id id=null;
         CompositeIE ieId=(CompositeIE)message.getIE(BCFTypeCollection.ID);
         if(ieId!=null) id=new Id(ieId.getString(), ieId.getIEInteger());
         */
        // Id

        // Type
        // From name
        // To name
        // Reference

        return null;
    }
}
