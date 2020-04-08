package org.example.wst.standalone;

import javax.xml.ws.WebFault;

@WebFault
public class CatException extends Exception {
    private static final long serialVersionUID = -6647544772732631047L;
    private final CatServiceFault fault;

    public CatException(String message, CatServiceFault fault) {
        super(message);
        this.fault = fault;
    }

    public CatException(String message, CatServiceFault fault, Throwable cause) {
        super(message, cause);
        this.fault = fault;
    }

    public CatServiceFault getFaultInfo() {
        return fault;
    }
}
