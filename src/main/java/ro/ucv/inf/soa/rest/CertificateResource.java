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
public class CertificateResource {

    private final CertificateDAO certificateDAO = new CertificateDAOImpl();

    @GET
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
            if (certificate.getTotalHours() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Total hours is required"))
                        .build();
            }

            if (certificateDAO.findByCertificateNumber(certificate.getCertificateNumber()).isPresent()) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(ApiResponse.error("Certificate with this number already exists"))
                        .build();
            }

            Certificate saved = certificateDAO.save(certificate);
            return Response.status(Response.Status.CREATED)
                    .entity(ApiResponse.success("Certificate created successfully", saved))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error creating certificate: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateCertificate(@PathParam("id") Long id, Certificate certificate) {
        try {
            if (!certificateDAO.existsById(id)) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Certificate not found with id: " + id))
                        .build();
            }

            certificate.setId(id);
            Certificate updated = certificateDAO.update(certificate);
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
