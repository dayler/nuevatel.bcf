package com.nuevatel.bcf;

import com.nuevatel.common.ds.JDBCProperties;
import com.nuevatel.common.exception.InvalidPropertyValueException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Main class for BCF app.
 *
 * @author Ariel Salazar
 */
public class BCF {

    private static Logger logger = LogManager.getLogger(BCF.class);

    private static Properties properties = null;

    public static void main(String[] args) {
        try {
            properties = loadBcfProperties(args);
            JDBCProperties jdbcBcfProp = loadJdbcBcfProperties(args);
            JDBCProperties jdbcRecordProp = loadJdbcRecordProperties(args);
            // wait 1 minute to finish all process, 1 thread to do.
            ShutdownHook hook = new ShutdownHook(60, 1);
            BCFProcessor bcfProcessor = new BCFProcessor(properties, jdbcBcfProp, jdbcRecordProp);
            hook.appendProcess(bcfProcessor);

            // Start BCF Processor.
             bcfProcessor.execute();
            logger.info("BCFProcessor was started...");

            Runtime.getRuntime().addShutdownHook(hook);
        } catch (Throwable ex) {
            logger.fatal("Failed to initialize BCF Stand alone app...", ex);
            System.exit(1);
        }
    }

    public static Properties getProperties() {
        return properties;
    }

    private static JDBCProperties loadJdbcBcfProperties(String[] args) throws IOException, InvalidPropertyValueException {
        InputStream is = null;
        try {
            if (args.length < 2) {
                // Load default properties
                is = BCF.class.getResourceAsStream("/jdbc.bcf.properties");
            } else {
                String propPath = args[1];
                is = new FileInputStream(propPath);
            }

            Properties prop = new Properties();
            prop.load(is);
            return  new JDBCProperties(prop);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private static JDBCProperties loadJdbcRecordProperties(String[] args) throws IOException, InvalidPropertyValueException {
        InputStream is = null;
        try {
            if (args.length < 2) {
                // Load default properties
                is = BCF.class.getResourceAsStream("/jdbc.bcf_record.properties");
            } else {
                String propPath = args[1];
                is = new FileInputStream(propPath);
            }

            Properties prop = new Properties();
            prop.load(is);
            return  new JDBCProperties(prop);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private static Properties loadBcfProperties(String[] args) throws IOException {
        InputStream is = null;
        try {
            if (args.length == 0) {
                // Load default properties.
                is = BCF.class.getResourceAsStream("/bcf.properties");
            } else {
                String propPath = args[0];
                is = new FileInputStream(propPath);
            }

            Properties prop = new Properties();
            prop.load(is);
            return prop;
        } finally {
             if (is != null) {
                 is.close();
             }
        }
    }
}
