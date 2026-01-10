package ro.ucv.inf.soa.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ro.ucv.inf.soa.dao.VolunteerDAO;
import ro.ucv.inf.soa.dao.VolunteerDAOImpl;
import ro.ucv.inf.soa.dto.ApiResponse;
import ro.ucv.inf.soa.model.Volunteer;

import java.util.List;

@Path("/volunteers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@io.swagger.v3.oas.annotations.tags.Tag(name = "Volunteers", description = "Operations for managing volunteers")
public class VolunteerResource {

    private final VolunteerDAO volunteerDAO = new VolunteerDAOImpl();

    @GET
    @io.swagger.v3.oas.annotations.Operation(summary = "List all volunteers", description = "Retrieves a list of all registered volunteers")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of volunteers found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    public Response getAllVolunteers() {
        try {
            List<Volunteer> volunteers = volunteerDAO.findAll();
            return Response.ok(ApiResponse.success(volunteers)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error retrieving volunteers: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get volunteer by ID", description = "Retrieves a specific volunteer by their ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Volunteer found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Volunteer not found")
    public Response getVolunteerById(@PathParam("id") Long id) {
        try {
            return volunteerDAO.findById(id)
                    .map(vol -> Response.ok(ApiResponse.success(vol)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(ApiResponse.error("Volunteer not found with id: " + id))
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        }
    }

    @POST
    @io.swagger.v3.oas.annotations.Operation(summary = "Register a new volunteer", description = "Creates a new volunteer account")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Volunteer created successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Email already exists")
    public Response createVolunteer(Volunteer volunteer) {
        try {
            if (volunteer.getFirstName() == null || volunteer.getFirstName().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("First name is required"))
                        .build();
            }
            if (volunteer.getLastName() == null || volunteer.getLastName().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Last name is required"))
                        .build();
            }
            if (volunteer.getEmail() == null || volunteer.getEmail().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Email is required"))
                        .build();
            }

            if (volunteer.getCnp() != null && !volunteer.getCnp().isEmpty()) {
                if (!volunteer.getCnp().matches("^\\d{13}$")) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(ApiResponse.error("CNP must be exactly 13 digits"))
                            .build();
                }
            }
            if (!volunteer.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Invalid email format"))
                        .build();
            }
            if (volunteer.getPhone() != null && !volunteer.getPhone().isEmpty()) {
                if (!volunteer.getPhone().matches("^\\d{10}$")) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(ApiResponse.error("Phone number must be exactly 10 digits"))
                            .build();
                }
            }

            if (volunteerDAO.findByEmail(volunteer.getEmail()).isPresent()) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(ApiResponse.error("Volunteer with this email already exists"))
                        .build();
            }

            Volunteer saved = volunteerDAO.save(volunteer);
            return Response.status(Response.Status.CREATED)
                    .entity(ApiResponse.success("Volunteer created successfully", saved))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error creating volunteer: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Update volunteer", description = "Updates details of an existing volunteer")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Volunteer updated successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Volunteer not found")
    public Response updateVolunteer(@PathParam("id") Long id, Volunteer volunteer) {
        try {
            Volunteer existing = volunteerDAO.findById(id).orElse(null);

            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Volunteer not found with id: " + id))
                        .build();
            }

            // Assign ID to the object for update
            volunteer.setId(id);

            // Fill in missing fields from existing if null (so we don't null them out in
            // JPQL)
            if (volunteer.getFirstName() == null)
                volunteer.setFirstName(existing.getFirstName());
            if (volunteer.getLastName() == null)
                volunteer.setLastName(existing.getLastName());
            if (volunteer.getEmail() != null) {
                if (!volunteer.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(ApiResponse.error("Invalid email format"))
                            .build();
                }
                existing.setEmail(volunteer.getEmail());
            }
            if (volunteer.getPhone() != null) {
                if (!volunteer.getPhone().isEmpty() && !volunteer.getPhone().matches("^\\d{10}$")) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(ApiResponse.error("Phone number must be exactly 10 digits"))
                            .build();
                }
                existing.setPhone(volunteer.getPhone());
            }
            if (volunteer.getDateOfBirth() == null)
                volunteer.setDateOfBirth(existing.getDateOfBirth());
            if (volunteer.getAddress() == null)
                volunteer.setAddress(existing.getAddress());
            if (volunteer.getCity() == null)
                volunteer.setCity(existing.getCity());
            if (volunteer.getCounty() == null)
                volunteer.setCounty(existing.getCounty());
            if (volunteer.getBio() == null)
                volunteer.setBio(existing.getBio());
            if (volunteer.getStatus() == null)
                volunteer.setStatus(existing.getStatus());

            // Use the safe JPQL update method
            volunteerDAO.updateDetails(volunteer);

            // Fetch fresh record to return
            Volunteer updated = volunteerDAO.findById(id).orElse(null);
            return Response.ok(ApiResponse.success("Volunteer updated successfully", updated))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error updating volunteer: " + e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Delete volunteer", description = "Removes a volunteer from the system")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Volunteer deleted successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Volunteer not found")
    public Response deleteVolunteer(@PathParam("id") Long id) {
        try {
            if (!volunteerDAO.existsById(id)) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Volunteer not found with id: " + id))
                        .build();
            }

            volunteerDAO.deleteById(id);
            return Response.ok(ApiResponse.success("Volunteer deleted successfully", null))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error deleting volunteer: " + e.getMessage()))
                    .build();
        }
    }
}