package ro.ucv.inf.soa.dao;

import ro.ucv.inf.soa.model.Feedback;
import ro.ucv.inf.soa.model.FeedbackType;

import java.util.List;

public interface FeedbackDAO extends GenericDAO<Feedback, Long> {
    List<Feedback> findByAssignmentId(Long assignmentId);
    List<Feedback> findByType(FeedbackType type);
    List<Feedback> findByRating(Integer minRating);
}
