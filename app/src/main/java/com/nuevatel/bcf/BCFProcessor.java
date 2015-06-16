package com.nuevatel.bcf;

import com.nuevatel.base.appconn.AppServer;
import com.nuevatel.base.appconn.TaskSet;
import com.nuevatel.bcf.service.BCFServerFactory;
import com.nuevatel.bcf.service.MediaServiceFactory;
import com.nuevatel.bcf.service.RegexCacheLoader;
import com.nuevatel.bcf.service.RegexServiceFactory;
import com.nuevatel.bcf.service.UnitCacheLoader;
import com.nuevatel.bcf.service.UnitServiceFactory;
import com.nuevatel.common.ds.DataSourceManagerConfigurator;
import com.nuevatel.common.ds.JDBCProperties;
import com.nuevatel.common.exception.InvalidPropertyValueException;
import com.nuevatel.common.util.IntegerUtil;
import com.nuevatel.common.util.LongUtil;
import com.nuevatel.common.util.Parameters;
import com.nuevatel.bcf.core.PropName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private AppServer appServer = null;

    private int bcfId;

    private BCFServerFactory bcfServerFactory = new BCFServerFactory();

    private MediaServiceFactory mediaServiceFactory = new MediaServiceFactory();

    /**
     * Initialize processor, it cannot start the services, to start the service execute <b>execute</b> method.
     *
     * @param prop It cannot be <b>null</b>.
     */
    public BCFProcessor(Properties prop) {
        Parameters.checkNull(prop, "prop");

        Integer bcfId = IntegerUtil.tryParse(prop.getProperty(PropName.id.property()));
        if (bcfId == null) {
            throw new IllegalArgumentException("Missing bcfId.");
        } else {
            this.bcfId = bcfId.intValue();
        }

        this.prop = prop;
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
        configureDatasourceManager(prop);
        logger.info("Datasource Manager was started...");
        // Start media service
        configureMediaService(prop);
        logger.info("Media Service was started...");
        // Configure Regex service
        configureRegexService(prop);
        logger.info("UnitService was started...");
        // Configure Unit service
        configureUnitService(prop);
        logger.info("RegexService was started...");
        // Initialize appconn taskset
        TaskSet taskSet = getTaskSet();
        // Initialize AppConnServer
        bcfServerFactory.start(bcfId, taskSet, prop);
    }

    private void configureMediaService(Properties prop) {
        Integer size = IntegerUtil.tryParse(prop.getProperty(PropName.bcf_service_media_size.property()));
        mediaServiceFactory.start(size);
    }

    private TaskSet getTaskSet() {
        return new TaskSet();
    }

    @Override
    public void shutdown(int ts) {
        try {
            service.shutdown();
            service.awaitTermination(ts, TimeUnit.SECONDS);
            // Stop appconn server
            appServer.interrupt();
        } catch (InterruptedException ex) {
            logger.warn("Failed to shutdown process.");
        }
    }

    private void configureNameGetter(Properties prop) throws ClassNotFoundException,
                                                             InstantiationException,
                                                             IllegalAccessException {
        String strNameGetter =  prop.getProperty(PropName.getter.property(), "com.nuevatel.bcf.NameGetter10");
        Class<NameGetter>clazzNameGetter = (Class<NameGetter>) Class.forName(strNameGetter);
        NameGetterProvider.set(clazzNameGetter);
    }

    private void configureDatasourceManager(Properties prop) throws InvalidPropertyValueException,
            SQLException,
            ClassNotFoundException {
        JDBCProperties jdbcProps = new JDBCProperties(prop);
        DataSourceManagerConfigurator configurator = new DataSourceManagerConfigurator();
        configurator.configure().setJdbcDriver(jdbcProps.getDriver())
                .setJdbcUrl(jdbcProps.getUrl())
                .setJdbcUser(jdbcProps.getUser())
                .setJdbcPassword(jdbcProps.getPassword())
                .setMinConnPerPatition(jdbcProps.getMinConnPerPartition())
                .setMaxConnPerPatition(jdbcProps.getMaxConnPerPartition())
                .setPartitionCount(jdbcProps.getPartitionCount())
                .build();
    }

    private void configureRegexService(Properties prop) {
        int cLevel = IntegerUtil.tryParse(prop.getProperty(PropName.cache_service_concurrencyLevel.property(), "4"));
        long expireAfterWrite = LongUtil.tryParse(prop.getProperty(PropName.cache_service_expireAfterWrite.property(), "30000"));
        long expireAfterAccess = LongUtil.tryParse(prop.getProperty(PropName.cache_service_expireAfterAccess.property(), "600000"));
        RegexServiceFactory sFactory = new RegexServiceFactory(null);
        sFactory.setExpireAfterWrite(expireAfterWrite)
                .setExpireAfterAccess(expireAfterAccess)
                .setConcurrencyLevel(cLevel)
                .setCacheLoader(new RegexCacheLoader());
        // No destination cache, only for configure purpose.
        sFactory.getCache();
    }

    private void configureUnitService(Properties prop) {
        int cLevel = IntegerUtil.tryParse(prop.getProperty(PropName.cache_service_concurrencyLevel.property(), "4"));
        long expireAfterWrite = LongUtil.tryParse(prop.getProperty(PropName.cache_service_expireAfterWrite.property(), "30000"));
        long expireAfterAccess = LongUtil.tryParse(prop.getProperty(PropName.cache_service_expireAfterAccess.property(), "600000"));
        UnitServiceFactory sFactory = new UnitServiceFactory(null);
        sFactory.setExpireAfterWrite(expireAfterWrite)
                .setExpireAfterAccess(expireAfterAccess)
                .setConcurrencyLevel(cLevel)
                .setCacheLoader(new UnitCacheLoader());
        // No destination cache, only for configure purpose.
        sFactory.getCache();
    }
}
