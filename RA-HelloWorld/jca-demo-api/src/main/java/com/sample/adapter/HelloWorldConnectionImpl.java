package com.sample.adapter;

import java.util.logging.Logger;

public class HelloWorldConnectionImpl implements HelloWorldConnection {
    /**
     * The logger
     */
    private static Logger log = Logger.getLogger("HelloWorldConnectionImpl");
    /**
     * ManagedConnection
     */
    private HelloWorldManagedConnection mc;
    /**
     * ManagedConnectionFactory
     */
    private HelloWorldManagedConnectionFactory mcf;

    public HelloWorldConnectionImpl(HelloWorldManagedConnection mc,
                                    HelloWorldManagedConnectionFactory mcf) {
        this.mc = mc;
        this.mcf = mcf;
    }

    public String helloWorld() {
        return helloWorld(((HelloWorldResourceAdapter) mcf.getResourceAdapter()).getName());
    }

    public String helloWorld(String name) {
        return mc.helloWorld(name);
    }

    public void close() {
        mc.closeHandle(this);
    }
}