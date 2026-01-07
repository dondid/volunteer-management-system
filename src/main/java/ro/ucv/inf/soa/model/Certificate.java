package ro.ucv.inf.soa.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "certificates")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "certificate_number", nullable = false, unique = true, length = 50)
    private String certificateNumber;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "total_hours", nullable = false, precision = 6, scale = 2)
    private BigDecimal totalHours;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "signed_by", length = 100)
    private String signedBy;

    @Column(name = "file_url", length = 500)
    private String fileUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Certificate() {
    }

    public Certificate(Volunteer volunteer, String certificateNumber, LocalDate issueDate, BigDecimal totalHours) {
        this.volunteer = volunteer;
        this.certificateNumber = certificateNumber;
        this.issueDate = issueDate;
        this.totalHours = totalHours;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (issueDate == null) {
            issueDate = LocalDate.now();
        }
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

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public BigDecimal getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(BigDecimal totalHours) {
        this.totalHours = totalHours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSignedBy() {
        return signedBy;
    }

    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "id=" + id +
                ", number='" + certificateNumber + '\'' +
                ", volunteer=" + (volunteer != null ? volunteer.getFullName() : null) +
                ", hours=" + totalHours +
                '}';
    }
}