package ro.ucv.inf.soa.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "assignment_date", nullable = false)
    private LocalDate assignmentDate;

    @Column(length = 100)
    private String role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentStatus status = AssignmentStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendances = new ArrayList<>();

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks = new ArrayList<>();

    public Assignment() {
    }

    public Assignment(Volunteer volunteer, Project project, LocalDate assignmentDate) {
        this.volunteer = volunteer;
        this.project = project;
        this.assignmentDate = assignmentDate;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (assignmentDate == null) {
            assignmentDate = LocalDate.now();
        }
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
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

    public void addAttendance(Attendance attendance) {
        attendances.add(attendance);
        attendance.setAssignment(this);
    }

    public void removeAttendance(Attendance attendance) {
        attendances.remove(attendance);
        attendance.setAssignment(null);
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", volunteer=" + (volunteer != null ? volunteer.getFullName() : null) +
                ", project=" + (project != null ? project.getTitle() : null) +
                ", status=" + status +
                '}';
    }
}