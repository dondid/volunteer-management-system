package ro.ucv.inf.soa.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import ro.ucv.inf.soa.dao.ProjectDAO;
import ro.ucv.inf.soa.dao.ProjectDAOImpl;
import ro.ucv.inf.soa.model.Project;

import java.util.List;

@WebService(serviceName = "ProjectService")
public class ProjectSoapService {

    private final ProjectDAO projectDAO = new ProjectDAOImpl();

    @WebMethod
    public List<Project> getAllProjects() {
        return projectDAO.findAll();
    }

    @WebMethod
    public Project getProjectById(Long id) {
        return projectDAO.findById(id).orElse(null);
    }

    @WebMethod
    public Project addProject(Project project) {
        ro.ucv.inf.soa.model.Organization realOrg = null;
        if (project.getOrganization() != null && project.getOrganization().getId() != null) {
            ro.ucv.inf.soa.dao.OrganizationDAO orgDAO = new ro.ucv.inf.soa.dao.OrganizationDAOImpl();
            realOrg = orgDAO.findById(project.getOrganization().getId()).orElse(null);
            if (realOrg != null) {
                project.setOrganization(realOrg);
            }
        }
        Project savedProject = projectDAO.save(project);

        // Ensure we return the real Organization entity, not a Hibernate proxy, to
        // avoid JAX-B errors
        if (realOrg != null) {
            savedProject.setOrganization(realOrg);
        }

        return savedProject;
    }

    @WebMethod
    public Project updateProject(Project project) {
        Project existing = projectDAO.findById(project.getId()).orElse(null);
        if (existing == null) {
            throw new RuntimeException("Project not found with ID: " + project.getId());
        }

        // Smart Copy: Only non-null fields
        if (project.getTitle() != null)
            existing.setTitle(project.getTitle());
        if (project.getDescription() != null)
            existing.setDescription(project.getDescription());
        if (project.getStartDate() != null)
            existing.setStartDate(project.getStartDate());
        if (project.getEndDate() != null)
            existing.setEndDate(project.getEndDate());
        if (project.getLocation() != null)
            existing.setLocation(project.getLocation());
        if (project.getCity() != null)
            existing.setCity(project.getCity());
        if (project.getCounty() != null)
            existing.setCounty(project.getCounty());
        if (project.getMaxVolunteers() != null)
            existing.setMaxVolunteers(project.getMaxVolunteers());
        if (project.getImageUrl() != null)
            existing.setImageUrl(project.getImageUrl());
        if (project.getStatus() != null)
            existing.setStatus(project.getStatus());

        // Handle Organization update
        if (project.getOrganization() != null && project.getOrganization().getId() != null) {
            ro.ucv.inf.soa.dao.OrganizationDAO orgDAO = new ro.ucv.inf.soa.dao.OrganizationDAOImpl();
            ro.ucv.inf.soa.model.Organization realOrg = orgDAO.findById(project.getOrganization().getId()).orElse(null);
            if (realOrg != null) {
                existing.setOrganization(realOrg);
            }
        }

        return projectDAO.update(existing);
    }

    @WebMethod
    public void deleteProject(Long id) {
        projectDAO.deleteById(id);
    }
}
