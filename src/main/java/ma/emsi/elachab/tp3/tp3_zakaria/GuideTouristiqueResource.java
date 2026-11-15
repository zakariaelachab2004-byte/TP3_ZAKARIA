
package ma.emsi.elachab.tp3.tp3_zakaria;

import ma.emsi.elachab.tp3.tp3_zakaria.llm.GuideTouristique;

import dev.langchain4j.model.google.genai.GoogleGenAiChatModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.service.AiServices;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/guide")
public class GuideTouristiqueResource {

    private volatile GuideTouristique guide;

    // Initialisation du modele Gemini
    private GuideTouristique getService() {

        if (guide == null) {
            synchronized (this) {
                if (guide == null) {

                    // Recuperation de la cle API Windows
                    String apiKey =
                            System.getenv("GEMINI_KEY");

                    if (apiKey == null || apiKey.isBlank()) {
                        throw new IllegalStateException(
                                "Variable GEMINI_KEY introuvable"
                        );
                    }

                    // Configuration de Gemini
                    GoogleGenAiChatModel model =
                            GoogleGenAiChatModel.builder()
                                    .apiKey(apiKey)
                                    .modelName("gemini-3.8-flash")
                                    .responseFormat(ResponseFormat.JSON)
                                    .build();

                    // Creation du service LangChain4j
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
     * Retourne les informations touristiques
     * d'une ville ou d'un pays au format JSON.
     *
     * @param lieu ville ou pays recherche
     * @return reponse HTTP contenant le guide
     */
    @GET
    @Path("/lieu/{ville_ou_pays}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response endroitsDeVisite(
            @PathParam("ville_ou_pays") String lieu) {

        try {

            // Appel du LLM
            String resultat = getService().getGuide(lieu);

            // Retour du JSON au client
            return Response.ok(
                    resultat,
                    MediaType.APPLICATION_JSON
            ).build();

        } catch (Exception e) {

            e.printStackTrace();

            return Response.status(
                            Response.Status.INTERNAL_SERVER_ERROR
                    )
                    .entity("Erreur lors de la generation du guide")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }
}
