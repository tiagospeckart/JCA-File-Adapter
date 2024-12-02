package com.dcx.jfoss.fra.spi;

import java.util.logging.Logger;

/**
 * Factory class for logger instances
 */
public class LoggingManager {
    private static LoggingManager instance = null;

    public static LoggingManager getInstance() {
        if (instance == null) {
            instance = new LoggingManager();
        }
        return instance;
    }

    public Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }
}