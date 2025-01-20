package com.example.demo.repository;

import com.example.demo.entity.Player;
import com.example.demo.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository  extends JpaRepository<Team,Integer> {
    Page<Team> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Team> findByCountryContainingIgnoreCase(String country, Pageable pageable);
    Page<Team> findByRanked(int ranked, Pageable pageable);
    @Query("SELECT t from Team t where SIZE(t.players) = :numbersOfPlayers")
    Page<Team> findByNumberOfPlayers(int numbersOfPlayers, Pageable pageable);
}
