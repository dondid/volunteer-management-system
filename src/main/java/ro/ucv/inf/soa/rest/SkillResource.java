package ro.ucv.inf.soa.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ro.ucv.inf.soa.dao.SkillDAO;
import ro.ucv.inf.soa.dao.SkillDAOImpl;
import ro.ucv.inf.soa.dto.ApiResponse;
import ro.ucv.inf.soa.model.Skill;
import ro.ucv.inf.soa.model.SkillCategory;

import java.util.List;

@Path("/skills")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SkillResource {

    private final SkillDAO skillDAO = new SkillDAOImpl();

    @GET
    public Response getAllSkills(@QueryParam("category") String category,
            @QueryParam("name") String name) {
        try {
            List<Skill> skills;
            if (category != null) {
                skills = skillDAO.findByCategory(SkillCategory.valueOf(category.toUpperCase()));
            } else if (name != null) {
                skills = skillDAO.findByNameContaining(name);
            } else {
                skills = skillDAO.findAll();
            }
            return Response.ok(ApiResponse.success(skills)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error retrieving skills: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getSkillById(@PathParam("id") Long id) {
        try {
            return skillDAO.findById(id)
                    .map(skill -> Response.ok(ApiResponse.success(skill)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(ApiResponse.error("Skill not found with id: " + id))
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        }
    }

    @POST
    public Response createSkill(Skill skill) {
        try {
            if (skill.getName() == null || skill.getName().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Skill name is required"))
                        .build();
            }

            if (skillDAO.findByName(skill.getName()).isPresent()) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(ApiResponse.error("Skill with this name already exists"))
                        .build();
            }

            Skill saved = skillDAO.save(skill);
            return Response.status(Response.Status.CREATED)
                    .entity(ApiResponse.success("Skill created successfully", saved))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error creating skill: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateSkill(@PathParam("id") Long id, Skill skill) {
        try {
            Skill existing = skillDAO.findById(id).orElse(null);

            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Skill not found with id: " + id))
                        .build();
            }

            // Update scalar fields
            if (skill.getName() != null) {
                if (!skill.getName().equals(existing.getName()) && skillDAO.findByName(skill.getName()).isPresent()) {
                    return Response.status(Response.Status.CONFLICT)
                            .entity(ApiResponse.error("Skill with this name already exists"))
                            .build();
                }
                existing.setName(skill.getName());
            }
            if (skill.getDescription() != null)
                existing.setDescription(skill.getDescription());
            if (skill.getCategory() != null)
                existing.setCategory(skill.getCategory());

            Skill updated = skillDAO.update(existing);
            return Response.ok(ApiResponse.success("Skill updated successfully", updated))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error updating skill: " + e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSkill(@PathParam("id") Long id) {
        try {
            if (!skillDAO.existsById(id)) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Skill not found with id: " + id))
                        .build();
            }

            skillDAO.deleteById(id);
            return Response.ok(ApiResponse.success("Skill deleted successfully", null))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error deleting skill: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/category/{category}")
    public Response getSkillsByCategory(@PathParam("category") String category) {
        try {
            SkillCategory skillCategory = SkillCategory.valueOf(category.toUpperCase());
            List<Skill> skills = skillDAO.findByCategory(skillCategory);
            return Response.ok(ApiResponse.success(skills)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Invalid category: " + category))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error: " + e.getMessage()))
                    .build();
        }
    }
}
