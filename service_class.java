import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlService {

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    private static final String URL_PREFIX = "http://localhost:8080/";

    public String shortenUrl(String destinationUrl) {
        String shortUrl = generateShortUrl();
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setDestinationUrl(destinationUrl);
        urlMapping.setCreatedAt(LocalDateTime.now());
        urlMapping.setExpiryAt(LocalDateTime.now().plusMonths(10));

        urlMappingRepository.save(urlMapping);
        return URL_PREFIX + shortUrl;
    }

    public boolean updateShortUrl(String shortUrl, String newDestinationUrl) {
        Optional<UrlMapping> optionalUrlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (optionalUrlMapping.isPresent()) {
            UrlMapping urlMapping = optionalUrlMapping.get();
            urlMapping.setDestinationUrl(newDestinationUrl);
            urlMappingRepository.save(urlMapping);
            return true;
        }
        return false;
    }

    public String getDestinationUrl(String shortUrl) {
        Optional<UrlMapping> optionalUrlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (optionalUrlMapping.isPresent()) {
            UrlMapping urlMapping = optionalUrlMapping.get();
            if (urlMapping.getExpiryAt().isAfter(LocalDateTime.now())) {
                return urlMapping.getDestinationUrl();
            }
        }
        return null;
    }

    public boolean updateExpiry(String shortUrl, int daysToAdd) {
        Optional<UrlMapping> optionalUrlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (optionalUrlMapping.isPresent()) {
            UrlMapping urlMapping = optionalUrlMapping.get();
            urlMapping.setExpiryAt(urlMapping.getExpiryAt().plusDays(daysToAdd));
            urlMappingRepository.save(urlMapping);
            return true;
        }
        return false;
    }

    private String generateShortUrl() {
        // Generate a random 8-character string
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder shortUrl = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            shortUrl.append(chars.charAt(random.nextInt(chars.length())));
        }
        return shortUrl.toString();
    }
}
