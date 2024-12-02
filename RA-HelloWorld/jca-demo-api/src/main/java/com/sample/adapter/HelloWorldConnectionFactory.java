package com.sample.adapter;

import jakarta.resource.Referenceable;
import jakarta.resource.ResourceException;

import java.io.Serializable;

/**
 * HelloWorldConnectionFactory
 *
 * @version $Revision: $
 */
public interface HelloWorldConnectionFactory extends Serializable, Referenceable {
    /**
     * Get connection from factory
     *
     * @return HelloWorldConnection instance
     * @throws ResourceException Thrown if a connection can't be obtained
     */
    public HelloWorldConnection getConnection() throws ResourceException;

}