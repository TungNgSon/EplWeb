package com.example.demo.controller;

import com.example.demo.entity.Coach;
import com.example.demo.entity.Player;
import com.example.demo.entity.Team;
import com.example.demo.service.impl.CoachServiceImpl;
import com.example.demo.service.impl.PlayerServiceImpl;
import com.example.demo.service.impl.TeamServiceImpl;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ChatController {

    private final ChatClient chatClient;
    @Autowired
    TeamServiceImpl teamService;
    @Autowired
    CoachServiceImpl coachService;
    @Autowired
    PlayerServiceImpl playerService;
    @Autowired
    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/chat")
    public String chatPage() {
        return "chat";
    }

    @GetMapping("/ai/generate")
    @ResponseBody
    public Map<String, String> generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        String response = chatClient.prompt().user(message).call().content();
        return Map.of("generation", response);
    }

    @GetMapping("/ai/generateStream")
    @ResponseBody
    public Flux<String> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return chatClient.prompt().user(message).stream().content();
    }

    @GetMapping("/ai/generateWithData")
    @ResponseBody
    public Map<String, String> generateWithData(@RequestParam(value = "message", defaultValue = "Tell me about the teams") String message) {
        List<Team> teams = teamService.findAllTeams();
        List<Player> players = playerService.showAllPlayers();
        List<Coach> coaches = coachService.findAllCoach();

        String teamData = teams.stream()
                .map(team -> "Team: " + team.getName() + ", Country: " + team.getCountry() + ", Ranked: " + team.getRanked())
                .collect(Collectors.joining("\n"));

        String playerData = players.stream()
                .map(player -> "Player: " + player.getName() + ", Country: " + player.getCountry() + ", Age: " + player.getAge() + ", Position: " + player.getPosition())
                .collect(Collectors.joining("\n"));

        String coachData = coaches.stream()
                .map(coach -> "Coach: " + coach.getName() + ", Country: " + coach.getCountry() + ", Age: " + coach.getAge())
                .collect(Collectors.joining("\n"));

        String prompt = message + "\n\n" + "Teams:\n" + teamData + "\n\n" + "Players:\n" + playerData + "\n\n" + "Coaches:\n" + coachData;

        String response = chatClient.prompt().user(prompt).call().content();
        return Map.of("generation", response);
    }
}