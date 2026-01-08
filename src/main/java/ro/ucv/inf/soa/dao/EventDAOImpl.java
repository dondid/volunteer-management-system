package ro.ucv.inf.soa.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ro.ucv.inf.soa.model.Event;
import ro.ucv.inf.soa.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public class EventDAOImpl extends GenericDAOImpl<Event, Long> implements EventDAO {

    public EventDAOImpl() {
        super(Event.class);
    }

    @Override
    public java.util.Optional<Event> findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Event> query = em.createQuery(
                    "SELECT e FROM Event e JOIN FETCH e.project p JOIN FETCH p.organization WHERE e.id = :id",
                    Event.class);
            query.setParameter("id", id);
            return java.util.Optional.ofNullable(query.getSingleResult());
        } catch (jakarta.persistence.NoResultException e) {
            return java.util.Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Event> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Event e JOIN FETCH e.project p JOIN FETCH p.organization", Event.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Event> findByProjectId(Long projectId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Event> query = em.createQuery(
                    "SELECT e FROM Event e JOIN FETCH e.project p JOIN FETCH p.organization WHERE e.project.id = :projectId",
                    Event.class);
            query.setParameter("projectId", projectId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Event> findByStatus(EventStatus status) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Event> query = em.createQuery(
                    "SELECT e FROM Event e JOIN FETCH e.project p JOIN FETCH p.organization WHERE e.status = :status",
                    Event.class);
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Event> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Event> query = em.createQuery(
                    "SELECT e FROM Event e JOIN FETCH e.project p JOIN FETCH p.organization WHERE e.eventDate BETWEEN :startDate AND :endDate",
                    Event.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Event> findUpcomingEvents() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Event> query = em.createQuery(
                    "SELECT e FROM Event e JOIN FETCH e.project p JOIN FETCH p.organization WHERE e.eventDate >= :now AND e.status = :status ORDER BY e.eventDate ASC",
                    Event.class);
            query.setParameter("now", LocalDateTime.now());
            query.setParameter("status", EventStatus.SCHEDULED);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Event> findAvailableEvents() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Event> query = em.createQuery(
                    "SELECT e FROM Event e JOIN FETCH e.project p JOIN FETCH p.organization WHERE (e.currentParticipants IS NULL OR e.currentParticipants < e.maxParticipants) "
                            +
                            "AND e.status = :status",
                    Event.class);
            query.setParameter("status", EventStatus.SCHEDULED);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
