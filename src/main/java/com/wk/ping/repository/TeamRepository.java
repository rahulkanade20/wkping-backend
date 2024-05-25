package com.wk.ping.repository;

import java.util.List;

import com.wk.ping.models.Team;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long> {
    List<Team> findByName(String name);
}
