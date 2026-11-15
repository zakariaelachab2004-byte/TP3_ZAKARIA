
package ma.emsi.elachab.tp3.tp3_zakaria;

import ma.emsi.elachab.tp3.tp3_zakaria.llm.GuideTouristique;

import dev.langchain4j.model.google.genai.GoogleGenAiChatModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.service.AiServices;

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

    private volatile GuideTouristique guide;

    // Initialisation du modèle Gemini
    private GuideTouristique getService() {

        if (guide == null) {
            synchronized (this) {
                if (guide == null) {

                    String apiKey =
                            System.getenv("GEMINI_KEY");

                    if (apiKey == null || apiKey.isBlank()) {
                        throw new IllegalStateException(
                                "Variable GEMINI_KEY introuvable"
                        );
                    }

                    GoogleGenAiChatModel model =
                            GoogleGenAiChatModel.builder()
                                    .apiKey(apiKey)
                                    .modelName("gemini-3.8-flash")
                                    .responseFormat(ResponseFormat.JSON)
                                    .build();

                    guide = AiServices.create(
                            GuideTouristique.class,
                            model
                    );
                }
            }
        }

        return guide;
    }

    /**
     * Génère un guide touristique en JSON.
     *
     * @param lieu ville ou pays demandé
     * @param nb nombre d'endroits à visiter
     * @return réponse HTTP contenant le guide
     */
    @GET
    @Path("/lieu/{ville_ou_pays}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response endroitsDeVisite(
            @PathParam("ville_ou_pays") String lieu,
            @QueryParam("nb")
            @DefaultValue("2") int nb) {

        // Vérification du nombre demandé
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
            // Construction de la demande
            String demande = String.format(
                    "Donne un guide touristique de %s " +
                            "avec exactement %d endroits a visiter.",
                    lieu, nb
            );

            // Appel de Gemini via LangChain4j
            String resultat =
                    getService().getGuide(demande);

            // Retour de la réponse JSON
            return Response.ok(
                    resultat,
                    MediaType.APPLICATION_JSON
            ).build();

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
