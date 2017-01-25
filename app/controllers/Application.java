package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.libs.Json;
import play.mvc.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import views.html.*;
import java.util.HashMap;
import java.util.UUID;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

public class Application extends Controller {

    static HashMap<String, String> map = new HashMap<String,String>();
    
    public Result index() {
        return ok(index.render("RESTful URL Shortener"));
    }

    public Result list() {
        return ok(Json.toJson(map.values()));
    }

    public Result shorten() throws java.io.IOException {

        JsonNode jsonInput = request().body().asJson();

        //Récupérer l'url passé en paramètre
        if (jsonInput == null || ! jsonInput.has("url")) {
            return badRequest("Entrée invalide! Merci de spécifier l'url à minifier");
        }

        //Récupérer l'url passé en paramètre
        String completeUrl = jsonInput.findPath("url").asText();

        // Ajouter le préfixe http:// s'il n'existe pas
        if (completeUrl.startsWith("http://") && ! completeUrl.startsWith("https://")) {
            completeUrl = "http://" + completeUrl;
        }

        String shortUrlKey;
        //chercher si l'URL a été déjà minifiée ou nn
        if (map.containsValue(completeUrl)) {
            shortUrlKey = getKeyFromValue(map,completeUrl);
        }else {
            //Obtenir un hash unique
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            UUID uuid = UUID.randomUUID();
            dos.writeLong(uuid.getMostSignificantBits());
            String encoded = new String(Base64.encodeBase64(baos.toByteArray()), "ISO-8859-1");

            //Ajuster la taille du hash à 8 caractères
            shortUrlKey = StringUtils.left(encoded, 8); // returns the leftmost 6 characters

            //Associer l'url minifié à l'url d'origine
            map.put(shortUrlKey, completeUrl);
        }
        //Retourner l'url minifié
        ObjectMapper mapper = new ObjectMapper();
        JsonNode resultShortUrl = mapper.readTree("{\"url\":\""+shortUrlKey+"\"}");
        return ok(resultShortUrl);
    }

    public Result expend(String url) throws java.io.IOException {
        if (!map.containsKey(url)) {
            return forbidden("Url invalide");
        }

        String completeUrl = map.get(url);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode resultCompleteUrl = mapper.readTree("{\"url\":\""+completeUrl+"\"}");
        return ok(resultCompleteUrl);
    }



    public static String getKeyFromValue(HashMap<String,String> hm, String value) {
        for (String o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }
}
