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
public class FeedbackResource {

    private final FeedbackDAO feedbackDAO = new FeedbackDAOImpl();

    @GET
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

            Feedback saved = feedbackDAO.save(feedback);
            return Response.status(Response.Status.CREATED)
                    .entity(ApiResponse.success("Feedback created successfully", saved))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error creating feedback: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateFeedback(@PathParam("id") Long id, Feedback feedback) {
        try {
            if (!feedbackDAO.existsById(id)) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Feedback not found with id: " + id))
                        .build();
            }

            feedback.setId(id);
            Feedback updated = feedbackDAO.update(feedback);
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
