package ro.ucv.inf.soa.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ro.ucv.inf.soa.model.Project;
import ro.ucv.inf.soa.model.ProjectStatus;

import java.util.List;

public class ProjectDAOImpl extends GenericDAOImpl<Project, Long> implements ProjectDAO {

    public ProjectDAOImpl() {
        super(Project.class);
    }

    @Override
    public List<Project> findByOrganizationId(Long organizationId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Project> query = em.createQuery(
                    "SELECT p FROM Project p WHERE p.organization.id = :orgId", Project.class);
            query.setParameter("orgId", organizationId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Project> findByStatus(ProjectStatus status) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Project> query = em.createQuery(
                    "SELECT p FROM Project p WHERE p.status = :status", Project.class);
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Project> findActiveProjects() {
        return findByStatus(ProjectStatus.ACTIVE);
    }

    @Override
    public List<Project> findAvailableProjects() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Project> query = em.createQuery(
                    "SELECT p FROM Project p WHERE p.currentVolunteers < p.maxVolunteers " +
                            "AND p.status = :status", Project.class);
            query.setParameter("status", ProjectStatus.ACTIVE);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long countByStatus(ProjectStatus status) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(p) FROM Project p WHERE p.status = :status", Long.class);
            query.setParameter("status", status);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}