package com.dcx.jfoss.fra.war.exceptions;

import java.io.Serial;

public class TDOCBusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public TDOCBusinessException(String message) {
        super(message);
    }
}
