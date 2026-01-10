package ro.ucv.inf.soa.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "Volunteer Management System API", version = "1.0", description = "REST API documentation for Volunteer Management System. Supports standard CRUD operations and advanced statistics.", contact = @Contact(name = "Dondid", email = "admin@volunteer-system.com")), servers = {
        @Server(description = "Local Development Server", url = "/volunteer-management-system/api")
})
public class OpenApiConfig {
}
