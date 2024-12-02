package com.sample.adapter;

import jakarta.resource.NotSupportedException;
import jakarta.resource.ResourceException;
import jakarta.resource.spi.*;

import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class HelloWorldManagedConnection implements ManagedConnection {
    /**
     * The logger
     */
    private static Logger log = Logger.getLogger("HelloWorldManagedConnection");

    /**
     * MCF
     */
    private HelloWorldManagedConnectionFactory mcf;

    /**
     * Log writer
     */
    private PrintWriter logWriter;

    /**
     * Listeners
     */
    private List<ConnectionEventListener> listeners;

    /**
     * Connection
     */
    private Object connection;


    public HelloWorldManagedConnection(HelloWorldManagedConnectionFactory mcf) {
        this.mcf = mcf;
        this.logWriter = null;
        this.listeners = new ArrayList<ConnectionEventListener>(1);
        this.connection = null;
    }


    public Object getConnection(Subject subject,
                                ConnectionRequestInfo cxRequestInfo)
            throws ResourceException {
        connection = new HelloWorldConnectionImpl(this, mcf);

        return connection;
    }


    public void associateConnection(Object connection) throws ResourceException {
        this.connection = connection;
    }


    public void cleanup() throws ResourceException {
    }


    public void destroy() throws ResourceException {
        this.connection = null;
    }


    public void addConnectionEventListener(ConnectionEventListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("Listener is null");

        listeners.add(listener);
    }

    public void removeConnectionEventListener(ConnectionEventListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("Listener is null");

        listeners.remove(listener);
    }


    public PrintWriter getLogWriter() throws ResourceException {
        return logWriter;
    }


    public void setLogWriter(PrintWriter out) throws ResourceException {
        this.logWriter = out;
    }


    public LocalTransaction getLocalTransaction() throws ResourceException {
        throw new NotSupportedException("LocalTransaction not supported");
    }


    public XAResource getXAResource() throws ResourceException {
        throw new NotSupportedException("GetXAResource not supported");
    }


    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        return new HelloWorldManagedConnectionMetaData();
    }


    String helloWorld(String name) {
        return "Hello World, " + name + " !";
    }


    void closeHandle(HelloWorldConnection handle) {
        ConnectionEvent event = new ConnectionEvent(this, ConnectionEvent.CONNECTION_CLOSED);
        event.setConnectionHandle(handle);

        for (ConnectionEventListener cel : listeners) {
            cel.connectionClosed(event);
        }
    }
}