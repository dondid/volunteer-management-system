package ro.ucv.inf.soa.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ro.ucv.inf.soa.dao.ProjectDAO;
import ro.ucv.inf.soa.dao.ProjectDAOImpl;
import ro.ucv.inf.soa.dto.ApiResponse;
import ro.ucv.inf.soa.model.Project;
import ro.ucv.inf.soa.model.ProjectStatus;

import java.util.List;

@Path("/projects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@io.swagger.v3.oas.annotations.tags.Tag(name = "Projects", description = "Operations for managing projects")
public class ProjectResource {

    private final ProjectDAO projectDAO = new ProjectDAOImpl();
    private final ro.ucv.inf.soa.dao.OrganizationDAO organizationDAO = new ro.ucv.inf.soa.dao.OrganizationDAOImpl();

    @GET
    @io.swagger.v3.oas.annotations.Operation(summary = "List projects", description = "Retrieves a list of projects, optionally filtered by status, organization, or availability")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of projects found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    public Response getAllProjects(@QueryParam("status") String status,
            @QueryParam("organizationId") Long organizationId,
            @QueryParam("available") Boolean available) {
        try {
            List<Project> projects;
            if (available != null && available) {
                projects = projectDAO.findAvailableProjects();
            } else if (status != null) {
                projects = projectDAO.findByStatus(ProjectStatus.valueOf(status.toUpperCase()));
            } else if (organizationId != null) {
                projects = projectDAO.findByOrganizationId(organizationId);
            } else {
                projects = projectDAO.findAll();
            }
            return Response.ok(ApiResponse.success(projects)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error retrieving projects: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get project by ID", description = "Retrieves a specific project by its ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Project found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Project not found")
    public Response getProjectById(@PathParam("id") Long id) {
        try {
            return projectDAO.findById(id)
                    .map(proj -> Response.ok(ApiResponse.success(proj)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(ApiResponse.error("Project not found with id: " + id))
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        }
    }

    @POST
    @io.swagger.v3.oas.annotations.Operation(summary = "Create a new project", description = "Creates a new project within an organization")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Project created successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or dates")
    public Response createProject(Project project) {
        try {
            if (project.getTitle() == null || project.getTitle().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Project title is required"))
                        .build();
            }
            if (project.getOrganization() == null || project.getOrganization().getId() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Organization is required"))
                        .build();
            }
            if (project.getStartDate() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Start date is required"))
                        .build();
            }

            ro.ucv.inf.soa.model.Organization organization = organizationDAO.findById(project.getOrganization().getId())
                    .orElse(null);
            if (organization == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse
                                .error("Organization not found with id: " + project.getOrganization().getId()))
                        .build();
            }
            project.setOrganization(organization);

            if (project.getEndDate() != null && project.getStartDate().isAfter(project.getEndDate())) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Start date cannot be after End date"))
                        .build();
            }

            Project saved = projectDAO.save(project);

            // Reload to ensure full initialization (avoid proxy errors)
            Project fullProject = projectDAO.findById(saved.getId())
                    .orElseThrow(() -> new RuntimeException(
                            "CRITICAL: Could not find project after save! ID: " + saved.getId()));

            return Response.status(Response.Status.CREATED)
                    .entity(ApiResponse.success("Project created successfully", fullProject))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error creating project: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Update project", description = "Updates details of an existing project")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Project updated successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid dates or input")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Project not found")
    public Response updateProject(@PathParam("id") Long id, Project project) {
        try {
            Project existing = projectDAO.findById(id).orElse(null);

            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Project not found with id: " + id))
                        .build();
            }

            // Update scalar fields
            if (project.getTitle() != null)
                existing.setTitle(project.getTitle());
            if (project.getDescription() != null)
                existing.setDescription(project.getDescription());
            if (project.getStartDate() != null)
                existing.setStartDate(project.getStartDate());
            if (project.getEndDate() != null)
                existing.setEndDate(project.getEndDate());

            // Validate dates
            if (existing.getEndDate() != null && existing.getStartDate().isAfter(existing.getEndDate())) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Start date cannot be after End date"))
                        .build();
            }

            if (project.getLocation() != null)
                existing.setLocation(project.getLocation());
            if (project.getStatus() != null)
                existing.setStatus(project.getStatus());
            if (project.getMaxVolunteers() != null)
                existing.setMaxVolunteers(project.getMaxVolunteers());
            if (project.getCurrentVolunteers() != null)
                existing.setCurrentVolunteers(project.getCurrentVolunteers());

            // Handle logical updates (e.g. changing organization) if necessary, but careful
            // with full object replacement
            // For now assuming organization change is not primary use case of simple Edit
            // or handled carefully

            projectDAO.update(existing);

            // Reload to ensure full initialization (avoid proxy errors)
            Project updated = projectDAO.findById(id)
                    .orElseThrow(() -> new RuntimeException(
                            "CRITICAL: Could not find project after update! ID: " + id));

            return Response.ok(ApiResponse.success("Project updated successfully", updated))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error updating project: " + e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Delete project", description = "Removes a project from the system")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Project deleted successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Project not found")
    public Response deleteProject(@PathParam("id") Long id) {
        try {
            if (!projectDAO.existsById(id)) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Project not found with id: " + id))
                        .build();
            }

            projectDAO.deleteById(id);
            return Response.ok(ApiResponse.success("Project deleted successfully", null))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error deleting project: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/organization/{organizationId}")
    public Response getProjectsByOrganization(@PathParam("organizationId") Long organizationId) {
        try {
            List<Project> projects = projectDAO.findByOrganizationId(organizationId);
            return Response.ok(ApiResponse.success(projects)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/status/{status}")
    public Response getProjectsByStatus(@PathParam("status") String status) {
        try {
            ProjectStatus projectStatus = ProjectStatus.valueOf(status.toUpperCase());
            List<Project> projects = projectDAO.findByStatus(projectStatus);
            return Response.ok(ApiResponse.success(projects)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Invalid status: " + status))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        }
    }
}
