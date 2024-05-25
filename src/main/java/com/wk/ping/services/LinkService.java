package com.wk.ping.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.wk.ping.dto.TeamRegDTO;
import com.wk.ping.models.Link;
import com.wk.ping.models.Team;
import com.wk.ping.repository.LinkRepository;
import com.wk.ping.repository.TeamRepository;
import org.springframework.stereotype.Service;

@Service
public class LinkService {
    private final LinkRepository linkRepository;

    private final TeamRepository teamRepository;

    public LinkService(LinkRepository linkRepository, TeamRepository teamRepository) {
        this.linkRepository = linkRepository;
        this.teamRepository = teamRepository;
    }

    public List<Link> getAllLinks() {
        List<Link> links = new ArrayList<>();
        linkRepository.findAll().forEach(l -> links.add(l));
        return links;
    }

    public Optional<Link> getLink(Long id) {
        return linkRepository.findById(id);
    }

    public void addLink(Link l) {
        l.setLastPingTime(null);
        l.setStatus_code(0);
        linkRepository.save(l);
    }

    public void addLinkByTeamName(TeamRegDTO teamRegDTO) {
        Link l = new Link();
        l.setLastPingTime(null);
        l.setStatus_code(0);
        List<Team> teams = teamRepository.findByName(teamRegDTO.getTeam_name());
        l.setTeam_id(teams.get(0).getId());
        l.setLinkName(teamRegDTO.getLinkName());
        l.setLink(teamRegDTO.getLink());
        linkRepository.save(l);
    }

    public void updateLink(Link l) {
        linkRepository.save(l);
    }

    public void deleteLink(Long id) {
        linkRepository.deleteById(id);
    }
}
