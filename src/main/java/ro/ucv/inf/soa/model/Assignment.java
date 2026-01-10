package ro.ucv.inf.soa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@Entity
@Table(name = "assignments", uniqueConstraints = @UniqueConstraint(columnNames = { "volunteer_id", "project_id" }))
@XmlAccessorType(XmlAccessType.FIELD)
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(ro.ucv.inf.soa.ws.adapter.LocalDateAdapter.class)
    @Column(name = "assignment_date", nullable = false)
    private LocalDate assignmentDate;

    @Column(length = 100)
    private String role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AssignmentStatus status = AssignmentStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(ro.ucv.inf.soa.ws.adapter.LocalDateTimeAdapter.class)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(ro.ucv.inf.soa.ws.adapter.LocalDateTimeAdapter.class)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relații
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @JsonIgnore
    @jakarta.xml.bind.annotation.XmlTransient
    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attendance> attendances;

    @JsonIgnore
    @jakarta.xml.bind.annotation.XmlTransient
    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Feedback> feedbacks;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public Assignment() {
    }

    public Assignment(Volunteer volunteer, Project project, LocalDate assignmentDate) {
        this.volunteer = volunteer;
        this.project = project;
        this.assignmentDate = assignmentDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAssignmentDate() {
        return assignmentDate;
    }

    public void setAssignmentDate(LocalDate assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public AssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(AssignmentStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", assignmentDate=" + assignmentDate +
                ", role='" + role + '\'' +
                ", status=" + status +
                '}';
    }
}