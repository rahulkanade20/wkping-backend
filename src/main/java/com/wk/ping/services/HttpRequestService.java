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

//    private final RestTemplate restTemplate;

    public HttpRequestService(LinkService linkService) {
        this.linkService = linkService;
//        this.restTemplate = restTemplate;
    }

    public void makeRequest(Link link) {
        RestTemplate restTemplate = new RestTemplate();
        long requestStartTime = System.currentTimeMillis();
        logger.info("sending ping request, multithreaded version");
        String l = link.getLink();
        logger.info("Link Object is -> " + link.toString());
        ResponseEntity<String> response;
        int responseCode;
        logger.info("Id is " + link.getId() + " just before try block");
        long reqHitStartTime = System.currentTimeMillis();
        try {
            logger.info("Id is " + link.getId() + " inside try just before rest template call");
            response = restTemplate.exchange(l, HttpMethod.GET, null, String.class);
            logger.info("Id is " + link.getId() + " | " + "url is " + link.getLink() + " | " + "status is " + response.getStatusCode().value());
            responseCode = response.getStatusCode().value();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.out.println("Id is " + link.getId() + "Catch block" + e.getStatusCode().value());
            logger.error("Error while making call to url " + e.getMessage());
            responseCode = e.getStatusCode().value();
        } catch (Exception e) {
//            e.printStackTrace();
            logger.error("Unknown exception while making call to endpoint");
            responseCode = -1;
        }
        long reqHitEndTime = System.currentTimeMillis();
        logger.info("Time required to make request - " + (reqHitEndTime - reqHitStartTime));
        logger.info("Id is " + link.getId() + "Out of restTemplate logic");
        long updateInDatabaseStartTime = System.currentTimeMillis();
        link.setStatus_code(responseCode);
        link.setLastPingTime(LocalDateTime.now());
        linkService.updateLink(link);
        long updateInDatabaseEndTime = System.currentTimeMillis();
        logger.info("Time required to update row in database - " + (updateInDatabaseEndTime - updateInDatabaseStartTime));
        logger.info("Id is " + link.getId() + "Updation done");
        long requestEndTime = System.currentTimeMillis();
        logger.info("Id is " + link.getId() + "Total time required by thread is: " + (requestEndTime - requestStartTime));
    }
}
