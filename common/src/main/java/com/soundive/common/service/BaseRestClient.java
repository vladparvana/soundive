package com.soundive.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
public abstract class BaseRestClient {

    protected final RestTemplate restTemplate;

    public BaseRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Send a POST request and return a response of the specified type.
     * 
     * @param uri          Target URL for the POST request.
     * @param request      Request body object.
     * @param responseType Response type expected from the API.
     * @param <T>          Generic type parameter for the response.
     * @return Response body of the specified type, or null if the request fails.
     */
    protected <T> T postForEntity(String uri, Object request, Class<T> responseType) {
        log.info("Performing POST request to URI: {}", uri);
        try {
            HttpEntity<Object> entity = prepareEntity(request);
            ResponseEntity<T> response = restTemplate.postForEntity(uri, entity, responseType);

            if (response.hasBody()) {
                log.info("POST request to URI: {} completed successfully.", uri);
                return response.getBody();
            }

            log.warn("POST request to URI: {} completed with no body.", uri);
        } catch (RestClientException ex) {
            log.error("Error occurred during POST request to URI: {}: {}", uri, ex.getMessage());
            throw ex; // Rethrow to handle it where required
        }
        return null;
    }

    /**
     * Send a GET request and return a response of the specified type.
     *
     * @param uri          Target URL for the GET request.
     * @param responseType Response type expected from the API.
     * @param <T>          Generic type parameter for the response.
     * @return Response body of the specified type, or null if the request fails.
     */
    protected <T> T getForEntity(String uri, Class<T> responseType) {
        log.info("Performing GET request to URI: {}", uri);
        try {
            ResponseEntity<T> response = restTemplate.getForEntity(uri, responseType);

            if (response.hasBody()) {
                log.info("GET request to URI: {} completed successfully.", uri);
                return response.getBody();
            }

            log.warn("GET request to URI: {} completed with no body.", uri);
        } catch (RestClientException ex) {
            log.error("Error occurred during GET request to URI: {}: {}", uri, ex.getMessage());
            throw ex; // Rethrow to handle it where required
        }
        return null;
    }

    /**
     * Prepares an HttpEntity for the request with JSON content type headers.
     */
    private HttpEntity<Object> prepareEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
}