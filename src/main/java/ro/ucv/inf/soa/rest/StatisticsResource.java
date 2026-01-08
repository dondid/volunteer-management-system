package ro.ucv.inf.soa.rest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ro.ucv.inf.soa.dto.ApiResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/statistics")
@Produces(MediaType.APPLICATION_JSON)
public class StatisticsResource {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("VolunteerPU");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Statistici generale - COUNT pentru toate entitățile principale
     */
    @GET
    @Path("/overview")
    public Response getOverview() {
        EntityManager em = getEntityManager();
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // COUNT queries pentru fiecare tabel
            stats.put("totalOrganizations", em.createQuery("SELECT COUNT(o) FROM Organization o", Long.class).getSingleResult());
            stats.put("totalVolunteers", em.createQuery("SELECT COUNT(v) FROM Volunteer v", Long.class).getSingleResult());
            stats.put("totalProjects", em.createQuery("SELECT COUNT(p) FROM Project p", Long.class).getSingleResult());
            stats.put("totalEvents", em.createQuery("SELECT COUNT(e) FROM Event e", Long.class).getSingleResult());
            stats.put("totalAssignments", em.createQuery("SELECT COUNT(a) FROM Assignment a", Long.class).getSingleResult());
            stats.put("totalSkills", em.createQuery("SELECT COUNT(s) FROM Skill s", Long.class).getSingleResult());
            stats.put("totalCertificates", em.createQuery("SELECT COUNT(c) FROM Certificate c", Long.class).getSingleResult());
            stats.put("totalFeedback", em.createQuery("SELECT COUNT(f) FROM Feedback f", Long.class).getSingleResult());
            
            return Response.ok(ApiResponse.success(stats)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error retrieving statistics: " + e.getMessage()))
                    .build();
        } finally {
            em.close();
        }
    }

    /**
     * JOIN + COUNT - Număr de voluntari per organizație
     */
    @GET
    @Path("/volunteers-per-organization")
    public Response getVolunteersPerOrganization() {
        EntityManager em = getEntityManager();
        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createQuery(
                    "SELECT o.name, COUNT(DISTINCT a.volunteer.id) " +
                            "FROM Organization o " +
                            "LEFT JOIN Project p ON p.organization.id = o.id " +
                            "LEFT JOIN Assignment a ON a.project.id = p.id " +
                            "GROUP BY o.id, o.name " +
                            "ORDER BY COUNT(DISTINCT a.volunteer.id) DESC"
            ).getResultList();
            
            List<Map<String, Object>> stats = results.stream()
                    .map(row -> {
                        Map<String, Object> stat = new HashMap<>();
                        stat.put("organizationName", row[0]);
                        stat.put("volunteerCount", row[1]);
                        return stat;
                    })
                    .toList();
            
            return Response.ok(ApiResponse.success(stats)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        } finally {
            em.close();
        }
    }

    /**
     * JOIN + SUM - Total ore lucrate per voluntar
     */
    @GET
    @Path("/hours-per-volunteer")
    public Response getHoursPerVolunteer(@QueryParam("limit") @DefaultValue("10") Integer limit) {
        EntityManager em = getEntityManager();
        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createQuery(
                    "SELECT v.firstName, v.lastName, COALESCE(SUM(a.hoursWorked), 0) " +
                            "FROM Volunteer v " +
                            "LEFT JOIN Assignment ass ON ass.volunteer.id = v.id " +
                            "LEFT JOIN Attendance a ON a.assignment.id = ass.id " +
                            "GROUP BY v.id, v.firstName, v.lastName " +
                            "ORDER BY SUM(a.hoursWorked) DESC"
            ).setMaxResults(limit)
            .getResultList();
            
            List<Map<String, Object>> stats = results.stream()
                    .map(row -> {
                        Map<String, Object> stat = new HashMap<>();
                        stat.put("volunteerName", row[0] + " " + row[1]);
                        stat.put("totalHours", row[2]);
                        return stat;
                    })
                    .toList();
            
            return Response.ok(ApiResponse.success(stats)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        } finally {
            em.close();
        }
    }

    /**
     * JOIN + AVG - Rating mediu per proiect
     */
    @GET
    @Path("/average-rating-per-project")
    public Response getAverageRatingPerProject() {
        EntityManager em = getEntityManager();
        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createQuery(
                    "SELECT p.title, AVG(f.rating), COUNT(f.id) " +
                            "FROM Project p " +
                            "LEFT JOIN Assignment a ON a.project.id = p.id " +
                            "LEFT JOIN Feedback f ON f.assignment.id = a.id " +
                            "GROUP BY p.id, p.title " +
                            "HAVING COUNT(f.id) > 0 " +
                            "ORDER BY AVG(f.rating) DESC"
            ).getResultList();
            
            List<Map<String, Object>> stats = results.stream()
                    .map(row -> {
                        Map<String, Object> stat = new HashMap<>();
                        stat.put("projectTitle", row[0]);
                        stat.put("averageRating", row[1]);
                        stat.put("feedbackCount", row[2]);
                        return stat;
                    })
                    .toList();
            
            return Response.ok(ApiResponse.success(stats)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        } finally {
            em.close();
        }
    }

    /**
     * JOIN complex - Proiecte cu cei mai mulți voluntari
     */
    @GET
    @Path("/projects-by-volunteer-count")
    public Response getProjectsByVolunteerCount(@QueryParam("limit") @DefaultValue("10") Integer limit) {
        EntityManager em = getEntityManager();
        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createQuery(
                    "SELECT p.title, o.name, COUNT(DISTINCT a.volunteer.id), p.maxVolunteers " +
                            "FROM Project p " +
                            "JOIN p.organization o " +
                            "LEFT JOIN Assignment a ON a.project.id = p.id AND a.status = 'ACCEPTED' " +
                            "GROUP BY p.id, p.title, o.name, p.maxVolunteers " +
                            "ORDER BY COUNT(DISTINCT a.volunteer.id) DESC"
            ).setMaxResults(limit)
            .getResultList();
            
            List<Map<String, Object>> stats = results.stream()
                    .map(row -> {
                        Map<String, Object> stat = new HashMap<>();
                        stat.put("projectTitle", row[0]);
                        stat.put("organizationName", row[1]);
                        stat.put("currentVolunteers", row[2]);
                        stat.put("maxVolunteers", row[3]);
                        return stat;
                    })
                    .toList();
            
            return Response.ok(ApiResponse.success(stats)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        } finally {
            em.close();
        }
    }

    /**
     * JOIN + GROUP BY - Competențe cele mai cerute în proiecte
     */
    @GET
    @Path("/most-requested-skills")
    public Response getMostRequestedSkills(@QueryParam("limit") @DefaultValue("10") Integer limit) {
        EntityManager em = getEntityManager();
        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createQuery(
                    "SELECT s.name, s.category, COUNT(ps.project.id) " +
                            "FROM Skill s " +
                            "LEFT JOIN ProjectSkill ps ON ps.skill.id = s.id " +
                            "GROUP BY s.id, s.name, s.category " +
                            "ORDER BY COUNT(ps.project.id) DESC"
            ).setMaxResults(limit)
            .getResultList();
            
            List<Map<String, Object>> stats = results.stream()
                    .map(row -> {
                        Map<String, Object> stat = new HashMap<>();
                        stat.put("skillName", row[0]);
                        stat.put("category", row[1]);
                        stat.put("projectCount", row[2]);
                        return stat;
                    })
                    .toList();
            
            return Response.ok(ApiResponse.success(stats)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        } finally {
            em.close();
        }
    }

    /**
     * Paginare și sortare - Voluntari cu paginare
     */
    @GET
    @Path("/volunteers-paginated")
    public Response getVolunteersPaginated(
            @QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("size") @DefaultValue("10") Integer size,
            @QueryParam("sortBy") @DefaultValue("lastName") String sortBy,
            @QueryParam("order") @DefaultValue("ASC") String order) {
        EntityManager em = getEntityManager();
        try {
            String orderDirection = order.toUpperCase().equals("DESC") ? "DESC" : "ASC";
            String sortField = sortBy.equals("firstName") ? "v.firstName" : 
                              sortBy.equals("email") ? "v.email" : "v.lastName";
            
            Query countQuery = em.createQuery("SELECT COUNT(v) FROM Volunteer v");
            Long totalCount = (Long) countQuery.getSingleResult();
            
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createQuery(
                    "SELECT v.id, v.firstName, v.lastName, v.email, v.status, COUNT(DISTINCT a.id) " +
                            "FROM Volunteer v " +
                            "LEFT JOIN Assignment a ON a.volunteer.id = v.id " +
                            "GROUP BY v.id, v.firstName, v.lastName, v.email, v.status " +
                            "ORDER BY " + sortField + " " + orderDirection
            )
            .setFirstResult(page * size)
            .setMaxResults(size)
            .getResultList();
            
            List<Map<String, Object>> volunteers = results.stream()
                    .map(row -> {
                        Map<String, Object> vol = new HashMap<>();
                        vol.put("id", row[0]);
                        vol.put("firstName", row[1]);
                        vol.put("lastName", row[2]);
                        vol.put("email", row[3]);
                        vol.put("status", row[4]);
                        vol.put("assignmentCount", row[5]);
                        return vol;
                    })
                    .toList();
            
            Map<String, Object> response = new HashMap<>();
            response.put("volunteers", volunteers);
            response.put("totalCount", totalCount);
            response.put("page", page);
            response.put("size", size);
            response.put("totalPages", (int) Math.ceil((double) totalCount / size));
            
            return Response.ok(ApiResponse.success(response)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        } finally {
            em.close();
        }
    }

    /**
     * Filtrare complexă - Proiecte active cu locuri disponibile și filtrare după organizație
     */
    @GET
    @Path("/available-projects-filtered")
    public Response getAvailableProjectsFiltered(
            @QueryParam("organizationId") Long organizationId,
            @QueryParam("minSlots") Integer minSlots) {
        EntityManager em = getEntityManager();
        try {
            StringBuilder queryBuilder = new StringBuilder(
                    "SELECT p.id, p.title, o.name, p.currentVolunteers, p.maxVolunteers, " +
                    "(p.maxVolunteers - p.currentVolunteers) as availableSlots " +
                    "FROM Project p " +
                    "JOIN p.organization o " +
                    "WHERE p.status = 'ACTIVE' " +
                    "AND p.currentVolunteers < p.maxVolunteers "
            );
            
            if (organizationId != null) {
                queryBuilder.append("AND o.id = :orgId ");
            }
            
            queryBuilder.append("ORDER BY (p.maxVolunteers - p.currentVolunteers) DESC");
            
            Query query = em.createQuery(queryBuilder.toString());
            if (organizationId != null) {
                query.setParameter("orgId", organizationId);
            }
            
            @SuppressWarnings("unchecked")
            List<Object[]> results = query.getResultList();
            
            List<Map<String, Object>> projects = results.stream()
                    .map(row -> {
                        Map<String, Object> proj = new HashMap<>();
                        proj.put("id", row[0]);
                        proj.put("title", row[1]);
                        proj.put("organizationName", row[2]);
                        proj.put("currentVolunteers", row[3]);
                        proj.put("maxVolunteers", row[4]);
                        proj.put("availableSlots", row[5]);
                        return proj;
                    })
                    .filter(proj -> minSlots == null || ((Number) proj.get("availableSlots")).intValue() >= minSlots)
                    .toList();
            
            return Response.ok(ApiResponse.success(projects)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        } finally {
            em.close();
        }
    }
}
