package ro.ucv.inf.soa.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import ro.ucv.inf.soa.dao.FeedbackDAO;
import ro.ucv.inf.soa.dao.FeedbackDAOImpl;
import ro.ucv.inf.soa.model.Feedback;

import java.util.List;

@WebService(serviceName = "FeedbackService")
public class FeedbackSoapService {

    private final FeedbackDAO feedbackDAO = new FeedbackDAOImpl();

    @WebMethod
    public List<Feedback> getAllFeedback() {
        return feedbackDAO.findAll();
    }

    @WebMethod
    public Feedback getFeedbackById(Long id) {
        return feedbackDAO.findById(id).orElse(null);
    }

    @WebMethod
    public Feedback addFeedback(Feedback feedback) {
        if (feedback.getAssignment() != null && feedback.getAssignment().getId() != null) {
            ro.ucv.inf.soa.dao.AssignmentDAO assignDAO = new ro.ucv.inf.soa.dao.AssignmentDAOImpl();
            ro.ucv.inf.soa.model.Assignment assign = assignDAO.findById(feedback.getAssignment().getId()).orElse(null);
            if (assign != null) {
                feedback.setAssignment(assign);
            }
        }
        return feedbackDAO.save(feedback);
    }

    @WebMethod
    public Feedback updateFeedback(Feedback feedback) {
        Feedback existing = feedbackDAO.findById(feedback.getId()).orElse(null);
        if (existing == null) {
            throw new RuntimeException("Feedback not found with ID: " + feedback.getId());
        }

        if (feedback.getRating() != null)
            existing.setRating(feedback.getRating());
        if (feedback.getComment() != null)
            existing.setComment(feedback.getComment());
        if (feedback.getFeedbackType() != null)
            existing.setFeedbackType(feedback.getFeedbackType());
        // Check if getStatus exists or if it's handled differently, assuming standard
        // getter/setter

        if (feedback.getAssignment() != null && feedback.getAssignment().getId() != null) {
            ro.ucv.inf.soa.dao.AssignmentDAO assignDAO = new ro.ucv.inf.soa.dao.AssignmentDAOImpl();
            ro.ucv.inf.soa.model.Assignment assign = assignDAO.findById(feedback.getAssignment().getId()).orElse(null);
            if (assign != null) {
                existing.setAssignment(assign);
            }
        }

        return feedbackDAO.update(existing);
    }

    @WebMethod
    public void deleteFeedback(Long id) {
        feedbackDAO.deleteById(id);
    }
}
