package org.example.wst.standalone;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ThrottlingException extends Exception implements ExceptionMapper<ThrottlingException> {
    @Override
    public Response toResponse(ThrottlingException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    }

    private static final long serialVersionUID = 0L;
    public static ThrottlingException DEFAULT_INSTANCE = new ThrottlingException("");

    public ThrottlingException() {
        super();
    }

    public ThrottlingException(String message) {
        super(message);
    }
}
