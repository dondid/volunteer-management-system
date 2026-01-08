package ro.ucv.inf.soa.dao;

import ro.ucv.inf.soa.model.Skill;
import ro.ucv.inf.soa.model.SkillCategory;

import java.util.List;
import java.util.Optional;

public interface SkillDAO extends GenericDAO<Skill, Long> {
    Optional<Skill> findByName(String name);
    List<Skill> findByCategory(SkillCategory category);
    List<Skill> findByNameContaining(String name);
}

