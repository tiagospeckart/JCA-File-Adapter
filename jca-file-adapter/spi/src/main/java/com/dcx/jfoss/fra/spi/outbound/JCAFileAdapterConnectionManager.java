package com.dcx.jfoss.fra.spi.outbound;

import com.dcx.jfoss.fra.api.JCAFileAdapterConnection;
import com.dcx.jfoss.fra.spi.LoggingManager;
import jakarta.resource.ResourceException;
import jakarta.resource.spi.ConnectionManager;
import jakarta.resource.spi.ConnectionRequestInfo;
import jakarta.resource.spi.ManagedConnection;
import jakarta.resource.spi.ManagedConnectionFactory;

import java.io.Serializable;
import java.util.logging.Logger;

public final class JCAFileAdapterConnectionManager
        implements ConnectionManager, Serializable {

    private static final long serialVersionUID = -1559642990933571679L;
    private static final Logger LOGGER = LoggingManager.getInstance().getLogger(JCAFileAdapterConnectionManager.class);

    public Object allocateConnection(ManagedConnectionFactory mgdConnFactory, ConnectionRequestInfo conReqInfo) throws ResourceException {
        LOGGER.fine("JCA_9000;Called JCAFileAdapterConnectionManager.allocateConnection");
        JCAFileAdapterConnection result = null;

        ManagedConnection mgdConn = mgdConnFactory.createManagedConnection(null, conReqInfo);
        if (mgdConn != null) {
            Object connection = mgdConn.getConnection(null, conReqInfo);
            if (connection instanceof JCAFileAdapterConnection) {
                result = (JCAFileAdapterConnection) connection;
            } else {
                throw new IllegalArgumentException("Not able to retrieve a JCAFileAdapterConnection from getConnection()");
            }
        }

        return result;
    }
}


