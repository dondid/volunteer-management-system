package ro.ucv.inf.soa.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import ro.ucv.inf.soa.dao.EventDAO;
import ro.ucv.inf.soa.dao.EventDAOImpl;
import ro.ucv.inf.soa.model.Event;

import java.util.List;

@WebService(serviceName = "EventService")
public class EventSoapService {

    private final EventDAO eventDAO = new EventDAOImpl();

    @WebMethod
    public List<Event> getAllEvents() {
        return eventDAO.findAll();
    }

    @WebMethod
    public Event getEventById(Long id) {
        return eventDAO.findById(id).orElse(null);
    }

    @WebMethod
    public Event addEvent(Event event) {
        if (event.getProject() != null && event.getProject().getId() != null) {
            ro.ucv.inf.soa.dao.ProjectDAO projectDAO = new ro.ucv.inf.soa.dao.ProjectDAOImpl();
            ro.ucv.inf.soa.model.Project proj = projectDAO.findById(event.getProject().getId()).orElse(null);
            if (proj != null) {
                event.setProject(proj);
            }
        }
        return eventDAO.save(event);
    }

    @WebMethod
    public Event updateEvent(Event event) {
        Event existing = eventDAO.findById(event.getId()).orElse(null);
        if (existing == null) {
            throw new RuntimeException("Event not found with ID: " + event.getId());
        }

        if (event.getTitle() != null)
            existing.setTitle(event.getTitle());
        if (event.getDescription() != null)
            existing.setDescription(event.getDescription());
        if (event.getEventDate() != null)
            existing.setEventDate(event.getEventDate());
        if (event.getLocation() != null)
            existing.setLocation(event.getLocation());
        if (event.getMaxParticipants() != null)
            existing.setMaxParticipants(event.getMaxParticipants());
        if (event.getStatus() != null)
            existing.setStatus(event.getStatus());

        if (event.getProject() != null && event.getProject().getId() != null) {
            ro.ucv.inf.soa.dao.ProjectDAO projectDAO = new ro.ucv.inf.soa.dao.ProjectDAOImpl();
            ro.ucv.inf.soa.model.Project proj = projectDAO.findById(event.getProject().getId()).orElse(null);
            if (proj != null) {
                existing.setProject(proj);
            }
        }

        return eventDAO.update(existing);
    }

    @WebMethod
    public void deleteEvent(Long id) {
        eventDAO.deleteById(id);
    }
}
