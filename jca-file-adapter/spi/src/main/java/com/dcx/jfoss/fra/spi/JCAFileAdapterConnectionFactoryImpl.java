package com.dcx.jfoss.fra.spi;

import com.dcx.jfoss.fra.api.JCAFileAdapterConnection;
import com.dcx.jfoss.fra.api.JCAFileAdapterConnectionFactory;
import com.dcx.jfoss.fra.api.JCAFileAdapterException;
import jakarta.resource.ResourceException;
import jakarta.resource.spi.ConnectionManager;
import jakarta.resource.spi.ManagedConnectionFactory;

import javax.naming.NamingException;
import javax.naming.Reference;
import java.util.logging.Logger;

public class JCAFileAdapterConnectionFactoryImpl implements JCAFileAdapterConnectionFactory {
    private static final long serialVersionUID = -5239443999834211259L;
    private static final Logger LOGGER = LoggingManager.getInstance().getLogger(JCAFileAdapterConnectionFactoryImpl.class);

    private Reference reference = null;
    private ConnectionManager connectionManager = null;
    private ManagedConnectionFactory managedConnectionFactory = null;

    public JCAFileAdapterConnectionFactoryImpl(ConnectionManager connManager, ManagedConnectionFactory managedConnFactory) {
        this.connectionManager = connManager;
        this.managedConnectionFactory = managedConnFactory;
    }

    public Reference getReference() throws NamingException {
        return this.reference;
    }

    public void setReference(Reference ref) {
        this.reference = ref;
    }

    public JCAFileAdapterConnection getConnection() throws JCAFileAdapterException {
        JCAFileAdapterConnection result = null;
        try {
            result = ((JCAFileAdapterConnection) getConnectionManager().allocateConnection(getManagedConnectionFactory(), null));
        } catch (ResourceException e) {
            LOGGER.warning("JCA_0001;Unable to get connection [" + e.getMessage() + "]");
            throw new JCAFileAdapterException(e);
        }

        return result;
    }

    protected ManagedConnectionFactory getManagedConnectionFactory() {
        return this.managedConnectionFactory;
    }

    protected ConnectionManager getConnectionManager() {
        return this.connectionManager;
    }
}


