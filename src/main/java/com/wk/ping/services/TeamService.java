package com.wk.ping.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.wk.ping.dto.TeamRegDTO;
import com.wk.ping.models.Link;
import com.wk.ping.models.Team;
import com.wk.ping.repository.TeamRepository;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> getAllTeams() {
        List<Team> teams = new ArrayList<>();
        teamRepository.findAll().forEach(t -> teams.add(t));
        return teams;
    }

    public Optional<Team> getTeam(Long id) {
        return teamRepository.findById(id);
    }

    public List<Team> getTeamByName(String name) {
        return teamRepository.findByName(name);
    }

    public void addTeam(Team t) {
        teamRepository.save(t);
    }

    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }
}
