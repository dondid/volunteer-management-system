package ro.ucv.inf.soa.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import ro.ucv.inf.soa.dao.OrganizationDAO;
import ro.ucv.inf.soa.dao.OrganizationDAOImpl;
import ro.ucv.inf.soa.model.Organization;

import java.util.List;

@WebService(serviceName = "OrganizationService")
public class OrganizationSoapService {

    private final OrganizationDAO organizationDAO = new OrganizationDAOImpl();

    @WebMethod
    public List<Organization> getAllOrganizations() {
        return organizationDAO.findAll();
    }

    @WebMethod
    public Organization getOrganizationById(Long id) {
        return organizationDAO.findById(id).orElse(null);
    }

    @WebMethod
    public Organization addOrganization(Organization organization) {
        return organizationDAO.save(organization);
    }

    @WebMethod
    public Organization updateOrganization(Organization organization) {
        Organization existing = organizationDAO.findById(organization.getId()).orElse(null);
        if (existing == null) {
            return null;
        }
        if (organization.getName() != null)
            existing.setName(organization.getName());
        if (organization.getDescription() != null)
            existing.setDescription(organization.getDescription());
        if (organization.getEmail() != null)
            existing.setEmail(organization.getEmail());
        if (organization.getPhone() != null)
            existing.setPhone(organization.getPhone());
        if (organization.getAddress() != null)
            existing.setAddress(organization.getAddress());
        if (organization.getWebsite() != null)
            existing.setWebsite(organization.getWebsite());
        if (organization.getRegistrationNumber() != null)
            existing.setRegistrationNumber(organization.getRegistrationNumber());
        // Status typically handled via specific transition, but for CRUD:
        if (organization.getStatus() != null)
            existing.setStatus(organization.getStatus());

        return organizationDAO.update(existing);
    }

    @WebMethod
    public void deleteOrganization(Long id) {
        organizationDAO.deleteById(id);
    }
}
