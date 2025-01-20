package com.example.demo.controller;

import com.example.demo.entity.Coach;
import com.example.demo.entity.Team;
import com.example.demo.service.impl.CoachServiceImpl;
import com.example.demo.service.impl.TeamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/coaches")
public class CoachController {
    @Autowired
    CoachServiceImpl coachService;
    @Autowired
    TeamServiceImpl teamService;
    @GetMapping("/")
    public String showAllCoaches(Model model)
    {
        model.addAttribute("coaches",coachService.findAllCoach());
        return "all_coaches";
    }
    @GetMapping("/add")
    public String addCoach(Model model)
    {
        Coach coach=new Coach();
        model.addAttribute("coach",coach);
        return "add_coach";
    }
    @PostMapping("/save")
    public String saveCoach(Coach coach)
    {
        coachService.saveCoach(coach);
        return "redirect:/coaches/";
    }
    @GetMapping("/delete/{idCoach}")
    public String deleteCoach(@PathVariable int idCoach)
    {
        coachService.deleteCoach(coachService.findById(idCoach));
        return "redirect:/coaches/";
    }
    @GetMapping("/edit/{idCoach}")
    public String updateCoach(@PathVariable int idCoach,Model model)
    {
        Coach coach=coachService.findById(idCoach);
        model.addAttribute("coach",coach);
        return "edit_coach";
    }
    @GetMapping("/select/{idCoach}")
    public String selectTeam(@PathVariable int idCoach,Model model)
    {
        Coach coach=coachService.findById(idCoach);
        List<Team> teams=teamService.findAllTeams();
        List<Team> teamlist=new ArrayList<>();
        for(Team team:teams)
        {
            if(team.getCoach()==null)
            {
                teamlist.add(team);
            }
        }
        model.addAttribute("teams",teamlist);
        model.addAttribute("coach",coach);
        return "select_team";
    }
    @PostMapping("/saveteam/{idCoach}")
    public String saveTeam(@PathVariable int idCoach,@RequestParam("teamId") int teamId)
    {
        Coach coach=coachService.findById(idCoach);
        Team team=teamService.findById(teamId);
//        if(team.getCoach()!=null)
//        {
//            team.setCoach(null);
//            teamService.saveTeam(team);
//            coach.setTeam(null);
//           coachService.saveCoach(coach);
//        }
        if(coach.getTeam()!=null)
        {
            coach.getTeam().setCoach(null);
            teamService.saveTeam(coach.getTeam());
            coach.setTeam(null);
            coachService.saveCoach(coach);

        }

        team.setCoach(coach);
        teamService.saveTeam(team);
        return "redirect:/coaches/";
    }
    @GetMapping("/release/{idCoach}")
    public String releaseTeam(@PathVariable int idCoach)
    {
        Coach coach=coachService.findById(idCoach);
        Team team=coach.getTeam();
        team.setCoach(null);
        teamService.saveTeam(team);
        return "redirect:/coaches/";
    }
}
