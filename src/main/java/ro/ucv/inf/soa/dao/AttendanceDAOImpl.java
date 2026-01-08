package ro.ucv.inf.soa.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ro.ucv.inf.soa.model.Attendance;

import java.time.LocalDate;
import java.util.List;

public class AttendanceDAOImpl extends GenericDAOImpl<Attendance, Long> implements AttendanceDAO {

    public AttendanceDAOImpl() {
        super(Attendance.class);
    }

    @Override
    public List<Attendance> findByAssignmentId(Long assignmentId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Attendance> query = em.createQuery(
                    "SELECT a FROM Attendance a WHERE a.assignment.id = :assignmentId", Attendance.class);
            query.setParameter("assignmentId", assignmentId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Attendance> findByDateRange(LocalDate startDate, LocalDate endDate) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Attendance> query = em.createQuery(
                    "SELECT a FROM Attendance a WHERE a.date BETWEEN :startDate AND :endDate", Attendance.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Attendance> findByVolunteerId(Long volunteerId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Attendance> query = em.createQuery(
                    "SELECT a FROM Attendance a WHERE a.assignment.volunteer.id = :volunteerId", Attendance.class);
            query.setParameter("volunteerId", volunteerId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
