package com.dcx.jfoss.fra.spi.outbound;

public interface JCAFileAdapterParams {
    String getRootPath();

    boolean isFileAccessLocked();

    void close();
}