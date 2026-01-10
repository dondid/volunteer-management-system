package ro.ucv.inf.soa.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import ro.ucv.inf.soa.dao.AssignmentDAO;
import ro.ucv.inf.soa.dao.AssignmentDAOImpl;
import ro.ucv.inf.soa.model.Assignment;

import java.util.List;

@WebService(serviceName = "AssignmentService")
public class AssignmentSoapService {

    private final AssignmentDAO assignmentDAO = new AssignmentDAOImpl();

    @WebMethod
    public List<Assignment> getAllAssignments() {
        return assignmentDAO.findAll();
    }

    @WebMethod
    public Assignment getAssignmentById(Long id) {
        return assignmentDAO.findById(id).orElse(null);
    }

    @WebMethod
    public Assignment addAssignment(Assignment assignment) {
        if (assignment.getVolunteer() != null && assignment.getVolunteer().getId() != null) {
            ro.ucv.inf.soa.dao.VolunteerDAO volDAO = new ro.ucv.inf.soa.dao.VolunteerDAOImpl();
            ro.ucv.inf.soa.model.Volunteer vol = volDAO.findById(assignment.getVolunteer().getId()).orElse(null);
            if (vol != null) {
                assignment.setVolunteer(vol);
            }
        }
        if (assignment.getProject() != null && assignment.getProject().getId() != null) {
            ro.ucv.inf.soa.dao.ProjectDAO projDAO = new ro.ucv.inf.soa.dao.ProjectDAOImpl();
            ro.ucv.inf.soa.model.Project proj = projDAO.findById(assignment.getProject().getId()).orElse(null);
            if (proj != null) {
                assignment.setProject(proj);
            }
        }
        return assignmentDAO.save(assignment);
    }

    @WebMethod
    public Assignment updateAssignment(Assignment assignment) {
        Assignment existing = assignmentDAO.findById(assignment.getId()).orElse(null);
        if (existing == null) {
            throw new RuntimeException("Assignment not found with ID: " + assignment.getId());
        }

        if (assignment.getRole() != null)
            existing.setRole(assignment.getRole());
        if (assignment.getAssignmentDate() != null)
            existing.setAssignmentDate(assignment.getAssignmentDate());
        if (assignment.getStatus() != null)
            existing.setStatus(assignment.getStatus());

        if (assignment.getVolunteer() != null && assignment.getVolunteer().getId() != null) {
            ro.ucv.inf.soa.dao.VolunteerDAO volDAO = new ro.ucv.inf.soa.dao.VolunteerDAOImpl();
            ro.ucv.inf.soa.model.Volunteer vol = volDAO.findById(assignment.getVolunteer().getId()).orElse(null);
            if (vol != null) {
                existing.setVolunteer(vol);
            }
        }
        if (assignment.getProject() != null && assignment.getProject().getId() != null) {
            ro.ucv.inf.soa.dao.ProjectDAO projDAO = new ro.ucv.inf.soa.dao.ProjectDAOImpl();
            ro.ucv.inf.soa.model.Project proj = projDAO.findById(assignment.getProject().getId()).orElse(null);
            if (proj != null) {
                existing.setProject(proj);
            }
        }

        return assignmentDAO.update(existing);
    }

    @WebMethod
    public void deleteAssignment(Long id) {
        assignmentDAO.deleteById(id);
    }
}
