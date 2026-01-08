package ro.ucv.inf.soa.rest;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ro.ucv.inf.soa.dto.ApiResponse;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        System.out.println(
                "GlobalExceptionMapper CAUGHT: " + exception.getClass().getName() + " - " + exception.getMessage());
        exception.printStackTrace(System.out); // Print to stdout!

        // Return the error message to the client
        return Response.status(Response.Status.BAD_REQUEST) // Use BAD_REQUEST or INTERNAL_SERVER_ERROR depending on
                                                            // logic
                .entity(ApiResponse.error("Global Error: " + exception.getMessage()))
                .build();
    }
}
