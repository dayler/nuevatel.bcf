package com.nuevatel.bcf.service;

import com.nuevatel.bcf.core.dao.ConfigDao;
import com.nuevatel.bcf.core.domain.Config;
import com.nuevatel.common.util.StringUtils;
import jaxws.client.mailmiddleware.MailWS;
import jaxws.client.mailmiddleware.MailWSService;
import jaxws.client.smsdispatcher.VASWS;
import jaxws.client.smsdispatcher.VASWSService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.ws.BindingProvider;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by asalazar on 6/25/15.
 */
public class AlertServiceImpl implements AlertService {

    private static Logger logger = LogManager.getLogger(AlertService.class);

    private final static int PERIOD = 5;

    private final static String ENDL = StringUtils.END_LINE;

    private int appId;

    private Config config;

    private ConfigDao configDao = new ConfigDao();

    private Queue<String>messages = new ConcurrentLinkedQueue<>();

    private MailWS mailWSPort = null;

    private VASWS vasws = null;

    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void start(int appId) throws SQLException {
        this.appId = appId;
        config = configDao.getConfig();
        initializeMailWSPort();
        initializeDispatcher();
        // Schedule service
        long minutes = Calendar.getInstance().get(Calendar.MINUTE) % 10;
        service.scheduleAtFixedRate(()->dispatch(),
                                    Math.abs(PERIOD - minutes),
                                    PERIOD,
                                    TimeUnit.MINUTES);
    }

    @Override
    public void shutdown() {
        try {
            if (service == null) {
                return;
            }
            service.shutdown();
            service.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            logger.warn("Failed to shutdown AlertService...");
        }
    }

    @Override
    public void appendAlert(String msg) {
        messages.offer(msg);
    }

    private Config getConfig() {
        try {
            return configDao.getConfig();
        } catch (SQLException ex) {
            logger.error("Failed to load initial configuration.", ex);
            return null;
        }
    }

    private void dispatch() {
        logger.debug("Executing dispatch for {} registers...", messages.size());
        if (messages.isEmpty()) {
            return;
        }
        config = getConfig();
        String header = String.format(config.getAlertHeader(), messages.size());
        StringBuffer emailBody = new StringBuffer();
        String smsBody =  messages.peek();
        while (!messages.isEmpty()) {
            emailBody.append(messages.poll()).append(ENDL);
        }
        dispatchEmail(header, emailBody.toString());
        dispatchSMS(header, smsBody);
    }

    private void dispatchEmail(String header, String body) {
        String fullMsg = header + ENDL + ENDL + body;
        mailWSPort.sendMail(appId,
                config.getFromEmail(),
                config.getToEmailList(),
                config.getEmailSubject(),
                fullMsg, null, null, false);
    }

    private void dispatchSMS(String header, String body) {
        String fullMsg = header + ENDL + body;
        config.getToSmsList().forEach((to) -> {
            List<String> arg2 = new ArrayList<>();
            arg2.add(config.getFromSms());
            arg2.add(to);
            arg2.add(fullMsg);
            vasws.execute(appId, arg2);
        });
    }

    private void initializeMailWSPort() {
        MailWSService mailWSService = new MailWSService();
        mailWSPort = mailWSService.getMailWSPort();
        ((BindingProvider)mailWSPort).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                config.getEndpointEmailMiddleware());
    }

    private void initializeDispatcher() {
        VASWSService vaswsService = new VASWSService();
        vasws = vaswsService.getVASWSPort();
        ((BindingProvider)vasws).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                                                         config.getEndpointDispatcher());
    }
}

