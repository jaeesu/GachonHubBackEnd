package com.example.gachonhub.team.domain;

import com.example.gachonhub.team.domain.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Page<Team> findAllByType(Pageable pageable, Team.TeamType type);

}
