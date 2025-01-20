package com.example.demo.service.impl;

import com.example.demo.entity.Team;
import com.example.demo.repository.TeamRepository;
import com.example.demo.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    private TeamRepository teamRepository;
    @Override
    public void saveTeam(Team team) {
        teamRepository.save(team);
    }

    @Override
    public void deleteTeam(Team team) {
        teamRepository.delete(team);
    }

    @Override
    public void updateTeam(Team team) {
        teamRepository.save(team);
    }



    @Override
    public Team findById(int idTeam) {
        return teamRepository.findById(idTeam).orElse(null);
    }

    @Override
    public List<Team> findAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    public Page<Team> getTeamsWithPaginationAndSorting(String column, String keyword, String sortColumn, String sortDirection, int page, int size) {
        // Tạo đối tượng Sort

        Sort sort = Sort.by(sortColumn);
        if ("desc".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        }

        // Tạo đối tượng PageRequest để phân trang
        PageRequest pageRequest = PageRequest.of(page - 1, size, sort); // Trang Spring bắt đầu từ 0

        // Nếu tìm kiếm, gọi phương thức tìm kiếm với phân trang
        if (keyword != null && column != null && !keyword.isEmpty()) {
//            return searchTeamsByColumnWithPagination(column, keyword, pageRequest);
            Page<Team> searchResult = searchTeamsByColumnWithPagination(column, keyword, pageRequest);
            if ("numberOfPlayers".equals(sortColumn)) {
                List<Team> sortedTeams = searchResult.getContent();
                sortedTeams.sort((t1, t2) -> {
                    int size1 = t1.getPlayers().size();
                    int size2 = t2.getPlayers().size();
                    return "asc".equalsIgnoreCase(sortDirection) ? Integer.compare(size1, size2) : Integer.compare(size2, size1);
                });
                int start = (int) pageRequest.getOffset();
                int end = Math.min((start + pageRequest.getPageSize()), sortedTeams.size());
                return new PageImpl<>(sortedTeams.subList(start, end), pageRequest, sortedTeams.size());
            }
            return searchResult;
        }
        if ("numberOfPlayers".equals(sortColumn)) {
            List<Team> teams = teamRepository.findAll();
            teams.sort((t1, t2) -> {
                int size1 = t1.getPlayers().size();
                int size2 = t2.getPlayers().size();
                return "asc".equalsIgnoreCase(sortDirection) ? Integer.compare(size1, size2) : Integer.compare(size2, size1);
            });
            int start = (int) pageRequest.getOffset();
            int end = Math.min((start + pageRequest.getPageSize()), teams.size());
            return new PageImpl<>(teams.subList(start, end), pageRequest, teams.size());
        }

        // Nếu không tìm kiếm, trả về danh sách phân trang và sắp xếp toàn bộ
        return teamRepository.findAll(pageRequest);
    }
//    @Override
//    public Page<Team> getTeamsWithPaginationAndSorting(String column, String keyword, String sortColumn, String sortDirection, int page, int size) {
//        // Tạo đối tượng PageRequest để phân trang
//        PageRequest pageRequest = PageRequest.of(page - 1, size); // Trang Spring bắt đầu từ 0
//
//        // Nếu tìm kiếm, gọi phương thức tìm kiếm với phân trang
//        if (keyword != null && column != null && !keyword.isEmpty()) {
//            Page<Team> searchResult = searchTeamsByColumnWithPagination(column, keyword, pageRequest);
//            if ("numberOfPlayers".equals(sortColumn)) {
//                List<Team> sortedTeams = new ArrayList<>(searchResult.getContent());
//                sortedTeams.sort((t1, t2) -> {
//                    int size1 = t1.getPlayers().size();
//                    int size2 = t2.getPlayers().size();
//                    return "asc".equalsIgnoreCase(sortDirection) ? Integer.compare(size1, size2) : Integer.compare(size2, size1);
//                });
//                int start = (int) pageRequest.getOffset();
//                int end = Math.min((start + pageRequest.getPageSize()), sortedTeams.size());
//                return new PageImpl<>(sortedTeams.subList(start, end), pageRequest, sortedTeams.size());
//            }
//            return searchResult;
//        }
//
//        // Nếu không tìm kiếm, trả về danh sách phân trang và sắp xếp toàn bộ
//        if ("numberOfPlayers".equals(sortColumn)) {
//            List<Team> teams = teamRepository.findAll();
//            teams.sort((t1, t2) -> {
//                int size1 = t1.getPlayers().size();
//                int size2 = t2.getPlayers().size();
//                return "asc".equalsIgnoreCase(sortDirection) ? Integer.compare(size1, size2) : Integer.compare(size2, size1);
//            });
//            int start = (int) pageRequest.getOffset();
//            int end = Math.min((start + pageRequest.getPageSize()), teams.size());
//            return new PageImpl<>(teams.subList(start, end), pageRequest, teams.size());
//        }
//
//        // Tạo đối tượng Sort
//        Sort sort = Sort.by(sortColumn);
//        if ("desc".equalsIgnoreCase(sortDirection)) {
//            sort = sort.descending();
//        }
//        pageRequest = PageRequest.of(page - 1, size, sort);
//
//        return teamRepository.findAll(pageRequest);
//    }

    @Override
    public Page<Team> searchTeamsByColumnWithPagination(String column, String keyword, PageRequest pageRequest) {
        switch (column) {
            case "name":
                return teamRepository.findByNameContainingIgnoreCase(keyword, pageRequest);
            case "country":
                return teamRepository.findByCountryContainingIgnoreCase(keyword, pageRequest);
            case "ranked":
                try
                {
                    Integer rank = Integer.parseInt(keyword);
                    return teamRepository.findByRanked(rank, pageRequest);
                }
                catch (NumberFormatException e)
                {
                    return Page.empty();
                }
            case "numberOfPlayers":
                try {
                    Integer numberOfPlayers = Integer.parseInt(keyword);
                    return teamRepository.findByNumberOfPlayers(numberOfPlayers, pageRequest);
                } catch (NumberFormatException e) {
                    return Page.empty();
                }
            default:
                return Page.empty();

        }
    }

}
