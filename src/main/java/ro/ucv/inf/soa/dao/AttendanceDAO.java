package ro.ucv.inf.soa.dao;

import ro.ucv.inf.soa.model.Attendance;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceDAO extends GenericDAO<Attendance, Long> {
    List<Attendance> findByAssignmentId(Long assignmentId);

    List<Attendance> findByDateRange(LocalDate startDate, LocalDate endDate);

    List<Attendance> findByVolunteerId(Long volunteerId);

    java.math.BigDecimal calculateTotalHoursForVolunteer(Long volunteerId);

    java.math.BigDecimal calculateTotalHoursForVolunteerAndProject(Long volunteerId, Long projectId);
}
