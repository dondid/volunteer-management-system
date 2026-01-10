package ro.ucv.inf.soa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@Entity
@Table(name = "projects")
@XmlAccessorType(XmlAccessType.FIELD)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(ro.ucv.inf.soa.ws.adapter.LocalDateAdapter.class)
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(ro.ucv.inf.soa.ws.adapter.LocalDateAdapter.class)
    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(length = 300)
    private String location;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String county;

    @Column(name = "max_volunteers")
    private Integer maxVolunteers;

    @org.hibernate.annotations.Formula("(SELECT COUNT(*) FROM assignments a WHERE a.project_id = id)")
    private Integer currentVolunteers;

    @Column(length = 500)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProjectStatus status = ProjectStatus.DRAFT;

    @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(ro.ucv.inf.soa.ws.adapter.LocalDateTimeAdapter.class)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(ro.ucv.inf.soa.ws.adapter.LocalDateTimeAdapter.class)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relații
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @JsonIgnore
    @jakarta.xml.bind.annotation.XmlTransient
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Assignment> assignments;

    @JsonIgnore
    @jakarta.xml.bind.annotation.XmlTransient
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Event> events;

    @JsonIgnore
    @jakarta.xml.bind.annotation.XmlTransient
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectSkill> requiredSkills;

    @JsonIgnore
    @jakarta.xml.bind.annotation.XmlTransient
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Certificate> certificates;

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
    public Project() {
    }

    public Project(String title, LocalDate startDate, Organization organization) {
        this.title = title;
        this.startDate = startDate;
        this.organization = organization;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public Integer getMaxVolunteers() {
        return maxVolunteers;
    }

    public void setMaxVolunteers(Integer maxVolunteers) {
        this.maxVolunteers = maxVolunteers;
    }

    public Integer getCurrentVolunteers() {
        return currentVolunteers;
    }

    public void setCurrentVolunteers(Integer currentVolunteers) {
        this.currentVolunteers = currentVolunteers;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
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

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<ProjectSkill> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<ProjectSkill> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", startDate=" + startDate +
                ", status=" + status +
                '}';
    }
}