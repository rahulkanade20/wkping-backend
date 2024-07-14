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
        logger.info("sending ping request, multithreaded version");
        String l = link.getLink();
        logger.info("Link Object is -> " + link.toString());
        ResponseEntity<String> response;
        int responseCode;
        try {
            response = restTemplate.exchange(l, HttpMethod.GET, null, String.class);
            logger.info("Id is " + link.getId() + " | " + "url is " + link.getLink() + " | " + "status is " + response.getStatusCode().value());
            responseCode = response.getStatusCode().value();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.out.println("Catch block" + e.getStatusCode().value());
            logger.error("Error while making call to url " + e.getMessage());
            responseCode = e.getStatusCode().value();
        }
        logger.info("Out of restTemplate logic");
        link.setStatus_code(responseCode);
        link.setLastPingTime(LocalDateTime.now());
        linkService.updateLink(link);
        logger.info("Updation done");
        long requestEndTime = System.currentTimeMillis();
        logger.info("Total time required by thread is: " + (requestEndTime - requestStartTime));
    }
}
