package ro.ucv.inf.soa.dao;

import ro.ucv.inf.soa.model.Volunteer;
import ro.ucv.inf.soa.model.VolunteerStatus;

import java.util.List;
import java.util.Optional;

public interface VolunteerDAO extends GenericDAO<Volunteer, Long> {

    Optional<Volunteer> findByEmail(String email);
    Optional<Volunteer> findByCnp(String cnp);
    List<Volunteer> findByStatus(VolunteerStatus status);
    List<Volunteer> findByCity(String city);
    List<Volunteer> findByNameContaining(String name);
    List<Volunteer> findActiveVolunteers();
    long countByStatus(VolunteerStatus status);
}