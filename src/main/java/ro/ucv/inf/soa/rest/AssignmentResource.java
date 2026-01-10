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
@io.swagger.v3.oas.annotations.tags.Tag(name = "Assignments", description = "Operations for managing assignments")
public class AssignmentResource {

    private final AssignmentDAO assignmentDAO = new AssignmentDAOImpl();
    private final ro.ucv.inf.soa.dao.VolunteerDAO volunteerDAO = new ro.ucv.inf.soa.dao.VolunteerDAOImpl();
    private final ro.ucv.inf.soa.dao.ProjectDAO projectDAO = new ro.ucv.inf.soa.dao.ProjectDAOImpl();

    @GET
    @io.swagger.v3.oas.annotations.Operation(summary = "List assignments", description = "Retrieves a list of assignments with optional filters")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of assignments found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
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
    @io.swagger.v3.oas.annotations.Operation(summary = "Get assignment by ID", description = "Retrieves a specific assignment by its ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Assignment found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Assignment not found")
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
    @io.swagger.v3.oas.annotations.Operation(summary = "Create assignment", description = "Assigns a volunteer to a project")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Assignment created successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or business rule violation")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Duplicate assignment or capacity reached")
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

            // Explicitly load related entities to ensure they exist and are attached
            ro.ucv.inf.soa.model.Volunteer volunteer = volunteerDAO.findById(assignment.getVolunteer().getId())
                    .orElse(null);
            if (volunteer == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Volunteer not found with id: " + assignment.getVolunteer().getId()))
                        .build();
            }

            ro.ucv.inf.soa.model.Project project = projectDAO.findById(assignment.getProject().getId()).orElse(null);
            if (project == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Project not found with id: " + assignment.getProject().getId()))
                        .build();
            }

            // Professional Logic: Cannot assign to closed projects
            if (project.getStatus() == ro.ucv.inf.soa.model.ProjectStatus.COMPLETED ||
                    project.getStatus() == ro.ucv.inf.soa.model.ProjectStatus.CANCELLED) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Cannot assign volunteers to a " + project.getStatus() + " project"))
                        .build();
            }

            // Check if project has reached maximum capacity
            int currentCount = assignmentDAO.findByProjectId(assignment.getProject().getId()).size();
            if (currentCount >= project.getMaxVolunteers()) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(ApiResponse.error("Project has reached its maximum capacity of "
                                + project.getMaxVolunteers() + " volunteers"))
                        .build();
            }

            // Check for duplicate assignment
            List<Assignment> existingAssignments = assignmentDAO.findByVolunteerAndProject(
                    assignment.getVolunteer().getId(),
                    assignment.getProject().getId());

            if (!existingAssignments.isEmpty()) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(ApiResponse.error("Volunteer is already assigned to this project"))
                        .build();
            }

            assignment.setVolunteer(volunteer);
            assignment.setProject(project);

            Assignment saved = assignmentDAO.save(assignment);

            // Reload to ensure full initialization (avoid proxy errors)
            Assignment fullAssignment = assignmentDAO.findById(saved.getId()).orElse(saved);

            return Response.status(Response.Status.CREATED)
                    .entity(ApiResponse.success("Assignment created successfully", fullAssignment))
                    .build();
        } catch (Exception e) {
            e.printStackTrace(); // Log detailed error
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error creating assignment: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Update assignment", description = "Updates details of an existing assignment")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Assignment updated successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Assignment not found")
    public Response updateAssignment(@PathParam("id") Long id, Assignment assignment) {
        try {
            Assignment existing = assignmentDAO.findById(id).orElse(null);

            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Assignment not found with id: " + id))
                        .build();
            }

            // Update scalar fields
            if (assignment.getAssignmentDate() != null)
                existing.setAssignmentDate(assignment.getAssignmentDate());
            if (assignment.getRole() != null)
                existing.setRole(assignment.getRole());
            if (assignment.getNotes() != null)
                existing.setNotes(assignment.getNotes());
            if (assignment.getStatus() != null)
                existing.setStatus(assignment.getStatus());

            Assignment updated = assignmentDAO.update(existing);
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
    @io.swagger.v3.oas.annotations.Operation(summary = "Delete assignment", description = "Removes an assignment from the system")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Assignment deleted successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Assignment not found")
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
