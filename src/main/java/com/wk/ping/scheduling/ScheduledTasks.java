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
        this.executorService = new ThreadPoolExecutor(
            10,  // corePoolSize: More threads for better throughput
            20,  // maximumPoolSize: Can scale up during peak load
            60L, // keepAliveTime: Keep extra threads alive for 1 minute
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000), // Larger queue to handle more concurrent requests
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy() // Better rejection policy - runs in caller thread instead of throwing exception
        );
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void updatePingTimeMultiThreaded() {
        List<Link> allLinks = linkService.getAllLinks();
        log.info("Starting to ping {} links", allLinks.size());
        for(Link link : allLinks) {
            try {
                executorService.submit(() -> {
                    httpRequestService.makeRequest(link);
                    log.debug("Completed ping for: {}", link.getLink());
                });
            } catch(RejectedExecutionException e) {
                log.error("Task rejected for Link: {} - Queue might be full", link.toString());
                e.printStackTrace();
            } catch(Exception e) {
                log.error("Unknown exception while submitting ping task for link: {}", link.toString());
                e.printStackTrace();
            }
        }
        log.info("ALL PING TASKS SUBMITTED - {} tasks in queue, {} active threads",
                 executorService.getQueue().size(), executorService.getActiveCount());
    }
}
