package ro.ucv.inf.soa.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ro.ucv.inf.soa.dao.EventDAO;
import ro.ucv.inf.soa.dao.EventDAOImpl;
import ro.ucv.inf.soa.dto.ApiResponse;
import ro.ucv.inf.soa.model.Event;
import ro.ucv.inf.soa.model.EventStatus;

import java.util.List;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {

    private final EventDAO eventDAO = new EventDAOImpl();
    private final ro.ucv.inf.soa.dao.ProjectDAO projectDAO = new ro.ucv.inf.soa.dao.ProjectDAOImpl();

    @GET
    public Response getAllEvents(@QueryParam("projectId") Long projectId,
            @QueryParam("status") String status,
            @QueryParam("upcoming") Boolean upcoming,
            @QueryParam("available") Boolean available) {
        try {
            List<Event> events;
            if (upcoming != null && upcoming) {
                events = eventDAO.findUpcomingEvents();
            } else if (available != null && available) {
                events = eventDAO.findAvailableEvents();
            } else if (status != null) {
                events = eventDAO.findByStatus(EventStatus.valueOf(status.toUpperCase()));
            } else if (projectId != null) {
                events = eventDAO.findByProjectId(projectId);
            } else {
                events = eventDAO.findAll();
            }
            return Response.ok(ApiResponse.success(events)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error retrieving events: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getEventById(@PathParam("id") Long id) {
        try {
            return eventDAO.findById(id)
                    .map(event -> Response.ok(ApiResponse.success(event)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(ApiResponse.error("Event not found with id: " + id))
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        }
    }

    @POST
    public Response createEvent(Event event) {
        try {
            if (event.getTitle() == null || event.getTitle().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Event title is required"))
                        .build();
            }
            if (event.getEventDate() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Event date is required"))
                        .build();
            }
            if (event.getProject() == null || event.getProject().getId() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Project is required"))
                        .build();
            }

            // Explicit load match
            ro.ucv.inf.soa.model.Project project = projectDAO.findById(event.getProject().getId()).orElse(null);
            if (project == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Project not found with id: " + event.getProject().getId()))
                        .build();
            }
            event.setProject(project);

            Event saved = eventDAO.save(event);

            // Reload to ensure full initialization (avoid proxy errors)
            Event fullEvent = eventDAO.findById(saved.getId()).orElse(saved);

            return Response.status(Response.Status.CREATED)
                    .entity(ApiResponse.success("Event created successfully", fullEvent))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error creating event: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateEvent(@PathParam("id") Long id, Event event) {
        try {
            Event existing = eventDAO.findById(id).orElse(null);

            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Event not found with id: " + id))
                        .build();
            }

            // Update scalar fields
            if (event.getTitle() != null)
                existing.setTitle(event.getTitle());
            if (event.getDescription() != null)
                existing.setDescription(event.getDescription());
            if (event.getEventDate() != null)
                existing.setEventDate(event.getEventDate());
            if (event.getLocation() != null)
                existing.setLocation(event.getLocation());
            if (event.getMaxParticipants() != null)
                existing.setMaxParticipants(event.getMaxParticipants());
            if (event.getCurrentParticipants() != null)
                existing.setCurrentParticipants(event.getCurrentParticipants());
            if (event.getStatus() != null)
                existing.setStatus(event.getStatus());

            // Note: Project association change is not handled here to avoid complexity

            Event updated = eventDAO.update(existing);
            return Response.ok(ApiResponse.success("Event updated successfully", updated))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error updating event: " + e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEvent(@PathParam("id") Long id) {
        try {
            if (!eventDAO.existsById(id)) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Event not found with id: " + id))
                        .build();
            }

            eventDAO.deleteById(id);
            return Response.ok(ApiResponse.success("Event deleted successfully", null))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error deleting event: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/project/{projectId}")
    public Response getEventsByProject(@PathParam("projectId") Long projectId) {
        try {
            List<Event> events = eventDAO.findByProjectId(projectId);
            return Response.ok(ApiResponse.success(events)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        }
    }
}
