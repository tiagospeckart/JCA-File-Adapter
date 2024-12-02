package com.dcx.jfoss.fra.spi.outbound;

import com.dcx.jfoss.fra.api.JCAFileAdapterConnection;
import com.dcx.jfoss.fra.spi.JCAFileAdapterConnectionImpl;
import com.dcx.jfoss.fra.spi.LoggingManager;
import jakarta.resource.NotSupportedException;
import jakarta.resource.ResourceException;
import jakarta.resource.spi.*;

import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Logger;

public class JCAFileAdapterManagedConnection implements ManagedConnection, JCAFileAdapterParams {
    private static final Logger LOGGER = LoggingManager.getInstance().getLogger(JCAFileAdapterManagedConnection.class);

    private List<ConnectionEventListener> listeners = Collections.synchronizedList(new LinkedList<>());
    private PrintWriter logWriter = null;
    private String rootPath = null;
    private boolean lockedFileAccess = false;
    private JCAFileAdapterConnectionImpl connectionHandle = null;

    public JCAFileAdapterManagedConnection(String path, boolean lockedAccess, PrintWriter logWriter) {
        this.rootPath = path;
        this.lockedFileAccess = lockedAccess;
        this.logWriter = logWriter;
    }

    public Object getConnection(Subject subject, ConnectionRequestInfo connReqInfo) throws ResourceException {
        LOGGER.fine("JCA_9000;Called JCAFileAdapterManangedConnection.getConnection()");
        this.connectionHandle = new JCAFileAdapterConnectionImpl(this);
        return this.connectionHandle;
    }

    public void destroy() throws ResourceException {
        LOGGER.fine("JCA_9000;Called JCAFileAdapterManangedConnection.destroy() - closing connection & cleanup");
        close();
        cleanup();
    }

    @Override
    public void cleanup() throws ResourceException {
        LOGGER.fine("JCA_9000;Called JCAFileAdapterManangedConnection.cleanup()");
        if (this.listeners != null) {
            this.listeners.clear();
        }
    }

    public void close() {
        try {
            final ConnectionEvent event = new ConnectionEvent(this, 1);
            event.setConnectionHandle(this.connectionHandle);
            fireConnectionEvent(event);
            if (this.listeners != null) {
                this.listeners.clear();
            }
        } catch (RuntimeException e) {
            LOGGER.warning("JCA_0008;Exception occurred while closing connection: [" + e.getMessage() + "]");
        }
    }

    public void associateConnection(Object con) throws ResourceException {
    }

    public void addConnectionEventListener(ConnectionEventListener listener) {
        LOGGER.finest("JCA_9000;Called JCAFileAdapterManangedConnection.addConnectionListener()");
        this.listeners.add(listener);
    }

    public void removeConnectionEventListener(ConnectionEventListener listener) {
        LOGGER.finest("JCA_9000;Called JCAFileAdapterManangedConnection.removeConnectionListener()");
        this.listeners.remove(listener);
    }

    public XAResource getXAResource() throws ResourceException {
        LOGGER.finest("JCA_0004;Calls to getXAResource() always lead to a javax.resource.NotSupportedException");
        throw new NotSupportedException();
    }

    public LocalTransaction getLocalTransaction() throws ResourceException {
        LOGGER.finest("JCA_0005;Calls to getLocalTransaction() always lead to a javax.resource.NotSupportedException");
        throw new NotSupportedException();
    }

    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        return new JCAFileAdapterManagedConnectionMetaData();
    }

    public void setLogWriter(PrintWriter logWriter) throws ResourceException {
        this.logWriter = logWriter;
    }

    public PrintWriter getLogWriter() throws ResourceException {
        return this.logWriter;
    }

    public String getRootPath() {
        return this.rootPath;
    }

    public boolean isFileAccessLocked() {
        return this.lockedFileAccess;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        JCAFileAdapterManagedConnection other = (JCAFileAdapterManagedConnection) obj;

        return Objects.equals(rootPath, other.rootPath)
                && lockedFileAccess == other.lockedFileAccess;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rootPath, lockedFileAccess);
    }

    protected final void fireConnectionEvent(ConnectionEvent event) {
        switch (event.getId()) {
            case 1: {
                LOGGER.finest("JCA_9000;Call JCAFileAdapterManagedConnection.fireConnectionEvent CONNECTION_CLOSED ...");
            }
            fireConnectionClosedEvent(event);
            break;
            case 5: {
                LOGGER.finest("JCA_9000;Called JCAFileAdapterManagedConnection.fireConnectionEvent CONNECTION_ERROR_OCCURED");
            }
            fireConnectionErrorOccuredEvent(event);
            break;
            case 3: {
                LOGGER.finest("JCA_9000;Called JCAFileAdapterManagedConnection.fireConnectionEvent LOCAL_TRANSACTION_COMMITTED");
            }
            fireLocalTransactionCommittedEvent(event);
            break;
            case 4: {
                LOGGER.finest("JCA_9000;Called JCAFileAdapterManagedConnection.fireConnectionEvent LOCAL_TRANSACTION_ROLLED_BACK");
            }
            fireLocalTransactionRolledbackEvent(event);
            break;
            case 2: {
                LOGGER.finest("JCA_9000;Called JCAFileAdapterManagedConnection.fireConnectionEvent LOCAL_TRANSACTION_STARTED");
            }
            fireLocalTransactionStartedEvent(event);
            break;
        }
    }

    private final void fireConnectionClosedEvent(ConnectionEvent event) {
        if (listeners != null && !listeners.isEmpty()) {
            final Object[] listenerArray = this.listeners.toArray();
            for (int i = 0; i < listenerArray.length; i++) {
                if (listenerArray[i] != null && listenerArray[i] instanceof ConnectionEventListener) {
                    if (event != null && event.getConnectionHandle() != null && event.getConnectionHandle() instanceof JCAFileAdapterConnection) {
                        if (!((JCAFileAdapterConnection) event.getConnectionHandle()).isClosed()) {
                            try {
                                ((ConnectionEventListener) listenerArray[i]).connectionClosed(event);
                            } catch (Exception e) {
                                LOGGER.finest(e.getMessage());
                            }
                        }
                    } else {
                        ((ConnectionEventListener) listenerArray[i]).connectionClosed(event);
                    }
                }
            }
        }
    }

    private final void fireConnectionErrorOccuredEvent(ConnectionEvent event) {
        Iterator<ConnectionEventListener> it = this.listeners.iterator();
        while (it.hasNext()) {
            ConnectionEventListener listener = it.next();
            listener.connectionErrorOccurred(event);
        }
    }

    private final void fireLocalTransactionCommittedEvent(ConnectionEvent event) {
        Iterator<ConnectionEventListener> it = this.listeners.iterator();
        while (it.hasNext()) {
            ConnectionEventListener listener = it.next();
            listener.localTransactionCommitted(event);
        }
    }

    private final void fireLocalTransactionRolledbackEvent(ConnectionEvent event) {
        Iterator<ConnectionEventListener> it = this.listeners.iterator();
        while (it.hasNext()) {
            ConnectionEventListener listener = it.next();
            listener.localTransactionRolledback(event);
        }
    }

    private final void fireLocalTransactionStartedEvent(ConnectionEvent event) {
        Iterator<ConnectionEventListener> it = this.listeners.iterator();
        while (it.hasNext()) {
            ConnectionEventListener listener = it.next();
            listener.localTransactionStarted(event);
        }
    }

    public JCAFileAdapterConnectionImpl getConnectionHandle() {
        return connectionHandle;
    }
}


