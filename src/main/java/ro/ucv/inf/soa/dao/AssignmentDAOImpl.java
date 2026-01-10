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
    public java.util.Optional<Assignment> findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Assignment> query = em.createQuery(
                    "SELECT a FROM Assignment a JOIN FETCH a.volunteer JOIN FETCH a.project JOIN FETCH a.project.organization WHERE a.id = :id",
                    Assignment.class);
            query.setParameter("id", id);
            return java.util.Optional.ofNullable(query.getSingleResult());
        } catch (jakarta.persistence.NoResultException e) {
            return java.util.Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Assignment> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "SELECT a FROM Assignment a JOIN FETCH a.volunteer JOIN FETCH a.project JOIN FETCH a.project.organization",
                    Assignment.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Assignment> findByVolunteerId(Long volunteerId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Assignment> query = em.createQuery(
                    "SELECT a FROM Assignment a JOIN FETCH a.volunteer JOIN FETCH a.project JOIN FETCH a.project.organization WHERE a.volunteer.id = :volunteerId",
                    Assignment.class);
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
                    "SELECT a FROM Assignment a JOIN FETCH a.volunteer JOIN FETCH a.project JOIN FETCH a.project.organization WHERE a.project.id = :projectId",
                    Assignment.class);
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
                    "SELECT a FROM Assignment a JOIN FETCH a.volunteer JOIN FETCH a.project JOIN FETCH a.project.organization WHERE a.status = :status",
                    Assignment.class);
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
                    "SELECT a FROM Assignment a JOIN FETCH a.volunteer JOIN FETCH a.project JOIN FETCH a.project.organization WHERE a.volunteer.id = :volunteerId AND a.project.id = :projectId",
                    Assignment.class);
            query.setParameter("volunteerId", volunteerId);
            query.setParameter("projectId", projectId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
