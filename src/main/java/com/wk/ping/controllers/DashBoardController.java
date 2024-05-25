package com.wk.ping.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.wk.ping.dto.UIData;
import com.wk.ping.models.Link;
import com.wk.ping.models.Team;
import com.wk.ping.services.LinkService;
import com.wk.ping.services.TeamService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/fetchData")
@CrossOrigin(origins = "http://localhost:3000/")
public class DashBoardController {

    private final LinkService linkService;

    private final TeamService teamService;

    public DashBoardController(LinkService linkService, TeamService teamService) {
        this.linkService = linkService;
        this.teamService = teamService;
    }

    @GetMapping("/test")
    public String getTestString() {
        return "hello";
    }

    @GetMapping("")
    public List<UIData> fetchAllData() {
        List<Link> allLinks = linkService.getAllLinks();
        List<UIData> data = new ArrayList<>();
        for(Link link : allLinks) {
            String teamName = "";
            String status = "";
            Optional<Team> t = teamService.getTeam(link.getTeam_id());
            if(t.isPresent()) {
                teamName = t.get().getName();
            }
            int statusCode = link.getStatus_code();
//            if(statusCode == 1) {
//                status = "UP";
//            } else if (statusCode == -1) {
//                status = "DOWN";
//            } else if(statusCode == 0) {
//                status = "UNKNOWN";
//            }
//            if(statusCode >= 200 && statusCode<300) {
//                status = "UP";
//            } else if (statusCode >= 500 && statusCode<600) {
//                status = "DOWN";
//            } else {
//                status = "UNKNOWN";
//            }
            if(statusCode == 200) {
                status = "HEALTHY";
            } else if(statusCode == 0) {
                status = "UNKNOWN";
            }
            else {
                status = "UNHEALTHY";
            }
            data.add(new UIData(teamName, link.getLinkName(), link.getLink(), status, statusCode, link.getLastPingTime()));
        }
        return data;
    }
}