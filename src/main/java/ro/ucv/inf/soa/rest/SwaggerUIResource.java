package ro.ucv.inf.soa.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.InputStream;

/**
 * Resource to serve Swagger UI documentation page
 */
@Path("/swagger-ui")
public class SwaggerUIResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getSwaggerUI() {
        try {
            InputStream htmlStream = getClass().getClassLoader()
                    .getResourceAsStream("META-INF/resources/swagger-ui.html");

            if (htmlStream == null) {
                // Fallback to webapp location
                htmlStream = getClass().getClassLoader()
                        .getResourceAsStream("swagger-ui.html");
            }

            if (htmlStream == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Swagger UI not found")
                        .build();
            }

            return Response.ok(htmlStream).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error loading Swagger UI: " + e.getMessage())
                    .build();
        }
    }
}
