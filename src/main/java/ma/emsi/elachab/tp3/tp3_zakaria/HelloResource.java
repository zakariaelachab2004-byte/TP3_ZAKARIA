
package ma.emsi.elachab.tp3.tp3_zakaria;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/hello-world")
public class HelloResource {

    // Test JAX-RS : Hello Mohamed
    @GET
    @Path("/personnes/{nom}")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@PathParam("nom") String nom) {
        return "Hello, " + nom;
    }

    // Test JAX-RS : tableau de String en JSON
    @GET
    @Path("/tableau/{nom}")
    @Produces(MediaType.APPLICATION_JSON)
    public String[] testerTableau(
            @PathParam("nom") String nom) {

        return new String[]{nom};
    }

    // Test JAX-RS : type Response
    @GET
    @Path("/response/{nom}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response testerResponse(
            @PathParam("nom") String nom) {

        return Response.ok("Hello, " + nom).build();
    }

    // Test JAX-RS : erreur HTTP 500
    @GET
    @Path("/erreur")
    @Produces(MediaType.TEXT_PLAIN)
    public Response testerErreur() {

        return Response.status(
                Response.Status.INTERNAL_SERVER_ERROR
        ).entity("Internal Server Error").build();
    }
}
