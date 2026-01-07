package ro.ucv.inf.soa.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_skills",
        uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "skill_id"}))
public class ProjectSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(name = "required_level", nullable = false)
    private ProficiencyLevel requiredLevel = ProficiencyLevel.BEGINNER;

    @Column(name = "is_mandatory", nullable = false)
    private Boolean isMandatory = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ProjectSkill() {
    }

    public ProjectSkill(Project project, Skill skill, ProficiencyLevel requiredLevel, Boolean isMandatory) {
        this.project = project;
        this.skill = skill;
        this.requiredLevel = requiredLevel;
        this.isMandatory = isMandatory;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public ProficiencyLevel getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(ProficiencyLevel requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public Boolean getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "ProjectSkill{" +
                "project=" + (project != null ? project.getId() : null) +
                ", skill=" + (skill != null ? skill.getName() : null) +
                ", requiredLevel=" + requiredLevel +
                ", mandatory=" + isMandatory +
                '}';
    }
}