package ro.ucv.inf.soa.dao;

import ro.ucv.inf.soa.model.Organization;
import ro.ucv.inf.soa.model.OrganizationStatus;

import java.util.List;
import java.util.Optional;

public interface OrganizationDAO extends GenericDAO<Organization, Long> {

    Optional<Organization> findByEmail(String email);
    List<Organization> findByStatus(OrganizationStatus status);
    List<Organization> findByNameContaining(String name);
    List<Organization> findActiveOrganizations();
    long countByStatus(OrganizationStatus status);
}