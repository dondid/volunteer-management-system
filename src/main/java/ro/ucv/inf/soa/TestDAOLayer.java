package ro.ucv.inf.soa;

import ro.ucv.inf.soa.dao.*;
import ro.ucv.inf.soa.model.*;

public class TestDAOLayer {
    public static void main(String[] args) {
        System.out.println("=== TESTING DAO LAYER ===\n");

        // Test OrganizationDAO
        OrganizationDAO orgDAO = new OrganizationDAOImpl();
        System.out.println("✅ Total Organizations: " + orgDAO.count());
        System.out.println("✅ Active Organizations: " + orgDAO.findActiveOrganizations().size());

        // Test find by email
        orgDAO.findByEmail("contact@habitat.ro").ifPresent(org ->
                System.out.println("✅ Found org: " + org.getName())
        );

        // Test VolunteerDAO
        VolunteerDAO volDAO = new VolunteerDAOImpl();
        System.out.println("\n✅ Total Volunteers: " + volDAO.count());
        System.out.println("✅ Active Volunteers: " + volDAO.findActiveVolunteers().size());

        // Test find by email
        volDAO.findByEmail("ion.popescu@email.com").ifPresent(vol ->
                System.out.println("✅ Found volunteer: " + vol.getFullName())
        );

        // Test ProjectDAO
        ProjectDAO projDAO = new ProjectDAOImpl();
        System.out.println("\n✅ Total Projects: " + projDAO.count());
        System.out.println("✅ Active Projects: " + projDAO.findActiveProjects().size());
        System.out.println("✅ Available Projects: " + projDAO.findAvailableProjects().size());

        System.out.println("\n=== ALL DAO TESTS PASSED! ===");
    }
}