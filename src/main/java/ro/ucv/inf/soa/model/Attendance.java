package ro.ucv.inf.soa.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "hours_worked", nullable = false, precision = 4, scale = 2)
    private BigDecimal hoursWorked = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "verified_by", length = 100)
    private String verifiedBy;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Attendance() {
    }

    public Attendance(Assignment assignment, LocalDate date, BigDecimal hoursWorked) {
        this.assignment = assignment;
        this.date = date;
        this.hoursWorked = hoursWorked;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (date == null) {
            date = LocalDate.now();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(BigDecimal hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void verify(String verifier) {
        this.verifiedBy = verifier;
        this.verifiedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", date=" + date +
                ", hours=" + hoursWorked +
                ", verified=" + (verifiedAt != null) +
                '}';
    }
}