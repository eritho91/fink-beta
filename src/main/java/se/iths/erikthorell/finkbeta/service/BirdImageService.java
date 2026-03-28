package se.iths.erikthorell.finkbeta.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class BirdImageService {

    private final RestTemplate restTemplate = new RestTemplate();

    public BirdImageResult fetchBirdImage(String species) {
        try {
            String encodedSpecies = URLEncoder.encode(species, StandardCharsets.UTF_8);
            String url = "https://api.inaturalist.org/v1/taxa/autocomplete?q=" + encodedSpecies;

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null || !response.containsKey("results")) {
                return null;
            }

            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

            if (results.isEmpty()) {
                return null;
            }

            Map<String, Object> firstResult = results.get(0);

            if (!firstResult.containsKey("default_photo")) {
                return null;
            }

            Map<String, Object> defaultPhoto = (Map<String, Object>) firstResult.get("default_photo");

            String imageUrl = (String) defaultPhoto.get("medium_url");
            if (imageUrl == null) {
                imageUrl = (String) defaultPhoto.get("square_url");
            }

            if (imageUrl == null) {
                return null;
            }

            String attribution = (String) defaultPhoto.get("attribution");

            BirdImageResult result = new BirdImageResult();
            result.setImageUrl(imageUrl);
            result.setAttribution(attribution);
            result.setSource("iNaturalist");

            return result;

        } catch (Exception e) {
            return null;
        }
    }
}