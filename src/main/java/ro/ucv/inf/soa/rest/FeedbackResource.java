package ro.ucv.inf.soa.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ro.ucv.inf.soa.dao.FeedbackDAO;
import ro.ucv.inf.soa.dao.FeedbackDAOImpl;
import ro.ucv.inf.soa.dto.ApiResponse;
import ro.ucv.inf.soa.model.Feedback;
import ro.ucv.inf.soa.model.FeedbackType;

import java.util.List;

@Path("/feedback")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@io.swagger.v3.oas.annotations.tags.Tag(name = "Feedback", description = "Operations for managing feedback from organizations and volunteers")
public class FeedbackResource {

    private final FeedbackDAO feedbackDAO = new FeedbackDAOImpl();
    private final ro.ucv.inf.soa.dao.AssignmentDAO assignmentDAO = new ro.ucv.inf.soa.dao.AssignmentDAOImpl();

    @GET
    @io.swagger.v3.oas.annotations.Operation(summary = "List feedback", description = "Retrieves feedback with optional filters by assignment, type, or rating")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of feedback entries found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    public Response getAllFeedback(@QueryParam("assignmentId") Long assignmentId,
            @QueryParam("type") String type,
            @QueryParam("minRating") Integer minRating) {
        try {
            List<Feedback> feedbacks;
            if (assignmentId != null) {
                feedbacks = feedbackDAO.findByAssignmentId(assignmentId);
            } else if (type != null) {
                feedbacks = feedbackDAO.findByType(FeedbackType.valueOf(type.toUpperCase()));
            } else if (minRating != null) {
                feedbacks = feedbackDAO.findByRating(minRating);
            } else {
                feedbacks = feedbackDAO.findAll();
            }
            return Response.ok(ApiResponse.success(feedbacks)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error retrieving feedback: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get feedback by ID", description = "Retrieves a specific feedback entry by its ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Feedback found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Feedback not found")
    public Response getFeedbackById(@PathParam("id") Long id) {
        try {
            return feedbackDAO.findById(id)
                    .map(feedback -> Response.ok(ApiResponse.success(feedback)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(ApiResponse.error("Feedback not found with id: " + id))
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        }
    }

    @POST
    @io.swagger.v3.oas.annotations.Operation(summary = "Submit feedback", description = "Creates a new feedback entry for an assignment")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Feedback submitted successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or missing assignment")
    public Response createFeedback(Feedback feedback) {
        try {
            if (feedback.getAssignment() == null || feedback.getAssignment().getId() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Assignment is required"))
                        .build();
            }
            if (feedback.getRating() == null || feedback.getRating() < 1 || feedback.getRating() > 5) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Rating must be between 1 and 5"))
                        .build();
            }

            ro.ucv.inf.soa.model.Assignment assignment = assignmentDAO.findById(feedback.getAssignment().getId())
                    .orElse(null);
            if (assignment == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Assignment not found with id: " + feedback.getAssignment().getId()))
                        .build();
            }
            feedback.setAssignment(assignment);

            Feedback saved = feedbackDAO.save(feedback);

            // Reload to ensure full initialization (avoid proxy errors)
            Feedback fullFeedback = feedbackDAO.findById(saved.getId()).orElse(saved);

            return Response.status(Response.Status.CREATED)
                    .entity(ApiResponse.success("Feedback submitted successfully", fullFeedback))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error creating feedback: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Update feedback", description = "Updates an existing feedback entry")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Feedback updated successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Feedback not found")
    public Response updateFeedback(@PathParam("id") Long id, Feedback feedback) {
        try {
            Feedback existing = feedbackDAO.findById(id).orElse(null);

            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Feedback not found with id: " + id))
                        .build();
            }

            // Update scalar fields
            if (feedback.getRating() != null) {
                if (feedback.getRating() < 1 || feedback.getRating() > 5) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(ApiResponse.error("Rating must be between 1 and 5"))
                            .build();
                }
                existing.setRating(feedback.getRating());
            }
            if (feedback.getComment() != null)
                existing.setComment(feedback.getComment());
            if (feedback.getFeedbackDate() != null)
                existing.setFeedbackDate(feedback.getFeedbackDate());
            if (feedback.getGivenBy() != null)
                existing.setGivenBy(feedback.getGivenBy());
            if (feedback.getFeedbackType() != null)
                existing.setFeedbackType(feedback.getFeedbackType());

            Feedback updated = feedbackDAO.update(existing);
            return Response.ok(ApiResponse.success("Feedback updated successfully", updated))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error updating feedback: " + e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Delete feedback", description = "Removes a feedback entry")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Feedback deleted successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Feedback not found")
    public Response deleteFeedback(@PathParam("id") Long id) {
        try {
            if (!feedbackDAO.existsById(id)) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Feedback not found with id: " + id))
                        .build();
            }

            feedbackDAO.deleteById(id);
            return Response.ok(ApiResponse.success("Feedback deleted successfully", null))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error deleting feedback: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/assignment/{assignmentId}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get feedback by assignment", description = "Retrieves all feedback for a specific assignment")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of feedback entries found")
    public Response getFeedbackByAssignment(@PathParam("assignmentId") Long assignmentId) {
        try {
            List<Feedback> feedbacks = feedbackDAO.findByAssignmentId(assignmentId);
            return Response.ok(ApiResponse.success(feedbacks)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        }
    }
}
