package com.example.gachonhub.domain.team;

import com.example.gachonhub.domain.team.Team.TeamType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Page<Team> findAllByType(Pageable pageable, TeamType type);

}
