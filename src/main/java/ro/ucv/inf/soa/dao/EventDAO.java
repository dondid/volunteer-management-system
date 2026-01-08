package ro.ucv.inf.soa.dao;

import ro.ucv.inf.soa.model.Event;
import ro.ucv.inf.soa.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface EventDAO extends GenericDAO<Event, Long> {
    List<Event> findByProjectId(Long projectId);
    List<Event> findByStatus(EventStatus status);
    List<Event> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<Event> findUpcomingEvents();
    List<Event> findAvailableEvents();
}
