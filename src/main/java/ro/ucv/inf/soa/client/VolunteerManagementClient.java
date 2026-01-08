package ro.ucv.inf.soa.client;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ro.ucv.inf.soa.model.Organization;
import ro.ucv.inf.soa.model.Volunteer;

/**
 * Client JAX-RS pentru consumarea API-ului Volunteer Management System
 * Demonstrează conectarea la serviciul web REST
 */
public class VolunteerManagementClient {

    private static final String BASE_URL = "http://localhost:8080/volunteer-management-system/api";
    private final Client client;
    private final WebTarget baseTarget;

    public VolunteerManagementClient() {
        this.client = ClientBuilder.newClient();
        this.baseTarget = client.target(BASE_URL);
    }

    /**
     * Testează conectarea la API
     */
    public void testConnection() {
        System.out.println("=== Testing Connection to API ===");
        try {
            Response response = baseTarget.path("/organizations")
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                System.out.println("✅ Connection successful! Status: " + response.getStatus());
                System.out.println("Response: " + response.readEntity(String.class));
            } else {
                System.out.println("❌ Connection failed! Status: " + response.getStatus());
            }
        } catch (Exception e) {
            System.out.println("❌ Error connecting to API: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obține toate organizațiile
     */
    public void getAllOrganizations() {
        System.out.println("\n=== Getting All Organizations ===");
        try {
            Response response = baseTarget.path("/organizations")
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                String result = response.readEntity(String.class);
                System.out.println("✅ Organizations retrieved:");
                System.out.println(result);
            } else {
                System.out.println("❌ Failed to retrieve organizations. Status: " + response.getStatus());
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obține o organizație după ID
     */
    public void getOrganizationById(Long id) {
        System.out.println("\n=== Getting Organization by ID: " + id + " ===");
        try {
            Response response = baseTarget.path("/organizations/" + id)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                String result = response.readEntity(String.class);
                System.out.println("✅ Organization retrieved:");
                System.out.println(result);
            } else {
                System.out.println("❌ Failed. Status: " + response.getStatus());
                System.out.println("Response: " + response.readEntity(String.class));
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creează o organizație nouă
     */
    public void createOrganization(String name, String email, String phone) {
        System.out.println("\n=== Creating Organization ===");
        try {
            Organization org = new Organization();
            org.setName(name);
            org.setEmail(email);
            org.setPhone(phone);
            org.setDescription("Created via REST Client");

            Response response = baseTarget.path("/organizations")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(org, MediaType.APPLICATION_JSON));
            
            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                String result = response.readEntity(String.class);
                System.out.println("✅ Organization created:");
                System.out.println(result);
            } else {
                System.out.println("❌ Failed to create organization. Status: " + response.getStatus());
                System.out.println("Response: " + response.readEntity(String.class));
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obține toți voluntarii
     */
    public void getAllVolunteers() {
        System.out.println("\n=== Getting All Volunteers ===");
        try {
            Response response = baseTarget.path("/volunteers")
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                String result = response.readEntity(String.class);
                System.out.println("✅ Volunteers retrieved:");
                System.out.println(result);
            } else {
                System.out.println("❌ Failed to retrieve volunteers. Status: " + response.getStatus());
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creează un voluntar nou
     */
    public void createVolunteer(String firstName, String lastName, String email) {
        System.out.println("\n=== Creating Volunteer ===");
        try {
            Volunteer volunteer = new Volunteer();
            volunteer.setFirstName(firstName);
            volunteer.setLastName(lastName);
            volunteer.setEmail(email);

            Response response = baseTarget.path("/volunteers")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(volunteer, MediaType.APPLICATION_JSON));
            
            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                String result = response.readEntity(String.class);
                System.out.println("✅ Volunteer created:");
                System.out.println(result);
            } else {
                System.out.println("❌ Failed to create volunteer. Status: " + response.getStatus());
                System.out.println("Response: " + response.readEntity(String.class));
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obține statistici
     */
    public void getStatistics() {
        System.out.println("\n=== Getting Statistics ===");
        try {
            Response response = baseTarget.path("/statistics/overview")
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                String result = response.readEntity(String.class);
                System.out.println("✅ Statistics retrieved:");
                System.out.println(result);
            } else {
                System.out.println("❌ Failed. Status: " + response.getStatus());
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obține proiecte disponibile
     */
    public void getAvailableProjects() {
        System.out.println("\n=== Getting Available Projects ===");
        try {
            Response response = baseTarget.path("/projects")
                    .queryParam("available", true)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                String result = response.readEntity(String.class);
                System.out.println("✅ Available projects:");
                System.out.println(result);
            } else {
                System.out.println("❌ Failed. Status: " + response.getStatus());
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obține voluntari cu paginare
     */
    public void getVolunteersPaginated(int page, int size) {
        System.out.println("\n=== Getting Volunteers (Page " + page + ", Size " + size + ") ===");
        try {
            Response response = baseTarget.path("/statistics/volunteers-paginated")
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .queryParam("sortBy", "lastName")
                    .queryParam("order", "ASC")
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                String result = response.readEntity(String.class);
                System.out.println("✅ Paginated volunteers:");
                System.out.println(result);
            } else {
                System.out.println("❌ Failed. Status: " + response.getStatus());
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obține ore lucrate per voluntar
     */
    public void getHoursPerVolunteer() {
        System.out.println("\n=== Getting Hours Per Volunteer ===");
        try {
            Response response = baseTarget.path("/statistics/hours-per-volunteer")
                    .queryParam("limit", 10)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                String result = response.readEntity(String.class);
                System.out.println("✅ Hours per volunteer:");
                System.out.println(result);
            } else {
                System.out.println("❌ Failed. Status: " + response.getStatus());
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Închide clientul
     */
    public void close() {
        if (client != null) {
            client.close();
        }
    }

    /**
     * Metodă main pentru testare
     */
    public static void main(String[] args) {
        VolunteerManagementClient client = new VolunteerManagementClient();
        
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║   Volunteer Management System - REST API Client         ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        
        // Test conexiune
        client.testConnection();
        
        // Obține organizații
        client.getAllOrganizations();
        
        // Obține voluntari
        client.getAllVolunteers();
        
        // Obține statistici
        client.getStatistics();
        
        // Obține proiecte disponibile
        client.getAvailableProjects();
        
        // Obține voluntari cu paginare
        client.getVolunteersPaginated(0, 5);
        
        // Obține ore per voluntar
        client.getHoursPerVolunteer();
        
        // Test creare (comentat pentru a nu crea duplicate)
        // client.createOrganization("Test Org", "test@example.com", "123456789");
        // client.createVolunteer("Test", "User", "testuser@example.com");
        
        client.close();
        System.out.println("\n✅ Client test completed!");
    }
}
