package com.nuevatel.bcf;

import com.nuevatel.base.appconn.AppServer;
import com.nuevatel.base.appconn.TaskSet;
import com.nuevatel.base.wsi.EndpointSet;
import com.nuevatel.base.wsi.WSIPublisher;
import com.nuevatel.bcf.appconn.task.EventReportTask;
import com.nuevatel.bcf.appconn.task.GetMediaTask;
import com.nuevatel.bcf.appconn.task.NewSessionTask;
import com.nuevatel.bcf.appconn.task.TestSessionAsyncTask;
import com.nuevatel.bcf.appconn.task.WatchReportTask;
import com.nuevatel.bcf.core.PropName;
import com.nuevatel.bcf.core.dao.DatabaseHelper;
import com.nuevatel.bcf.service.AppServerFactory;
import com.nuevatel.bcf.service.LogRecorderServiceFactory;
import com.nuevatel.bcf.service.MediaServiceFactory;
import com.nuevatel.bcf.service.RegexCacheLoader;
import com.nuevatel.bcf.service.RegexServiceFactory;
import com.nuevatel.bcf.service.UnitCacheLoader;
import com.nuevatel.bcf.service.UnitServiceFactory;
import com.nuevatel.bcf.wsi.Unit;
import com.nuevatel.cf.appconn.CFMessage;
import com.nuevatel.common.ds.JDBCProperties;
import com.nuevatel.common.exception.InvalidPropertyValueException;
import com.nuevatel.common.util.IntegerUtil;
import com.nuevatel.common.util.LongUtil;
import com.nuevatel.common.util.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.ws.Endpoint;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Throws BCFService.<br/><br/>
 * <li/>Configure NameGetter
 * <li/>Configure and start DatasourceManager
 * <li/>Configure and start  RegexService
 * <li/>Configure and start UnitService
 * <li/>Initialize App conn server
 *
 * @author Ariel Salazar
 */
public class BCFProcessor implements Processor {

    private Logger logger = LogManager.getLogger(BCFProcessor.class);

    private ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    /**
     * Properties to start the app. It cannot be <b>null</b>.
     */
    private Properties prop;

    /**
     * Properties to build JDBC connection pool for <b>bcf</b> schema.
     */
    private JDBCProperties jdbcBcfProp;

    /**
     * Properties to build JDBC connection pool for <b>bcf_record</b> schema.
     */
    private JDBCProperties jdbcRecordProp;

    private AppServer appServer = null;

    private int bcfId;

    private AppServerFactory appServerFactory = new AppServerFactory();

    private UnitServiceFactory unitServiceFactory = null;

    private LogRecorderServiceFactory logRecorderServiceFactory = null;

    private RegexServiceFactory regexServiceFactory = null;

    private MediaServiceFactory mediaServiceFactory = null;

    /**
     * The wsiPublisher.
     */
    private WSIPublisher wsiPublisher = null;

    /**
     * Initialize processor, it cannot start the services, to start the service execute <b>execute</b> method.
     *
     * @param prop It cannot be <b>null</b>.
     */
    public BCFProcessor(Properties prop, JDBCProperties jdbcBcfProp, JDBCProperties jdbcRecordProp) {
        Parameters.checkNull(prop, "prop");

        Integer bcfId = IntegerUtil.tryParse(prop.getProperty(PropName.id.property()));
        if (bcfId == null) {
            throw new IllegalArgumentException("Missing bcfId.");
        } else {
            this.bcfId = bcfId.intValue();
        }

        this.prop = prop;
        this.jdbcBcfProp = jdbcBcfProp;
        this.jdbcRecordProp = jdbcRecordProp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        service.execute(() -> {
            try {
                start();
            } catch (Throwable ex) {
                logger.fatal("AppServer cannot been initialized.", ex);
            }
        });
    }

    private void start() throws Exception {
        // Configure Name Unit Getter
        configureNameGetter(prop);
        // Configure data source manager
        configureDatasourceManager();
        logger.info("Datasource Manager was started...");
        // Configure Regex service
        configureRegexService(prop);
        logger.info("UnitService was started...");
        // Configure Unit service
        configureUnitService(prop);
        logger.info("RegexService was started...");
        // Initialize appconn taskset
        TaskSet taskSet = getTaskSet();
        // Initialize log recorder
        configureLogRecorderService();
        logger.info("LogRecorderService was started...");
        configureMediaService();
        logger.info("MediaService was started...");
        // Initialize AppConnServer
        appServerFactory.start(bcfId, taskSet, prop);
        logger.info("AppConnServer was started...");
        // Initialize web service interface
        configureWSI();
        logger.info("WSI was started...");
    }

    private TaskSet getTaskSet() {
        TaskSet taskSet = new TaskSet();
        taskSet.add(CFMessage.NEW_SESSION_CALL, new NewSessionTask());
        taskSet.add(CFMessage.EVENT_REPORT_CALL, new EventReportTask());
        taskSet.add(CFMessage.WATCH_REPORT_CALL, new WatchReportTask());
        taskSet.add(CFMessage.TEST_SESSION_ASYNC_RET, new TestSessionAsyncTask());
        taskSet.add(CFMessage.GET_MEDIA_CALL, new GetMediaTask());
        return taskSet;
    }

    @Override
    public void shutdown(int ts) {
        try {
            service.shutdown();
            service.awaitTermination(ts, TimeUnit.SECONDS);
            logger.info("Shutdown BCF App Standalone service...");
            mediaServiceFactory.shutdown();
            logger.info("Shutdown media service...");
            // Stop appconn server
            appServer.interrupt();
            logger.info("Shutdown Appconn Server service...");
            // Stop Web Service Interface
            wsiPublisher.interrupt();
            logger.info("Shutdown Web Service Interface...");
            // Stop jdbc connections
            DatabaseHelper.shutdownConnPool();
            logger.info("Shutdown Database connection pool...");
        } catch (InterruptedException ex) {
            logger.warn("Failed to shutdown process.", ex);
        }
    }

    private void configureNameGetter(Properties prop) throws ClassNotFoundException,
                                                             InstantiationException,
                                                             IllegalAccessException {
        String strNameGetter =  prop.getProperty(PropName.getter.property(), "com.nuevatel.bcf.NameGetter10");
        Class<NameGetter>clazzNameGetter = (Class<NameGetter>) Class.forName(strNameGetter);
        NameGetterProvider.set(clazzNameGetter);
    }

    private void configureDatasourceManager() throws InvalidPropertyValueException, SQLException, ClassNotFoundException {
        DatabaseHelper.configure(jdbcBcfProp, jdbcRecordProp);
    }

    private void configureRegexService(Properties prop) {
        int cLevel = IntegerUtil.tryParse(prop.getProperty(PropName.cache_service_concurrencyLevel.property(), "4"));
        long expireAfterWrite = LongUtil.tryParse(prop.getProperty(PropName.cache_service_expireAfterWrite.property(), "30000"));
        long expireAfterAccess = LongUtil.tryParse(prop.getProperty(PropName.cache_service_expireAfterAccess.property(), "600000"));
        regexServiceFactory = new RegexServiceFactory(null);
        regexServiceFactory.setExpireAfterWrite(expireAfterWrite)
                .setExpireAfterAccess(expireAfterAccess)
                .setConcurrencyLevel(cLevel)
                .setCacheLoader(new RegexCacheLoader());
        // No destination cache, only for configure purpose.
        regexServiceFactory.getCache();
    }

    private void configureUnitService(Properties prop) {
        int cLevel = IntegerUtil.tryParse(prop.getProperty(PropName.cache_service_concurrencyLevel.property(), "4"));
        long expireAfterWrite = LongUtil.tryParse(prop.getProperty(PropName.cache_service_expireAfterWrite.property(), "30000"));
        long expireAfterAccess = LongUtil.tryParse(prop.getProperty(PropName.cache_service_expireAfterAccess.property(), "600000"));
        unitServiceFactory = new UnitServiceFactory(null);
        unitServiceFactory.setExpireAfterWrite(expireAfterWrite)
                .setExpireAfterAccess(expireAfterAccess)
                .setConcurrencyLevel(cLevel)
                .setCacheLoader(new UnitCacheLoader());
        // No destination cache, only for configure purpose.
        unitServiceFactory.getCache();
    }

    private void configureLogRecorderService() {
        int threadPoolSize = IntegerUtil.tryParse(prop.getProperty(PropName.bcf_logrecorder_size.property(), "4"));
        logRecorderServiceFactory = new LogRecorderServiceFactory();
        logRecorderServiceFactory.start(threadPoolSize);
    }

    private void configureMediaService() {
        int threadPoolSize = IntegerUtil.tryParse(prop.getProperty(PropName.bcf_service_media_size.property(), "4"));
        mediaServiceFactory = new MediaServiceFactory();
        mediaServiceFactory.start(threadPoolSize);
    }

    private void configureWSI() throws Exception {
        // wsiPublisherProperties
//        wsiPublisherProperties.put(WSIPublisher.BIND_ADDRESS, "10.47.17.225");
//        wsiPublisherProperties.put(WSIPublisher.BIND_ADDRESS, "10.40.20.148");
        //wsiPublisherProperties.put(WSIPublisher.PORT, 8080);
        // http://10.47.17.229:8080/wsi/unit?wsdl
        prop.put(WSIPublisher.PORT, 8080);
        prop.put(WSIPublisher.BACKLOG, 32);
        // endpointSet
        EndpointSet endpointSet = new EndpointSet();
        endpointSet.add("unit", Endpoint.create(new Unit()), null);
        // wsiPublisher
        wsiPublisher = new WSIPublisher(endpointSet, prop);
        wsiPublisher.start();
    }
}
