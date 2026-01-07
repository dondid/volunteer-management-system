package ro.ucv.inf.soa.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "volunteers")
public class Volunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(unique = true, length = 13)
    private String cnp;

    @Column(length = 300)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String county;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VolunteerStatus status = VolunteerStatus.ACTIVE;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "volunteer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VolunteerSkill> volunteerSkills = new ArrayList<>();

    @OneToMany(mappedBy = "volunteer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assignment> assignments = new ArrayList<>();

    @OneToMany(mappedBy = "volunteer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certificate> certificates = new ArrayList<>();

    @OneToMany(mappedBy = "volunteer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventParticipant> eventParticipations = new ArrayList<>();

    public Volunteer() {
    }

    public Volunteer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registrationDate = LocalDate.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (registrationDate == null) {
            registrationDate = LocalDate.now();
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public VolunteerStatus getStatus() {
        return status;
    }

    public void setStatus(VolunteerStatus status) {
        this.status = status;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<VolunteerSkill> getVolunteerSkills() {
        return volunteerSkills;
    }

    public void setVolunteerSkills(List<VolunteerSkill> volunteerSkills) {
        this.volunteerSkills = volunteerSkills;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    public List<EventParticipant> getEventParticipations() {
        return eventParticipations;
    }

    public void setEventParticipations(List<EventParticipant> eventParticipations) {
        this.eventParticipations = eventParticipations;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "Volunteer{" +
                "id=" + id +
                ", name='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                '}';
    }
}