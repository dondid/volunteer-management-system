package ro.ucv.inf.soa.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import ro.ucv.inf.soa.dao.SkillDAO;
import ro.ucv.inf.soa.dao.SkillDAOImpl;
import ro.ucv.inf.soa.model.Skill;

import java.util.List;

@WebService(serviceName = "SkillService")
public class SkillSoapService {

    private final SkillDAO skillDAO = new SkillDAOImpl();

    @WebMethod
    public List<Skill> getAllSkills() {
        return skillDAO.findAll();
    }

    @WebMethod
    public Skill getSkillById(Long id) {
        return skillDAO.findById(id).orElse(null);
    }

    @WebMethod
    public Skill addSkill(Skill skill) {
        return skillDAO.save(skill);
    }

    @WebMethod
    public Skill updateSkill(Skill skill) {
        Skill existing = skillDAO.findById(skill.getId()).orElse(null);
        if (existing == null) {
            throw new RuntimeException("Skill not found with ID: " + skill.getId());
        }

        if (skill.getName() != null)
            existing.setName(skill.getName());
        if (skill.getDescription() != null)
            existing.setDescription(skill.getDescription());

        return skillDAO.update(existing);
    }

    @WebMethod
    public void deleteSkill(Long id) {
        skillDAO.deleteById(id);
    }
}
