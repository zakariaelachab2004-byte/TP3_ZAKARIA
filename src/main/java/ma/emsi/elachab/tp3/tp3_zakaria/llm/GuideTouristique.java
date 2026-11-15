package ma.emsi.elachab.tp3.tp3_zakaria.llm;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface GuideTouristique {

    @SystemMessage({
            "N'utilise pas Markdown, réponds uniquement avec du JSON pur.",
            "Tu es un guide touristique expert. Pour la ville ou le pays passé en paramètre, " +
                    "indique les 2 principaux endroits à visiter et le prix moyen d'un repas dans la devise du pays.",
            "Tu dois respecter strictement ce format JSON :",
            "{\n" +
                    "  \"ville_ou_pays\": \"nom de la ville ou du pays\",\n" +
                    "  \"endroits_a_visiter\": [\"endroit 1\", \"endroit 2\"],\n" +
                    "  \"prix_moyen_repas\": \"<prix> <devise du pays>\"\n" +
                    "}"
    })
    String getGuide(@UserMessage String lieu);
}