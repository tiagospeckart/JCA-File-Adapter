package com.dcx.jfoss.fra.api;


public class JCAFileAdapterException
        extends Exception {
    private static final long serialVersionUID = 1L;

    public JCAFileAdapterException() {
    }

    public JCAFileAdapterException(Throwable t) {
        super(t);
    }


    public JCAFileAdapterException(String message, Throwable t) {
        super(message, t);
    }


    public JCAFileAdapterException(String message) {
        super(message);
    }
}


