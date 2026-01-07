package ro.ucv.inf.soa.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import ro.ucv.inf.soa.model.Organization;
import ro.ucv.inf.soa.model.OrganizationStatus;

import java.util.List;
import java.util.Optional;

public class OrganizationDAOImpl extends GenericDAOImpl<Organization, Long> implements OrganizationDAO {

    public OrganizationDAOImpl() {
        super(Organization.class);
    }

    @Override
    public Optional<Organization> findByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Organization> query = em.createQuery(
                    "SELECT o FROM Organization o WHERE o.email = :email", Organization.class);
            query.setParameter("email", email);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Organization> findByStatus(OrganizationStatus status) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Organization> query = em.createQuery(
                    "SELECT o FROM Organization o WHERE o.status = :status", Organization.class);
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Organization> findByNameContaining(String name) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Organization> query = em.createQuery(
                    "SELECT o FROM Organization o WHERE LOWER(o.name) LIKE LOWER(:name)", Organization.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Organization> findActiveOrganizations() {
        return findByStatus(OrganizationStatus.ACTIVE);
    }

    @Override
    public long countByStatus(OrganizationStatus status) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(o) FROM Organization o WHERE o.status = :status", Long.class);
            query.setParameter("status", status);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}
