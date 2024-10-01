package com.wk.ping.controllers;

import java.util.List;

import com.wk.ping.dto.TeamRegDTO;
import com.wk.ping.models.Link;
import com.wk.ping.services.LinkService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/links")
@CrossOrigin(origins = "${front_end_url}")
public class LinksController {
    private final LinkService linkService;

    public LinksController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/test")
    public String getTestString() {
        return "hello";
    }

    @GetMapping("")
    public List<Link> getAllLinks() {
        return linkService.getAllLinks();
    }

    @PostMapping("")
    public void addLink(@RequestBody Link link) {
        linkService.addLink(link);
    }

    @PostMapping("/register")
    public void addLinkByTeamName(@RequestBody TeamRegDTO teamRegDTO) {
        linkService.addLinkByTeamName(teamRegDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteLink(@PathVariable Long id) {
        linkService.deleteLink(id);
    }
}
