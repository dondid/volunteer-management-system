package ro.ucv.inf.soa.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ro.ucv.inf.soa.model.Assignment;
import ro.ucv.inf.soa.model.AssignmentStatus;

import java.util.List;

public class AssignmentDAOImpl extends GenericDAOImpl<Assignment, Long> implements AssignmentDAO {

    public AssignmentDAOImpl() {
        super(Assignment.class);
    }

    @Override
    public List<Assignment> findByVolunteerId(Long volunteerId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Assignment> query = em.createQuery(
                    "SELECT a FROM Assignment a WHERE a.volunteer.id = :volunteerId", Assignment.class);
            query.setParameter("volunteerId", volunteerId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Assignment> findByProjectId(Long projectId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Assignment> query = em.createQuery(
                    "SELECT a FROM Assignment a WHERE a.project.id = :projectId", Assignment.class);
            query.setParameter("projectId", projectId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Assignment> findByStatus(AssignmentStatus status) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Assignment> query = em.createQuery(
                    "SELECT a FROM Assignment a WHERE a.status = :status", Assignment.class);
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Assignment> findByVolunteerAndProject(Long volunteerId, Long projectId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Assignment> query = em.createQuery(
                    "SELECT a FROM Assignment a WHERE a.volunteer.id = :volunteerId AND a.project.id = :projectId", Assignment.class);
            query.setParameter("volunteerId", volunteerId);
            query.setParameter("projectId", projectId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
