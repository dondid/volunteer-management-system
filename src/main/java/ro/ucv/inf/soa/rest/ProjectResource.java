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
public class ProjectResource {

    private final ProjectDAO projectDAO = new ProjectDAOImpl();

    @GET
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

            Project saved = projectDAO.save(project);
            return Response.status(Response.Status.CREATED)
                    .entity(ApiResponse.success("Project created successfully", saved))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error creating project: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateProject(@PathParam("id") Long id, Project project) {
        try {
            if (!projectDAO.existsById(id)) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Project not found with id: " + id))
                        .build();
            }

            project.setId(id);
            Project updated = projectDAO.update(project);
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
