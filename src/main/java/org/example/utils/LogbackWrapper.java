package org.example.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

/**
 * wrapper for configuring and returning Logback Logger instances.
 */
public class LogbackWrapper {

    private static final String DEFAULT_CONFIG_FILE = "logback.xml";

    /* use a static initializer for thread safety and so we can handle errors */
    static {
        try {
            System.out.println("Loading logback config file: " + DEFAULT_CONFIG_FILE);

            final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            context.reset();

            final JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            configurator.doConfigure(LogbackWrapper.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILE));
//			configurator.doConfigure(new File(DEFAULT_CONFIG_FILE));
        }
        catch (Exception ex) {
            System.out.println("Exception thrown while setting up logging");
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public static Logger getLogger(Class<?> mainClass) {
//		System.out.println("getting a logger");
        return LoggerFactory.getLogger(mainClass);
    }
}
