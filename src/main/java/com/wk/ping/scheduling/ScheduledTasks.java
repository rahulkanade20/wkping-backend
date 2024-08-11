package com.wk.ping.scheduling;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.wk.ping.models.Link;
import com.wk.ping.services.HttpRequestService;
import com.wk.ping.services.LinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private final LinkService linkService;

    private final HttpRequestService httpRequestService;

    private final ThreadPoolExecutor executorService;

    public ScheduledTasks(LinkService linkService, HttpRequestService httpRequestService) {
        log.info("ScheduledTasksConstructorCalled");
        this.linkService = linkService;
        this.httpRequestService = httpRequestService;
        this.executorService = new ThreadPoolExecutor(3, 5, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    }

    @Scheduled(fixedRate = 60000)
    public void updatePingTimeMultiThreaded() {
        List<Link> allLinks = linkService.getAllLinks();
        for(Link link : allLinks) {
            try {
                executorService.submit(() -> {
                    httpRequestService.makeRequest(link);
                    log.info("end of runnable task");
                });
            } catch(RejectedExecutionException e) {
                log.error("Task rejected. All threads busy, waiting queue full " + "Rejected for Link Object -> " + link.toString());
                e.printStackTrace();
            } catch(Exception e) {
                log.error("Unknown exception while task execution");
                e.printStackTrace();
            }
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                log.error("Interrupted exception in main thread sleep");
            }
        }
        log.info("ALL REQUESTS DONE");
    }

//    @Scheduled(fixedRate = 20000)
//    public void updatePingTime() {
//        List<Link> allLinks = linkService.getAllLinks();
////        System.out.println("Ping started");
////        System.out.println("TESTING CI 1");
//        for(Link link : allLinks) {
////            String url = link.getLink();
//////            int statusCode = Ping(url);
////            int statusCode = makeCall(url);
////            link.setStatus_code(statusCode);
////            link.setLastPingTime(LocalDateTime.now());
////            linkService.updateLink(link);
//
//            httpRequestService.makeRequest(link);
//        }
////        System.out.println("test log 1");
////        System.out.println("Ping ended");
//    }

//    public int makeCall(String url) {
//        ResponseEntity<String> response;
//        try {
//            response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
//        } catch (HttpClientErrorException e) {
//            System.out.println("Catch block" + e.getStatusCode().value());
//            return e.getStatusCode().value();
//        } catch (HttpServerErrorException e) {
//            System.out.println("Catch block" + e.getStatusCode().value());
//            return e.getStatusCode().value();
//        }
//        System.out.println("make Call" + response.getStatusCode().value());
//        return response.getStatusCode().value();
//    }

//    public int Ping(String url) {
//        try {
//            ProcessBuilder processBuilder = new ProcessBuilder("ping", url);
//            Process process = processBuilder.start();
//            int exitCode = process.waitFor();
//            if (exitCode == 0) {
//                return 1; // ping success, service is up
//            } else {
//                return -1; // ping failed, service is down
//            }
//        } catch (Exception e) {
//            return 0; // ping command failed to execute, service status unknown
//        }
//    }
}
