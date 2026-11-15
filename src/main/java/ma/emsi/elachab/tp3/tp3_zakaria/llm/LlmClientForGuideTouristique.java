
package ma.emsi.elachab.tp3.tp3_zakaria.llm;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.google.genai.GoogleGenAiChatModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.service.AiServices;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class LlmClientForGuideTouristique {

    /**
     * Cree et fournit le service de guide touristique
     * utilise par la ressource REST.
     *
     * @return une implementation LangChain4j
     *         de GuideTouristique
     */
    @Produces
    @ApplicationScoped
    public GuideTouristique creerGuideTouristique() {

        // Recuperation de la cle API Gemini
        String apiKey = System.getenv("GEMINI_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Variable GEMINI_KEY introuvable"
            );
        }

        // Configuration du modele Gemini
        ChatModel model = GoogleGenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-3.8-flash")
                .responseFormat(ResponseFormat.JSON)
                .build();

        // Creation du service IA avec LangChain4j
        return AiServices.create(
                GuideTouristique.class,
                model
        );
    }
}
