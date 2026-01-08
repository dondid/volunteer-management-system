package ro.ucv.inf.soa.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ro.ucv.inf.soa.dao.AttendanceDAO;
import ro.ucv.inf.soa.dao.AttendanceDAOImpl;
import ro.ucv.inf.soa.dto.ApiResponse;
import ro.ucv.inf.soa.model.Attendance;

import java.util.List;

@Path("/attendance")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AttendanceResource {

    private final AttendanceDAO attendanceDAO = new AttendanceDAOImpl();
    private final ro.ucv.inf.soa.dao.AssignmentDAO assignmentDAO = new ro.ucv.inf.soa.dao.AssignmentDAOImpl();

    @GET
    public Response getAllAttendance(@QueryParam("assignmentId") Long assignmentId,
            @QueryParam("volunteerId") Long volunteerId) {
        try {
            List<Attendance> attendances;
            if (assignmentId != null) {
                attendances = attendanceDAO.findByAssignmentId(assignmentId);
            } else if (volunteerId != null) {
                attendances = attendanceDAO.findByVolunteerId(volunteerId);
            } else {
                attendances = attendanceDAO.findAll();
            }
            return Response.ok(ApiResponse.success(attendances)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error retrieving attendance: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getAttendanceById(@PathParam("id") Long id) {
        try {
            return attendanceDAO.findById(id)
                    .map(attendance -> Response.ok(ApiResponse.success(attendance)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(ApiResponse.error("Attendance not found with id: " + id))
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        }
    }

    @POST
    public Response createAttendance(Attendance attendance) {
        try {
            if (attendance.getAssignment() == null || attendance.getAssignment().getId() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Assignment is required"))
                        .build();
            }
            if (attendance.getDate() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Date is required"))
                        .build();
            }
            if (attendance.getHoursWorked() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Hours worked is required"))
                        .build();
            }

            ro.ucv.inf.soa.model.Assignment assignment = assignmentDAO.findById(attendance.getAssignment().getId())
                    .orElse(null);
            if (assignment == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse
                                .error("Assignment not found with id: " + attendance.getAssignment().getId()))
                        .build();
            }
            attendance.setAssignment(assignment);

            Attendance saved = attendanceDAO.save(attendance);

            // Reload to ensure full initialization (avoid proxy errors)
            Attendance fullAttendance = attendanceDAO.findById(saved.getId()).orElse(saved);

            return Response.status(Response.Status.CREATED)
                    .entity(ApiResponse.success("Attendance recorded successfully", fullAttendance))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error creating attendance: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateAttendance(@PathParam("id") Long id, Attendance attendance) {
        try {
            Attendance existing = attendanceDAO.findById(id).orElse(null);

            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Attendance not found with id: " + id))
                        .build();
            }

            // Update scalar fields
            if (attendance.getDate() != null)
                existing.setDate(attendance.getDate());
            if (attendance.getHoursWorked() != null)
                existing.setHoursWorked(attendance.getHoursWorked());
            if (attendance.getNotes() != null)
                existing.setNotes(attendance.getNotes());
            if (attendance.getVerifiedBy() != null)
                existing.setVerifiedBy(attendance.getVerifiedBy());
            // verifiedAt might be handled by logic, but allowing update if provided
            if (attendance.getVerifiedAt() != null)
                existing.setVerifiedAt(attendance.getVerifiedAt());

            Attendance updated = attendanceDAO.update(existing);
            return Response.ok(ApiResponse.success("Attendance updated successfully", updated))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error updating attendance: " + e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAttendance(@PathParam("id") Long id) {
        try {
            if (!attendanceDAO.existsById(id)) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Attendance not found with id: " + id))
                        .build();
            }

            attendanceDAO.deleteById(id);
            return Response.ok(ApiResponse.success("Attendance deleted successfully", null))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error deleting attendance: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/assignment/{assignmentId}")
    public Response getAttendanceByAssignment(@PathParam("assignmentId") Long assignmentId) {
        try {
            List<Attendance> attendances = attendanceDAO.findByAssignmentId(assignmentId);
            return Response.ok(ApiResponse.success(attendances)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        }
    }
}
