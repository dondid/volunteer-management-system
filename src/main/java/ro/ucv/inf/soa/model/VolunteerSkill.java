package ro.ucv.inf.soa.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "volunteer_skills",
        uniqueConstraints = @UniqueConstraint(columnNames = {"volunteer_id", "skill_id"}))
public class VolunteerSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(name = "proficiency_level", nullable = false)
    private ProficiencyLevel proficiencyLevel = ProficiencyLevel.BEGINNER;

    @Column(name = "years_experience")
    private Integer yearsExperience = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public VolunteerSkill() {
    }

    public VolunteerSkill(Volunteer volunteer, Skill skill, ProficiencyLevel level) {
        this.volunteer = volunteer;
        this.skill = skill;
        this.proficiencyLevel = level;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
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

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public ProficiencyLevel getProficiencyLevel() {
        return proficiencyLevel;
    }

    public void setProficiencyLevel(ProficiencyLevel proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }

    public Integer getYearsExperience() {
        return yearsExperience;
    }

    public void setYearsExperience(Integer yearsExperience) {
        this.yearsExperience = yearsExperience;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "VolunteerSkill{" +
                "volunteer=" + (volunteer != null ? volunteer.getId() : null) +
                ", skill=" + (skill != null ? skill.getName() : null) +
                ", level=" + proficiencyLevel +
                '}';
    }
}