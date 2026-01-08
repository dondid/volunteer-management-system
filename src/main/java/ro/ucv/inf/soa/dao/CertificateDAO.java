package ro.ucv.inf.soa.dao;

import ro.ucv.inf.soa.model.Certificate;

import java.util.List;
import java.util.Optional;

public interface CertificateDAO extends GenericDAO<Certificate, Long> {
    List<Certificate> findByVolunteerId(Long volunteerId);
    List<Certificate> findByProjectId(Long projectId);
    Optional<Certificate> findByCertificateNumber(String certificateNumber);
}
