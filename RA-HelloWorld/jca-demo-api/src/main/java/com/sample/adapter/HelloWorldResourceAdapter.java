package com.sample.adapter;


import jakarta.resource.ResourceException;
import jakarta.resource.spi.*;
import jakarta.resource.spi.endpoint.MessageEndpointFactory;

import javax.transaction.xa.XAResource;
import java.util.logging.Logger;

/**
 * HelloWorldResourceAdapter
 *
 * @version $Revision: $
 */
@Connector(
        reauthenticationSupport = false,
        transactionSupport = TransactionSupport.TransactionSupportLevel.NoTransaction)
public class HelloWorldResourceAdapter implements ResourceAdapter {
    /**
     * The logger
     */
    private static Logger log = Logger.getLogger("HelloWorldResourceAdapter");

    /**
     * Name property
     */
    @ConfigProperty(defaultValue = "WildFly", supportsDynamicUpdates = true)
    private String name;

    /**
     * Default constructor
     */
    public HelloWorldResourceAdapter() {
    }

    /**
     * Get name
     *
     * @return The value
     */
    public String getName() {
        return name;
    }

    /**
     * Set name
     *
     * @param name The value
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This is called during the activation of a message endpoint.
     *
     * @param endpointFactory A message endpoint factory instance.
     * @param spec            An activation spec JavaBean instance.
     * @throws ResourceException generic exception
     */
    public void endpointActivation(MessageEndpointFactory endpointFactory,
                                   ActivationSpec spec) throws ResourceException {
    }

    /**
     * This is called when a message endpoint is deactivated.
     *
     * @param endpointFactory A message endpoint factory instance.
     * @param spec            An activation spec JavaBean instance.
     */
    public void endpointDeactivation(MessageEndpointFactory endpointFactory,
                                     ActivationSpec spec) {
    }

    /**
     * This is called when a resource adapter instance is bootstrapped.
     *
     * @param ctx A bootstrap context containing references
     * @throws ResourceAdapterInternalException indicates bootstrap failure.
     */
    public void start(BootstrapContext ctx)
            throws ResourceAdapterInternalException {
    }

    /**
     * This is called when a resource adapter instance is undeployed or
     * during application server shutdown.
     */
    public void stop() {
    }

    /**
     * This method is called by the application server during crash recovery.
     *
     * @param specs an array of ActivationSpec JavaBeans
     * @return an array of XAResource objects
     * @throws ResourceException generic exception
     */
    public XAResource[] getXAResources(ActivationSpec[] specs)
            throws ResourceException {
        return null;
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        int result = 17;
        if (name != null)
            result += 31 * result + 7 * name.hashCode();
        else
            result += 31 * result + 7;
        return result;
    }

    /**
     * Indicates whether some other object is equal to this one.
     *
     * @param other The reference object with which to compare.
     * @return true If this object is the same as the obj argument, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (other == this)
            return true;
        if (!(other instanceof HelloWorldResourceAdapter))
            return false;
        HelloWorldResourceAdapter obj = (HelloWorldResourceAdapter) other;
        boolean result = true;
        if (result) {
            if (name == null)
                result = obj.getName() == null;
            else
                result = name.equals(obj.getName());
        }
        return result;
    }
}