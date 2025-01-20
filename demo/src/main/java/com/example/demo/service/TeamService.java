package com.example.demo.service;

import com.example.demo.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
public interface TeamService {
    void saveTeam(Team team);
    void deleteTeam(Team team);
    void updateTeam(Team team);
    Team findById(int idTeam);
    List<Team> findAllTeams();
    Page<Team> getTeamsWithPaginationAndSorting(String column, String keyword, String sortColumn, String sortDirection, int page, int size);
    Page<Team> searchTeamsByColumnWithPagination(String column, String keyword, PageRequest pageRequest);
}

