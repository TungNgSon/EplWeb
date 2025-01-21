package com.example.demo.controller;


import com.example.demo.entity.Player;
import com.example.demo.entity.Team;
import com.example.demo.service.impl.PlayerServiceImpl;
import com.example.demo.service.impl.TeamServiceImpl;
import com.example.demo.service.impl.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/teams")
public class TeamController {
    @Autowired
    private WeatherService weatherService;
    @Autowired
    private TeamServiceImpl teamService;
    @Autowired
    private PlayerServiceImpl playerService;
    @GetMapping("")
    public String showAllTeams(
            @RequestParam(value = "column", required = false) String column,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "sortColumn", required = false, defaultValue = "id") String sortColumn,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "asc") String sortDirection,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            Model model) {

        // Phân trang và sắp xếp
        Page<Team> teamPage = teamService.getTeamsWithPaginationAndSorting(column, keyword, sortColumn, sortDirection, page, size);

        model.addAttribute("teams", teamPage.getContent()); // Nội dung của trang hiện tại
        model.addAttribute("currentPage", teamPage.getNumber() + 1); // Trang hiện tại (bắt đầu từ 1)
        model.addAttribute("totalPages", teamPage.getTotalPages()); // Tổng số trang
        model.addAttribute("totalItems", teamPage.getTotalElements()); // Tổng số teams
        model.addAttribute("sortColumn", sortColumn); // Cột đang được sắp xếp
        model.addAttribute("sortDirection", sortDirection); // Hướng sắp xếp (asc/desc)

        return "all_teams";
    }
    @GetMapping("/add")
    public String addTeam(Model model)
    {
        Team team=new Team();
        model.addAttribute("team",team);
        return "add_team";
    }
    @PostMapping("/save")
    public String saveTeam(Team team)
    {
        teamService.saveTeam(team);
        return "redirect:/teams";
    }
    @GetMapping("/delete/{idTeam}")
    public String deleteTeam(@PathVariable int idTeam)
    {
        teamService.deleteTeam(teamService.findById(idTeam));
        return "redirect:/teams";
    }
    @GetMapping("/edit/{idTeam}")
    public String updateTeam(@PathVariable int idTeam,Model model)
    {
        Team team=teamService.findById(idTeam);
        model.addAttribute("team",team);
        return "edit_team";
    }
    @GetMapping("/addplayer/{idTeam}")
    public String addPlayer(@PathVariable int idTeam,Model model)
    {
        List<Player> playerlist=new ArrayList<>();
        List<Player> players=playerService.showAllPlayers();
        for(Player player:players)
        {
            if(player.getTeam()==null)
            {
                playerlist.add(player);
            }
        }
        Team team=teamService.findById(idTeam);
        model.addAttribute("team",team);
        model.addAttribute("players",playerlist);
        return "select_player";
    }
    @PostMapping("/saveplayer/{idTeam}")
    public String savePlayer(@PathVariable int idTeam,@RequestParam("playerId") int playerId)
    {
//        System.out.println("ID DUOC CHON LA:" +playerId);
        Team team=teamService.findById(idTeam);
        Player player=playerService.findById(playerId);
        player.setTeam(team);
        playerService.savePlayer(player);
        return "redirect:/teams";
    }
    @GetMapping("/viewplayer/{idTeam}")
    public String viewPlayer(@PathVariable int idTeam,Model model)
    {
        Team team=teamService.findById(idTeam);
        model.addAttribute("team",team);
        model.addAttribute("players",team.getPlayers());
        return "view_player";
    }
    @GetMapping("/releaseplayer/{idTeam}/{playerId}")
    public String releasePlayer(@PathVariable int idTeam,@PathVariable int playerId)
    {
        Team team=teamService.findById(idTeam);
        Player player=playerService.findById(playerId);
        player.setTeam(null);
        playerService.savePlayer(player);
        return "redirect:/teams";
    }
    @GetMapping("/player_detail/{idPlayer}")
    public String viewPlayerDetail(@PathVariable int idPlayer,Model model)
    {
        Player player=playerService.findById(idPlayer);
        model.addAttribute("player",player);
        return "player_detail";
    }
    @GetMapping("/teamdetail/{teamId}")
    public String viewTeamDetail(@PathVariable int teamId,Model model)
    {
        Team team=teamService.findById(teamId);
        Double latitude=team.getLatitude();
        Double longitude=team.getLongitude();
        Map<String, Object> weatherData = weatherService.getWeather1(latitude,longitude);
        model.addAttribute("weatherData", weatherData);
        model.addAttribute("team",team);
        return "team_detail";
    }







}
