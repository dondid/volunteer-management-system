package ro.ucv.inf.soa.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ro.ucv.inf.soa.dao.OrganizationDAO;
import ro.ucv.inf.soa.dao.OrganizationDAOImpl;
import ro.ucv.inf.soa.dto.ApiResponse;
import ro.ucv.inf.soa.model.Organization;

import java.util.List;

@Path("/organizations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@io.swagger.v3.oas.annotations.tags.Tag(name = "Organizations", description = "Operations for managing organizations")
public class OrganizationResource {

    private final OrganizationDAO organizationDAO = new OrganizationDAOImpl();

    @GET
    @io.swagger.v3.oas.annotations.Operation(summary = "List all organizations", description = "Retrieves a list of all registered organizations")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of organizations found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    public Response getAllOrganizations() {
        try {
            List<Organization> organizations = organizationDAO.findAll();
            return Response.ok(ApiResponse.success(organizations)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error retrieving organizations: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get organization by ID", description = "Retrieves a specific organization by its ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Organization found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organization not found")
    public Response getOrganizationById(@PathParam("id") Long id) {
        try {
            return organizationDAO.findById(id)
                    .map(org -> Response.ok(ApiResponse.success(org)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(ApiResponse.error("Organization not found with id: " + id))
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        }
    }

    @POST
    @io.swagger.v3.oas.annotations.Operation(summary = "Create an organization", description = "Registers a new organization")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Organization created successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Email already exists")
    public Response createOrganization(Organization organization) {
        try {
            if (organization.getName() == null || organization.getName().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Organization name is required"))
                        .build();
            }
            if (organization.getEmail() == null || organization.getEmail().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Organization email is required"))
                        .build();
            }
            if (!organization.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Invalid email format"))
                        .build();
            }
            if (organization.getPhone() != null && !organization.getPhone().isEmpty()) {
                if (!organization.getPhone().matches("^\\d{10}$")) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(ApiResponse.error("Phone number must be exactly 10 digits"))
                            .build();
                }
            }

            if (organizationDAO.findByEmail(organization.getEmail()).isPresent()) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(ApiResponse.error("Organization with this email already exists"))
                        .build();
            }

            Organization saved = organizationDAO.save(organization);
            return Response.status(Response.Status.CREATED)
                    .entity(ApiResponse.success("Organization created successfully", saved))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error creating organization: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Update organization", description = "Updates details of an existing organization")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Organization updated successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organization not found")
    public Response updateOrganization(@PathParam("id") Long id, Organization organization) {
        try {
            Organization existing = organizationDAO.findById(id).orElse(null);

            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Organization not found with id: " + id))
                        .build();
            }

            // Update only scalar fields
            if (organization.getName() != null)
                existing.setName(organization.getName());
            if (organization.getDescription() != null)
                existing.setDescription(organization.getDescription());
            if (organization.getEmail() != null) {
                if (!organization.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(ApiResponse.error("Invalid email format"))
                            .build();
                }
                existing.setEmail(organization.getEmail());
            }
            if (organization.getPhone() != null) {
                if (!organization.getPhone().isEmpty() && !organization.getPhone().matches("^\\d{10}$")) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(ApiResponse.error("Phone number must be exactly 10 digits"))
                            .build();
                }
                existing.setPhone(organization.getPhone());
            }
            if (organization.getAddress() != null)
                existing.setAddress(organization.getAddress());
            if (organization.getWebsite() != null)
                existing.setWebsite(organization.getWebsite());
            if (organization.getRegistrationNumber() != null)
                existing.setRegistrationNumber(organization.getRegistrationNumber());
            if (organization.getStatus() != null)
                existing.setStatus(organization.getStatus());

            Organization updated = organizationDAO.update(existing);
            return Response.ok(ApiResponse.success("Organization updated successfully", updated))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error updating organization: " + e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Delete organization", description = "Deletes an organization from the system")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Organization deleted successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organization not found")
    public Response deleteOrganization(@PathParam("id") Long id) {
        try {
            if (!organizationDAO.existsById(id)) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Organization not found with id: " + id))
                        .build();
            }

            organizationDAO.deleteById(id);
            return Response.ok(ApiResponse.success("Organization deleted successfully", null))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error deleting organization: " + e.getMessage()))
                    .build();
        }
    }
}