package ro.ucv.inf.soa.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ro.ucv.inf.soa.dao.CertificateDAO;
import ro.ucv.inf.soa.dao.CertificateDAOImpl;
import ro.ucv.inf.soa.dto.ApiResponse;
import ro.ucv.inf.soa.model.Certificate;

import java.util.List;

@Path("/certificates")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@io.swagger.v3.oas.annotations.tags.Tag(name = "Certificates", description = "Operations for managing volunteer certificates")
public class CertificateResource {

    private final CertificateDAO certificateDAO = new CertificateDAOImpl();
    private final ro.ucv.inf.soa.dao.VolunteerDAO volunteerDAO = new ro.ucv.inf.soa.dao.VolunteerDAOImpl();
    private final ro.ucv.inf.soa.dao.ProjectDAO projectDAO = new ro.ucv.inf.soa.dao.ProjectDAOImpl();
    private final ro.ucv.inf.soa.dao.AttendanceDAO attendanceDAO = new ro.ucv.inf.soa.dao.AttendanceDAOImpl();

    @GET
    @io.swagger.v3.oas.annotations.Operation(summary = "List certificates", description = "Retrieves certificates filtered by volunteer or project")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of certificates found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    public Response getAllCertificates(@QueryParam("volunteerId") Long volunteerId,
            @QueryParam("projectId") Long projectId) {
        try {
            List<Certificate> certificates;
            if (volunteerId != null) {
                certificates = certificateDAO.findByVolunteerId(volunteerId);
            } else if (projectId != null) {
                certificates = certificateDAO.findByProjectId(projectId);
            } else {
                certificates = certificateDAO.findAll();
            }
            return Response.ok(ApiResponse.success(certificates)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error retrieving certificates: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get certificate by ID", description = "Retrieves a specific certificate by its ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Certificate found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Certificate not found")
    public Response getCertificateById(@PathParam("id") Long id) {
        try {
            return certificateDAO.findById(id)
                    .map(cert -> Response.ok(ApiResponse.success(cert)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(ApiResponse.error("Certificate not found with id: " + id))
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        }
    }

    @POST
    @io.swagger.v3.oas.annotations.Operation(summary = "Issue certificate", description = "Creates a new certificate for a volunteer")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Certificate created successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or missing prerequisites")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Certificate number already exists")
    public Response createCertificate(Certificate certificate) {
        try {
            if (certificate.getVolunteer() == null || certificate.getVolunteer().getId() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Volunteer is required"))
                        .build();
            }
            if (certificate.getCertificateNumber() == null || certificate.getCertificateNumber().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Certificate number is required"))
                        .build();
            }
            if (certificate.getTotalHours() == null
                    || certificate.getTotalHours().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                // Auto-calculate hours if not provided or 0
                java.math.BigDecimal calculatedHours;
                if (certificate.getProject() != null && certificate.getProject().getId() != null) {
                    calculatedHours = attendanceDAO.calculateTotalHoursForVolunteerAndProject(
                            certificate.getVolunteer().getId(), certificate.getProject().getId());
                } else {
                    calculatedHours = attendanceDAO.calculateTotalHoursForVolunteer(certificate.getVolunteer().getId());
                }

                if (calculatedHours.compareTo(java.math.BigDecimal.ZERO) <= 0) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(ApiResponse
                                    .error("Total hours is required or could not be calculated from attendance"))
                            .build();
                }
                certificate.setTotalHours(calculatedHours);
            }

            if (certificateDAO.findByCertificateNumber(certificate.getCertificateNumber()).isPresent()) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(ApiResponse.error("Certificate with this number already exists"))
                        .build();
            }

            ro.ucv.inf.soa.model.Volunteer volunteer = volunteerDAO.findById(certificate.getVolunteer().getId())
                    .orElse(null);
            if (volunteer == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Volunteer not found with id: " + certificate.getVolunteer().getId()))
                        .build();
            }
            certificate.setVolunteer(volunteer);

            if (certificate.getProject() != null && certificate.getProject().getId() != null) {
                ro.ucv.inf.soa.model.Project project = projectDAO.findById(certificate.getProject().getId())
                        .orElse(null);
                if (project == null) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(ApiResponse.error("Project not found with id: " + certificate.getProject().getId()))
                            .build();
                }
                certificate.setProject(project);
            }

            Certificate saved = certificateDAO.save(certificate);

            // Reload to ensure full initialization (avoid proxy errors)
            Certificate fullCert = certificateDAO.findById(saved.getId()).orElse(saved);

            return Response.status(Response.Status.CREATED)
                    .entity(ApiResponse.success("Certificate created successfully", fullCert))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error creating certificate: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Update certificate", description = "Updates an existing certificate")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Certificate updated successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Certificate not found")
    public Response updateCertificate(@PathParam("id") Long id, Certificate certificate) {
        try {
            Certificate existing = certificateDAO.findById(id).orElse(null);

            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Certificate not found with id: " + id))
                        .build();
            }

            // Update scalar fields
            if (certificate.getCertificateNumber() != null)
                existing.setCertificateNumber(certificate.getCertificateNumber());
            if (certificate.getIssueDate() != null)
                existing.setIssueDate(certificate.getIssueDate());
            if (certificate.getTotalHours() != null) {
                if (certificate.getTotalHours().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(ApiResponse.error("Total hours must be positive"))
                            .build();
                }
                existing.setTotalHours(certificate.getTotalHours());
            }
            if (certificate.getDescription() != null)
                existing.setDescription(certificate.getDescription());
            if (certificate.getSignedBy() != null)
                existing.setSignedBy(certificate.getSignedBy());
            if (certificate.getFileUrl() != null)
                existing.setFileUrl(certificate.getFileUrl());

            Certificate updated = certificateDAO.update(existing);
            return Response.ok(ApiResponse.success("Certificate updated successfully", updated))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error updating certificate: " + e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Delete certificate", description = "Revokes and removes a certificate")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Certificate deleted successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Certificate not found")
    public Response deleteCertificate(@PathParam("id") Long id) {
        try {
            if (!certificateDAO.existsById(id)) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Certificate not found with id: " + id))
                        .build();
            }

            certificateDAO.deleteById(id);
            return Response.ok(ApiResponse.success("Certificate deleted successfully", null))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error deleting certificate: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/volunteer/{volunteerId}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get certificates by volunteer", description = "Retrieves all certificates issued to a specific volunteer")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of certificates found")
    public Response getCertificatesByVolunteer(@PathParam("volunteerId") Long volunteerId) {
        try {
            List<Certificate> certificates = certificateDAO.findByVolunteerId(volunteerId);
            return Response.ok(ApiResponse.success(certificates)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        }
    }
}
