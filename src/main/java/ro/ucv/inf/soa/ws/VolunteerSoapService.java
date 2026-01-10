package ro.ucv.inf.soa.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import ro.ucv.inf.soa.dao.VolunteerDAO;
import ro.ucv.inf.soa.dao.VolunteerDAOImpl;
import ro.ucv.inf.soa.model.Volunteer;

import java.util.List;

@WebService(serviceName = "VolunteerService")
public class VolunteerSoapService {

    private final VolunteerDAO volunteerDAO = new VolunteerDAOImpl();

    @WebMethod
    public List<Volunteer> getAllVolunteers() {
        return volunteerDAO.findAll();
    }

    @WebMethod
    public Volunteer getVolunteerById(Long id) {
        return volunteerDAO.findById(id).orElse(null);
    }

    @WebMethod
    public Volunteer addVolunteer(Volunteer volunteer) {
        return volunteerDAO.save(volunteer);
    }

    @WebMethod
    public Volunteer updateVolunteer(Volunteer volunteer) {
        Volunteer existing = volunteerDAO.findById(volunteer.getId()).orElse(null);
        if (existing == null) {
            return null; // or throw exception
        }
        if (volunteer.getFirstName() != null)
            existing.setFirstName(volunteer.getFirstName());
        if (volunteer.getLastName() != null)
            existing.setLastName(volunteer.getLastName());
        if (volunteer.getEmail() != null)
            existing.setEmail(volunteer.getEmail());
        if (volunteer.getPhone() != null)
            existing.setPhone(volunteer.getPhone());
        if (volunteer.getCnp() != null)
            existing.setCnp(volunteer.getCnp());
        if (volunteer.getAddress() != null)
            existing.setAddress(volunteer.getAddress());
        if (volunteer.getCity() != null)
            existing.setCity(volunteer.getCity());
        if (volunteer.getCounty() != null)
            existing.setCounty(volunteer.getCounty());
        if (volunteer.getBio() != null)
            existing.setBio(volunteer.getBio());
        return volunteerDAO.update(existing);
    }

    @WebMethod
    public void deleteVolunteer(Long id) {
        volunteerDAO.deleteById(id);
    }
}
