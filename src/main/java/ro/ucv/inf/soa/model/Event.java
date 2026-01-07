package ro.ucv.inf.soa.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(length = 300)
    private String location;

    @Column(name = "max_participants")
    private Integer maxParticipants = 50;

    @Column(name = "current_participants")
    private Integer currentParticipants = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status = EventStatus.SCHEDULED;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventParticipant> participants = new ArrayList<>();

    public Event() {
    }

    public Event(Project project, String title, LocalDateTime eventDate) {
        this.project = project;
        this.title = title;
        this.eventDate = eventDate;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Integer getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(Integer currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<EventParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<EventParticipant> participants) {
        this.participants = participants;
    }

    public void addParticipant(EventParticipant participant) {
        participants.add(participant);
        participant.setEvent(this);
        currentParticipants++;
    }

    public void removeParticipant(EventParticipant participant) {
        participants.remove(participant);
        participant.setEvent(null);
        currentParticipants--;
    }

    public boolean isFull() {
        return currentParticipants >= maxParticipants;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date=" + eventDate +
                ", status=" + status +
                '}';
    }
}