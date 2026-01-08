package ro.ucv.inf.soa.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ro.ucv.inf.soa.dao.AssignmentDAO;
import ro.ucv.inf.soa.dao.AssignmentDAOImpl;
import ro.ucv.inf.soa.dto.ApiResponse;
import ro.ucv.inf.soa.model.Assignment;
import ro.ucv.inf.soa.model.AssignmentStatus;

import java.util.List;

@Path("/assignments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AssignmentResource {

    private final AssignmentDAO assignmentDAO = new AssignmentDAOImpl();

    @GET
    public Response getAllAssignments(@QueryParam("volunteerId") Long volunteerId,
                                      @QueryParam("projectId") Long projectId,
                                      @QueryParam("status") String status) {
        try {
            List<Assignment> assignments;
            if (volunteerId != null && projectId != null) {
                assignments = assignmentDAO.findByVolunteerAndProject(volunteerId, projectId);
            } else if (volunteerId != null) {
                assignments = assignmentDAO.findByVolunteerId(volunteerId);
            } else if (projectId != null) {
                assignments = assignmentDAO.findByProjectId(projectId);
            } else if (status != null) {
                assignments = assignmentDAO.findByStatus(AssignmentStatus.valueOf(status.toUpperCase()));
            } else {
                assignments = assignmentDAO.findAll();
            }
            return Response.ok(ApiResponse.success(assignments)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error retrieving assignments: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getAssignmentById(@PathParam("id") Long id) {
        try {
            return assignmentDAO.findById(id)
                    .map(assignment -> Response.ok(ApiResponse.success(assignment)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(ApiResponse.error("Assignment not found with id: " + id))
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        }
    }

    @POST
    public Response createAssignment(Assignment assignment) {
        try {
            if (assignment.getVolunteer() == null || assignment.getVolunteer().getId() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Volunteer is required"))
                        .build();
            }
            if (assignment.getProject() == null || assignment.getProject().getId() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Project is required"))
                        .build();
            }
            if (assignment.getAssignmentDate() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Assignment date is required"))
                        .build();
            }

            Assignment saved = assignmentDAO.save(assignment);
            return Response.status(Response.Status.CREATED)
                    .entity(ApiResponse.success("Assignment created successfully", saved))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error creating assignment: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateAssignment(@PathParam("id") Long id, Assignment assignment) {
        try {
            if (!assignmentDAO.existsById(id)) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Assignment not found with id: " + id))
                        .build();
            }

            assignment.setId(id);
            Assignment updated = assignmentDAO.update(assignment);
            return Response.ok(ApiResponse.success("Assignment updated successfully", updated))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error updating assignment: " + e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAssignment(@PathParam("id") Long id) {
        try {
            if (!assignmentDAO.existsById(id)) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Assignment not found with id: " + id))
                        .build();
            }

            assignmentDAO.deleteById(id);
            return Response.ok(ApiResponse.success("Assignment deleted successfully", null))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error deleting assignment: " + e.getMessage()))
                    .build();
        }
    }
}
