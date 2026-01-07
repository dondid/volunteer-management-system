package ro.ucv.inf.soa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import ro.ucv.inf.soa.model.*;

import java.util.List;

public class TestConnection {
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            System.out.println("=== TESTING JPA CONNECTION ===\n");
            System.out.println("Connecting to database...");

            emf = Persistence.createEntityManagerFactory("VolunteerPU");
            em = emf.createEntityManager();

            System.out.println("✅ Connection successful!\n");

            // Test Organizations
            System.out.println("--- Testing Organizations ---");
            List<Organization> orgs = em.createQuery("SELECT o FROM Organization o", Organization.class)
                    .getResultList();
            System.out.println("Found " + orgs.size() + " organizations");
            orgs.forEach(System.out::println);

            System.out.println("\n--- Testing Volunteers ---");
            List<Volunteer> volunteers = em.createQuery("SELECT v FROM Volunteer v", Volunteer.class)
                    .getResultList();
            System.out.println("Found " + volunteers.size() + " volunteers");
            volunteers.forEach(System.out::println);

            System.out.println("\n--- Testing Projects ---");
            List<Project> projects = em.createQuery("SELECT p FROM Project p", Project.class)
                    .getResultList();
            System.out.println("Found " + projects.size() + " projects");
            projects.forEach(System.out::println);

            System.out.println("\n--- Testing Skills ---");
            List<Skill> skills = em.createQuery("SELECT s FROM Skill s", Skill.class)
                    .getResultList();
            System.out.println("Found " + skills.size() + " skills");

            System.out.println("\n=== ALL TESTS PASSED! ===");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
        }
    }
}
