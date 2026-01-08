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
    public List<Event> findByProjectId(Long projectId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Event> query = em.createQuery(
                    "SELECT e FROM Event e WHERE e.project.id = :projectId", Event.class);
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
                    "SELECT e FROM Event e WHERE e.status = :status", Event.class);
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
                    "SELECT e FROM Event e WHERE e.eventDate BETWEEN :startDate AND :endDate", Event.class);
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
                    "SELECT e FROM Event e WHERE e.eventDate >= :now AND e.status = :status ORDER BY e.eventDate ASC", Event.class);
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
                    "SELECT e FROM Event e WHERE (e.currentParticipants IS NULL OR e.currentParticipants < e.maxParticipants) " +
                            "AND e.status = :status", Event.class);
            query.setParameter("status", EventStatus.SCHEDULED);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
