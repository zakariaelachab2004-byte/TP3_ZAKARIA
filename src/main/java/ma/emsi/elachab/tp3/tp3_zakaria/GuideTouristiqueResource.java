
package ma.emsi.elachab.tp3.tp3_zakaria;

import ma.emsi.elachab.tp3.tp3_zakaria.llm.GuideTouristique;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/guide")
public class GuideTouristiqueResource {

    // Injection du service IA avec CDI
    @Inject
    private GuideTouristique guideTouristique;

    /**
     * Retourne un guide touristique au format JSON
     * pour une ville ou un pays.
     *
     * @param lieu nom de la ville ou du pays
     * @param nb nombre d'endroits a visiter
     * @return reponse HTTP contenant le guide JSON
     */
    @GET
    @Path("/lieu/{ville_ou_pays}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response endroitsDeVisite(
            @PathParam("ville_ou_pays") String lieu,
            @QueryParam("nb")
            @DefaultValue("2") int nb) {

        // Verification du nombre d'endroits
        if (nb < 1 || nb > 20) {
            return Response.status(
                            Response.Status.BAD_REQUEST)
                    .entity(
                            "Le nombre doit etre entre 1 et 20"
                    )
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        try {
            // Creation de la demande utilisateur
            String demande = String.format(
                    "Donne un guide touristique de %s " +
                            "avec exactement %d endroits a visiter.",
                    lieu, nb
            );

            // Appel du service injecte par CDI
            String resultat =
                    guideTouristique.getGuide(demande);

            // Retour de la reponse JSON
            return Response.ok(
                            resultat,
                            MediaType.APPLICATION_JSON
                    )
                    .build();

        } catch (Exception e) {

            e.printStackTrace();

            return Response.status(
                            Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(
                            "Erreur lors de la generation du guide"
                    )
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }
}
