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
public class VolunteerResource {

    private final VolunteerDAO volunteerDAO = new VolunteerDAOImpl();

    @GET
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
    public Response updateVolunteer(@PathParam("id") Long id, Volunteer volunteer) {
        try {
            if (!volunteerDAO.existsById(id)) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Volunteer not found with id: " + id))
                        .build();
            }

            volunteer.setId(id);
            Volunteer updated = volunteerDAO.update(volunteer);
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