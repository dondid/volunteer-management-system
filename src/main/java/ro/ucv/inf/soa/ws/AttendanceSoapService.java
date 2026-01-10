package ro.ucv.inf.soa.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import ro.ucv.inf.soa.dao.AttendanceDAO;
import ro.ucv.inf.soa.dao.AttendanceDAOImpl;
import ro.ucv.inf.soa.model.Attendance;

import java.util.List;

@WebService(serviceName = "AttendanceService")
public class AttendanceSoapService {

    private final AttendanceDAO attendanceDAO = new AttendanceDAOImpl();

    @WebMethod
    public List<Attendance> getAllAttendance() {
        return attendanceDAO.findAll();
    }

    @WebMethod
    public Attendance getAttendanceById(Long id) {
        return attendanceDAO.findById(id).orElse(null);
    }

    @WebMethod
    public Attendance addAttendance(Attendance attendance) {
        if (attendance.getAssignment() != null && attendance.getAssignment().getId() != null) {
            ro.ucv.inf.soa.dao.AssignmentDAO assignDAO = new ro.ucv.inf.soa.dao.AssignmentDAOImpl();
            ro.ucv.inf.soa.model.Assignment assign = assignDAO.findById(attendance.getAssignment().getId())
                    .orElse(null);
            if (assign != null) {
                attendance.setAssignment(assign);
            }
        }
        return attendanceDAO.save(attendance);
    }

    @WebMethod
    public Attendance updateAttendance(Attendance attendance) {
        Attendance existing = attendanceDAO.findById(attendance.getId()).orElse(null);
        if (existing == null) {
            throw new RuntimeException("Attendance not found with ID: " + attendance.getId());
        }

        if (attendance.getHoursWorked() != null)
            existing.setHoursWorked(attendance.getHoursWorked());

        if (attendance.getAssignment() != null && attendance.getAssignment().getId() != null) {
            ro.ucv.inf.soa.dao.AssignmentDAO assignDAO = new ro.ucv.inf.soa.dao.AssignmentDAOImpl();
            ro.ucv.inf.soa.model.Assignment assign = assignDAO.findById(attendance.getAssignment().getId())
                    .orElse(null);
            if (assign != null) {
                existing.setAssignment(assign);
            }
        }

        return attendanceDAO.update(existing);
    }

    @WebMethod
    public void deleteAttendance(Long id) {
        attendanceDAO.deleteById(id);
    }
}
