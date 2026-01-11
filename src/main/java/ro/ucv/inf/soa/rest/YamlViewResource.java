package ro.ucv.inf.soa.rest;

import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiContextLocator;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;
import jakarta.servlet.ServletConfig;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/openapi.view")
public class YamlViewResource {

    @Context
    ServletConfig servletConfig;

    @Context
    Application application;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getYamlText() {
        try {
            // Attempt to get existing context or build a new one to ensure we have the
            // model
            // Use JaxrsOpenApiContextBuilder to get or create the context
            OpenApiContext ctx = new JaxrsOpenApiContextBuilder()
                    .servletConfig(servletConfig)
                    .application(application)
                    .resourcePackages(java.util.Collections.singleton("ro.ucv.inf.soa.rest"))
                    .buildContext(true);

            OpenAPI openApi = ctx.read();

            if (openApi == null) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Could not generate OpenAPI definition.")
                        .build();
            }

            // Serialize to YAML using Swagger's built-in mapper
            String yamlContent = Yaml.mapper().writeValueAsString(openApi);

            return Response.ok(yamlContent)
                    .type(MediaType.TEXT_PLAIN) // Force text/plain
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error generating YAML: " + e.getMessage())
                    .build();
        }
    }
}
