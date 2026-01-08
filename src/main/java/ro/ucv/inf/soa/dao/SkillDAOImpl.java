package ro.ucv.inf.soa.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import ro.ucv.inf.soa.model.Skill;
import ro.ucv.inf.soa.model.SkillCategory;

import java.util.List;
import java.util.Optional;

public class SkillDAOImpl extends GenericDAOImpl<Skill, Long> implements SkillDAO {

    public SkillDAOImpl() {
        super(Skill.class);
    }

    @Override
    public Optional<Skill> findByName(String name) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Skill> query = em.createQuery(
                    "SELECT s FROM Skill s WHERE LOWER(s.name) = LOWER(:name)", Skill.class);
            query.setParameter("name", name);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Skill> findByCategory(SkillCategory category) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Skill> query = em.createQuery(
                    "SELECT s FROM Skill s WHERE s.category = :category", Skill.class);
            query.setParameter("category", category);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Skill> findByNameContaining(String name) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Skill> query = em.createQuery(
                    "SELECT s FROM Skill s WHERE LOWER(s.name) LIKE LOWER(:name)", Skill.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
