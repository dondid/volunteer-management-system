package ro.ucv.inf.soa.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import ro.ucv.inf.soa.model.Certificate;

import java.util.List;
import java.util.Optional;

public class CertificateDAOImpl extends GenericDAOImpl<Certificate, Long> implements CertificateDAO {

    public CertificateDAOImpl() {
        super(Certificate.class);
    }

    @Override
    public List<Certificate> findByVolunteerId(Long volunteerId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Certificate> query = em.createQuery(
                    "SELECT c FROM Certificate c WHERE c.volunteer.id = :volunteerId", Certificate.class);
            query.setParameter("volunteerId", volunteerId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Certificate> findByProjectId(Long projectId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Certificate> query = em.createQuery(
                    "SELECT c FROM Certificate c WHERE c.project.id = :projectId", Certificate.class);
            query.setParameter("projectId", projectId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Certificate> findByCertificateNumber(String certificateNumber) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Certificate> query = em.createQuery(
                    "SELECT c FROM Certificate c WHERE c.certificateNumber = :certNumber", Certificate.class);
            query.setParameter("certNumber", certificateNumber);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
}
