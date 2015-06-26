package com.nuevatel.bcf.alert;

import com.nuevatel.bcf.service.AlertServiceFactory;
import com.nuevatel.common.util.StringUtils;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by asalazar on 6/25/15.
 */
@Plugin(name = "EmailAndSMSAlert", category = "core", elementType = "appender", printObject = true)
public final class EmailAndSMSAlert extends AbstractAppender {

    private ReadWriteLock rwLock = new ReentrantReadWriteLock();

    private Lock readLock = rwLock.readLock();

    private AlertServiceFactory alertServiceFactory = new AlertServiceFactory();

    protected EmailAndSMSAlert(String name,
                               Filter filter,
                               Layout<? extends Serializable> layout,
                               boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
    }

    @Override
    public void append(LogEvent logEvent) {
        try {
            readLock.lock();
            if (logEvent == null || alertServiceFactory.get() == null) {
                return;
            }
            alertServiceFactory.get().appendAlert(new String(getLayout().toByteArray(logEvent)));
        } finally {
            readLock.unlock();
        }
    }

    // Your custom appender needs to declare a factory method
    // annotated with `@PluginFactory`. Log4j will parse the configuration
    // and call this factory method to construct an appender instance with
    // the configured attributes.
    @PluginFactory
    public static EmailAndSMSAlert createAppender(@PluginAttribute("name") String name,
                                                  @PluginElement("Filter") final Filter filter,
                                                  @PluginElement("Layout") Layout<? extends Serializable> layout) {
        if (StringUtils.isBlank(name)) {
            LOGGER.error("No name provided for EmailAndSMSAlert");
            return null;
        }

        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }

        return new EmailAndSMSAlert(name, filter, layout, true);
    }
}
