package ro.ucv.inf.soa.dao;

import ro.ucv.inf.soa.model.Assignment;
import ro.ucv.inf.soa.model.AssignmentStatus;

import java.util.List;

public interface AssignmentDAO extends GenericDAO<Assignment, Long> {
    List<Assignment> findByVolunteerId(Long volunteerId);
    List<Assignment> findByProjectId(Long projectId);
    List<Assignment> findByStatus(AssignmentStatus status);
    List<Assignment> findByVolunteerAndProject(Long volunteerId, Long projectId);
}
