package ro.ucv.inf.soa.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ro.ucv.inf.soa.model.Feedback;
import ro.ucv.inf.soa.model.FeedbackType;

import java.util.List;

public class FeedbackDAOImpl extends GenericDAOImpl<Feedback, Long> implements FeedbackDAO {

    public FeedbackDAOImpl() {
        super(Feedback.class);
    }

    @Override
    public List<Feedback> findByAssignmentId(Long assignmentId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Feedback> query = em.createQuery(
                    "SELECT f FROM Feedback f WHERE f.assignment.id = :assignmentId", Feedback.class);
            query.setParameter("assignmentId", assignmentId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Feedback> findByType(FeedbackType type) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Feedback> query = em.createQuery(
                    "SELECT f FROM Feedback f WHERE f.feedbackType = :type", Feedback.class);
            query.setParameter("type", type);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Feedback> findByRating(Integer minRating) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Feedback> query = em.createQuery(
                    "SELECT f FROM Feedback f WHERE f.rating >= :minRating", Feedback.class);
            query.setParameter("minRating", minRating);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
