package com.dcx.jfoss.fra.spi.outbound;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.ManagedConnectionMetaData;

public class JCAFileAdapterManagedConnectionMetaData
        implements ManagedConnectionMetaData {
    public String getEISProductName() throws ResourceException {
        return "";
    }


    public String getEISProductVersion() throws ResourceException {
        return "";
    }


    public int getMaxConnections() throws ResourceException {
        return 0;
    }


    public String getUserName() throws ResourceException {
        return "";
    }
}


