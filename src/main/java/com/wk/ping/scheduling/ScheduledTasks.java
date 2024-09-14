package com.wk.ping.scheduling;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.wk.ping.models.Link;
import com.wk.ping.services.LinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private final LinkService linkService;

    private final RestTemplate restTemplate;

    public ScheduledTasks(LinkService linkService, RestTemplate restTemplate) {
        this.linkService = linkService;
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    public void updatePingTime() {
        List<Link> allLinks = linkService.getAllLinks();
        System.out.println("Ping started");
        for(Link link : allLinks) {
            String url = link.getLink();
//            int statusCode = Ping(url);
            int statusCode = makeCall(url);
            link.setStatus_code(statusCode);
            link.setLastPingTime(LocalDateTime.now());
            linkService.updateLink(link);
        }
        System.out.println("test log 1");
        System.out.println("Ping ended");
    }

    public int makeCall(String url) {
//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        } catch (HttpClientErrorException e) {
            System.out.println("Catch block" + e.getStatusCode().value());
            return e.getStatusCode().value();
        } catch (HttpServerErrorException e) {
            System.out.println("Catch block" + e.getStatusCode().value());
            return e.getStatusCode().value();
        } catch (Exception e) {
            System.out.println("Catch block with some other exception");
            return -1;
        }
        System.out.println("make Call" + response.getStatusCode().value());
        return response.getStatusCode().value();
    }

    public int Ping(String url) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("ping", url);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return 1; // ping success, service is up
            } else {
                return -1; // ping failed, service is down
            }
        } catch (Exception e) {
            return 0; // ping command failed to execute, service status unknown
        }
    }
}
