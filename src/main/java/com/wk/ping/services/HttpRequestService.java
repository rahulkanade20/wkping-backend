package com.wk.ping.services;

import java.time.LocalDateTime;

import com.wk.ping.models.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpRequestService {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestService.class);

    private final LinkService linkService;
    private final RestTemplate restTemplate;

    public HttpRequestService(LinkService linkService, RestTemplate restTemplate) {
        this.linkService = linkService;
        this.restTemplate = restTemplate;
    }

    public void makeRequest(Link link) {
        long requestStartTime = System.currentTimeMillis();
        logger.debug("Starting health check for link ID: {}, URL: {}", link.getId(), link.getLink());

        int responseCode = performHealthCheck(link.getLink());

        long requestEndTime = System.currentTimeMillis();
        long totalTime = requestEndTime - requestStartTime;

        updateLinkStatus(link, responseCode, totalTime);

        logger.info("Health check completed for ID: {} - Status: {} - Time: {}ms",
                   link.getId(), responseCode, totalTime);
    }

    private int performHealthCheck(String url) {
        long reqStartTime = System.currentTimeMillis();

        try {
            // Try HEAD request first (more efficient - no response body)
            ResponseEntity<Void> response = restTemplate.exchange(
                url, HttpMethod.HEAD, null, Void.class);

            long reqEndTime = System.currentTimeMillis();
            logger.debug("HEAD request successful for {} - Status: {} - Time: {}ms",
                        url, response.getStatusCode().value(), (reqEndTime - reqStartTime));

            return response.getStatusCode().value();

        } catch (HttpClientErrorException httpClientException) {
            // Check if server doesn't support HEAD method (405 Method Not Allowed, 400 Bad Request)
            int statusCode = httpClientException.getStatusCode().value();
            long reqEndTime = System.currentTimeMillis();

            if (statusCode == 405 || statusCode == 501 || statusCode == 400) {
                logger.debug("HEAD method not supported for {} - Status: {} - Trying GET fallback",
                           url, statusCode);
                return tryGetRequestFallback(url);
            } else {
                // Other 4xx errors (404, 401, 403, etc.) are valid responses
                logger.debug("HTTP client error for {} - Status: {} - Time: {}ms",
                            url, statusCode, (reqEndTime - reqStartTime));
                return statusCode;
            }

        } catch (HttpServerErrorException httpServerException) {
            // Check if server doesn't implement HEAD method (501 Not Implemented)
            int statusCode = httpServerException.getStatusCode().value();
            long reqEndTime = System.currentTimeMillis();

            if (statusCode == 501) {
                logger.debug("HEAD method not implemented for {} - Status: {} - Trying GET fallback",
                           url, statusCode);
                return tryGetRequestFallback(url);
            } else {
                // Other 5xx errors are valid server error responses
                logger.debug("HTTP server error for {} - Status: {} - Time: {}ms",
                            url, statusCode, (reqEndTime - reqStartTime));
                return statusCode;
            }

        } catch (ResourceAccessException resourceException) {
            // Connection timeout, DNS resolution failed, etc.
            long reqEndTime = System.currentTimeMillis();
            logger.warn("Connection failed for {} - Error: {} - Time: {}ms",
                       url, resourceException.getMessage(), (reqEndTime - reqStartTime));

            // Try fallback to GET request in case it's a network issue
            return tryGetRequestFallback(url);

        } catch (Exception e) {
            // Unknown exception
            long reqEndTime = System.currentTimeMillis();
            logger.error("Unknown exception for {} - Error: {} - Time: {}ms",
                        url, e.getMessage(), (reqEndTime - reqStartTime));
            return -1; // Service unreachable
        }
    }

    private int tryGetRequestFallback(String url) {
        try {
            logger.debug("Trying GET fallback for URL: {}", url);
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, null, String.class);

            logger.debug("GET fallback successful for {} - Status: {}", url, response.getStatusCode().value());
            return response.getStatusCode().value();

        } catch (HttpClientErrorException | HttpServerErrorException httpException) {
            return httpException.getStatusCode().value();
        } catch (Exception e) {
            logger.error("GET fallback also failed for {} - Error: {}", url, e.getMessage());
            return -1; // Completely unreachable
        }
    }

    private void updateLinkStatus(Link link, int responseCode, long responseTime) {
        long dbUpdateStartTime = System.currentTimeMillis();

        link.setStatus_code(responseCode);
        link.setLastPingTime(LocalDateTime.now());
        linkService.updateLink(link);

        long dbUpdateEndTime = System.currentTimeMillis();
        logger.debug("Database update completed for ID: {} in {}ms",
                    link.getId(), (dbUpdateEndTime - dbUpdateStartTime));
    }
}
