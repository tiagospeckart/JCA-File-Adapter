package com.dcx.jfoss.fra.spi;

import java.util.logging.Logger;

public class JCACustomLogFactory {

    /**
     * Application Logger.
     */
    private static final Logger LOG = LoggingManager.getInstance().getLogger(JCACustomLogFactory.class);

    private String _prefix = "";

    public void setPrefix(String prefix) {
        _prefix = prefix;
    }

    public Logger getLog(String channel) {
        return LOG;
    }
}