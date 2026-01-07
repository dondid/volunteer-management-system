package ro.ucv.inf.soa.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_participants",
        uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "volunteer_id"}))
public class EventParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status", nullable = false)
    private AttendanceStatus attendanceStatus = AttendanceStatus.REGISTERED;

    @Column(columnDefinition = "TEXT")
    private String notes;

    public EventParticipant() {
    }

    public EventParticipant(Event event, Volunteer volunteer) {
        this.event = event;
        this.volunteer = volunteer;
        this.registrationDate = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (registrationDate == null) {
            registrationDate = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "EventParticipant{" +
                "event=" + (event != null ? event.getTitle() : null) +
                ", volunteer=" + (volunteer != null ? volunteer.getFullName() : null) +
                ", status=" + attendanceStatus +
                '}';
    }
}