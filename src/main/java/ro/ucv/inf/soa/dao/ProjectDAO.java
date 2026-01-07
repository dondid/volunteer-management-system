package ro.ucv.inf.soa.dao;

import ro.ucv.inf.soa.model.Project;
import ro.ucv.inf.soa.model.ProjectStatus;

import java.util.List;

public interface ProjectDAO extends GenericDAO<Project, Long> {

    List<Project> findByOrganizationId(Long organizationId);
    List<Project> findByStatus(ProjectStatus status);
    List<Project> findActiveProjects();
    List<Project> findAvailableProjects();
    long countByStatus(ProjectStatus status);
}