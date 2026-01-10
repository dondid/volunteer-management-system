package ro.ucv.inf.soa.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import ro.ucv.inf.soa.dao.CertificateDAO;
import ro.ucv.inf.soa.dao.CertificateDAOImpl;
import ro.ucv.inf.soa.model.Certificate;

import java.util.List;

@WebService(serviceName = "CertificateService")
public class CertificateSoapService {

    private final CertificateDAO certificateDAO = new CertificateDAOImpl();

    @WebMethod
    public List<Certificate> getAllCertificates() {
        return certificateDAO.findAll();
    }

    @WebMethod
    public Certificate getCertificateById(Long id) {
        return certificateDAO.findById(id).orElse(null);
    }

    @WebMethod
    public Certificate addCertificate(Certificate certificate) {
        if (certificate.getVolunteer() != null && certificate.getVolunteer().getId() != null) {
            ro.ucv.inf.soa.dao.VolunteerDAO volDAO = new ro.ucv.inf.soa.dao.VolunteerDAOImpl();
            ro.ucv.inf.soa.model.Volunteer vol = volDAO.findById(certificate.getVolunteer().getId()).orElse(null);
            if (vol != null) {
                certificate.setVolunteer(vol);
            }
        }
        if (certificate.getProject() != null && certificate.getProject().getId() != null) {
            ro.ucv.inf.soa.dao.ProjectDAO projDAO = new ro.ucv.inf.soa.dao.ProjectDAOImpl();
            ro.ucv.inf.soa.model.Project proj = projDAO.findById(certificate.getProject().getId()).orElse(null);
            if (proj != null) {
                certificate.setProject(proj);
            }
        }
        return certificateDAO.save(certificate);
    }

    @WebMethod
    public Certificate updateCertificate(Certificate certificate) {
        Certificate existing = certificateDAO.findById(certificate.getId()).orElse(null);
        if (existing == null) {
            throw new RuntimeException("Certificate not found with ID: " + certificate.getId());
        }

        if (certificate.getCertificateNumber() != null)
            existing.setCertificateNumber(certificate.getCertificateNumber());
        if (certificate.getDescription() != null)
            existing.setDescription(certificate.getDescription());
        if (certificate.getIssueDate() != null)
            existing.setIssueDate(certificate.getIssueDate());
        if (certificate.getTotalHours() != null)
            existing.setTotalHours(certificate.getTotalHours());
        if (certificate.getSignedBy() != null)
            existing.setSignedBy(certificate.getSignedBy());
        if (certificate.getFileUrl() != null)
            existing.setFileUrl(certificate.getFileUrl());

        if (certificate.getVolunteer() != null && certificate.getVolunteer().getId() != null) {
            ro.ucv.inf.soa.dao.VolunteerDAO volDAO = new ro.ucv.inf.soa.dao.VolunteerDAOImpl();
            ro.ucv.inf.soa.model.Volunteer vol = volDAO.findById(certificate.getVolunteer().getId()).orElse(null);
            if (vol != null) {
                existing.setVolunteer(vol);
            }
        }
        if (certificate.getProject() != null && certificate.getProject().getId() != null) {
            ro.ucv.inf.soa.dao.ProjectDAO projDAO = new ro.ucv.inf.soa.dao.ProjectDAOImpl();
            ro.ucv.inf.soa.model.Project proj = projDAO.findById(certificate.getProject().getId()).orElse(null);
            if (proj != null) {
                existing.setProject(proj);
            }
        }

        return certificateDAO.update(existing);
    }

    @WebMethod
    public void deleteCertificate(Long id) {
        certificateDAO.deleteById(id);
    }
}
