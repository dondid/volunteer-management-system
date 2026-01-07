package ro.ucv.inf.soa.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import ro.ucv.inf.soa.model.Volunteer;
import ro.ucv.inf.soa.model.VolunteerStatus;

import java.util.List;
import java.util.Optional;

public class VolunteerDAOImpl extends GenericDAOImpl<Volunteer, Long> implements VolunteerDAO {

    public VolunteerDAOImpl() {
        super(Volunteer.class);
    }

    @Override
    public Optional<Volunteer> findByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Volunteer> query = em.createQuery(
                    "SELECT v FROM Volunteer v WHERE v.email = :email", Volunteer.class);
            query.setParameter("email", email);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Volunteer> findByCnp(String cnp) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Volunteer> query = em.createQuery(
                    "SELECT v FROM Volunteer v WHERE v.cnp = :cnp", Volunteer.class);
            query.setParameter("cnp", cnp);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Volunteer> findByStatus(VolunteerStatus status) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Volunteer> query = em.createQuery(
                    "SELECT v FROM Volunteer v WHERE v.status = :status", Volunteer.class);
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Volunteer> findByCity(String city) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Volunteer> query = em.createQuery(
                    "SELECT v FROM Volunteer v WHERE v.city = :city", Volunteer.class);
            query.setParameter("city", city);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Volunteer> findByNameContaining(String name) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Volunteer> query = em.createQuery(
                    "SELECT v FROM Volunteer v WHERE " +
                            "LOWER(CONCAT(v.firstName, ' ', v.lastName)) LIKE LOWER(:name)", Volunteer.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Volunteer> findActiveVolunteers() {
        return findByStatus(VolunteerStatus.ACTIVE);
    }

    @Override
    public long countByStatus(VolunteerStatus status) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(v) FROM Volunteer v WHERE v.status = :status", Long.class);
            query.setParameter("status", status);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}