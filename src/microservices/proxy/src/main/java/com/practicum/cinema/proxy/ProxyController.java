package com.practicum.cinema.proxy;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@RestController
public class ProxyController {

    private final RestTemplate restTemplate;

    @Value("${MOVIES_SERVICE_URL:http://localhost:8081}")
    private String moviesServiceUrl;

    @Value("${EVENTS_SERVICE_URL:http://localhost:8082}")
    private String eventsServiceUrl;

    @Value("${MONOLITH_URL:http://localhost:8080}")
    private String monolithUrl;

    @Value("${GRADUAL_MIGRATION:false}")
    private String gradualMigration;

    @Value("${MOVIES_MIGRATION_PERCENT:50}")
    private int moviesMigrationPercent;

    private final java.util.Random random = new java.util.Random();

    public ProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<byte[]> proxy(HttpServletRequest request, @RequestBody(required = false) byte[] body) 
            throws URISyntaxException, IOException {
        
        String path = request.getRequestURI();
        String query = request.getQueryString();
        String targetBaseUrl;

        if (path.startsWith("/api/movies")) {
            if ("true".equalsIgnoreCase(gradualMigration)) {
                if (random.nextInt(100) < moviesMigrationPercent) {
                    targetBaseUrl = moviesServiceUrl;
                } else {
                    targetBaseUrl = monolithUrl;
                }
            } else {
                targetBaseUrl = monolithUrl;
            }
        } else if (path.startsWith("/api/events")) {
            targetBaseUrl = eventsServiceUrl;
        } else {
            targetBaseUrl = monolithUrl;
        }

        if (targetBaseUrl.endsWith("/")) {
            targetBaseUrl = targetBaseUrl.substring(0, targetBaseUrl.length() - 1);
        }

        URI uri = new URI(targetBaseUrl + path + (query != null ? "?" + query : ""));

        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (!headerName.equalsIgnoreCase("host")) {
                headers.addAll(headerName, Collections.list(request.getHeaders(headerName)));
            }
        }

        HttpEntity<byte[]> httpEntity = new HttpEntity<>(body, headers);

        try {
            return restTemplate.exchange(uri, HttpMethod.valueOf(request.getMethod()), httpEntity, byte[].class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsByteArray());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage().getBytes());
        }
    }
}
