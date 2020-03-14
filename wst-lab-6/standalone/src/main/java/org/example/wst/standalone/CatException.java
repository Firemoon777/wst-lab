package org.example.wst.standalone;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CatException extends Exception implements ExceptionMapper<CatException> {

    @Override
    public Response toResponse(CatException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    }

    private static final long serialVersionUID = 0L;
    public static CatException DEFAULT_INSTANCE = new CatException("please check the parameters");

    public CatException() {
        super();
    }

    public CatException(String message) {
        super(message);
    }
}
