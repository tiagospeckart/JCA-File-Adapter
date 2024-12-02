package com.dcx.jfoss.fra.spi.outbound;

import com.dcx.jfoss.fra.api.JCAFileAdapterConnection;
import com.dcx.jfoss.fra.api.JCAFileAdapterConnectionFactory;
import com.dcx.jfoss.fra.spi.JCAFileAdapterConnectionFactoryImpl;
import com.dcx.jfoss.fra.spi.JCAFileAdapterConnectionImpl;
import com.dcx.jfoss.fra.spi.LoggingManager;
import jakarta.resource.ResourceException;
import jakarta.resource.spi.*;

import javax.security.auth.Subject;
import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

@ConnectionDefinition(connectionFactory = JCAFileAdapterConnectionFactory.class,
        connectionFactoryImpl = JCAFileAdapterConnectionFactoryImpl.class,
        connection = JCAFileAdapterConnection.class,
        connectionImpl = JCAFileAdapterConnectionImpl.class)
public class JCAFileAdapterManagedConnectionFactory implements ManagedConnectionFactory, ResourceAdapterAssociation {
    private static final long serialVersionUID = 3196531800449048606L;
    private static final Logger LOGGER = LoggingManager.getInstance().getLogger(JCAFileAdapterManagedConnectionFactory.class);

    private ResourceAdapter resourceAdapter = null;
    private PrintWriter logWriter = null;

    // Root path for file access. Use only absolute pathname
    private String rootPath = null;
    // Specifies if write operations to files should lock the file
    private Boolean lockedFileAccess = null;

    @ConfigProperty(
            description = {"Root path for file access. Use only absolute pathnames."},
            defaultValue = "",
            supportsDynamicUpdates = true,
            type = String.class)
    public void setRootPath(String path) {
        if (path == null || path.indexOf(".") != -1) {
            LOGGER.severe("JCA_0006;Illegal rootPath setting. Path: [" + path + "]");
            throw new IllegalArgumentException("Value for root path contains illegal character(s): " + path);
        }
        this.rootPath = path;
        if (!this.rootPath.endsWith("/") && !this.rootPath.endsWith("\\")) {
            this.rootPath += File.separator;
        }
    }

    @ConfigProperty(
            description = {"Specifies if write operations to files should lock the file."},
            defaultValue = "true",
            supportsDynamicUpdates = true,
            type = Boolean.class)
    public void setLockedFileAccess(Boolean lockedFileAccess) {
        this.lockedFileAccess = lockedFileAccess;
    }

    public Object createConnectionFactory(ConnectionManager connManager) throws ResourceException {
        LOGGER.info("JCA_9000;Called JCAFileAdapterManagedConnectionFactory.createConnectionFactory(conMgr)");
        return new JCAFileAdapterConnectionFactoryImpl(connManager, this);
    }

    public Object createConnectionFactory() throws ResourceException {
        throw new IllegalArgumentException("Not supported!");
    }

    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo connReqInfo) throws ResourceException {
        LOGGER.finest("JCA_9000;Called JCAFileAdapterManagedConnectionFactory.createManagedConnection()");
        return new JCAFileAdapterManagedConnection(this.rootPath, this.lockedFileAccess, this.logWriter);
    }

    public ManagedConnection matchManagedConnections(Set set, Subject subject, ConnectionRequestInfo connReqInfo) throws ResourceException {
        if (set != null && !set.isEmpty()) {
            for (Object o : set) {
                if (o instanceof JCAFileAdapterManagedConnection) {
                    JCAFileAdapterManagedConnection jcaConn = (JCAFileAdapterManagedConnection) o;
                    if (jcaConn.getConnectionHandle() != null && !jcaConn.getConnectionHandle().isClosed()) {
                        return jcaConn; // Return the first non-closed connection
                    }
                }
            }
        }

        return null; // No suitable connection found
    }

    public void setLogWriter(PrintWriter logWriter) throws ResourceException {
        this.logWriter = logWriter;
    }

    public PrintWriter getLogWriter() throws ResourceException {
        return this.logWriter;
    }

    public ResourceAdapter getResourceAdapter() {
        return this.resourceAdapter;
    }

    public void setResourceAdapter(ResourceAdapter resourceAdapter) throws ResourceException {
        this.resourceAdapter = resourceAdapter;
    }

    public String getRootPath() {
        return this.rootPath;
    }

    public Boolean getLockedFileAccess() {
        return Boolean.valueOf(this.lockedFileAccess);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        JCAFileAdapterManagedConnectionFactory other = (JCAFileAdapterManagedConnectionFactory) obj;

        return Objects.equals(rootPath, other.rootPath)
                && lockedFileAccess == other.lockedFileAccess;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rootPath, lockedFileAccess);
    }
}


