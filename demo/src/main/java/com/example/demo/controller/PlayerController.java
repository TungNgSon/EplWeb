package com.example.demo.controller;

import com.example.demo.entity.Player;
import com.example.demo.service.impl.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/players")
public class PlayerController {
    @Autowired
    private PlayerServiceImpl playerService;
    @GetMapping("")
    public String showAllPlayers(
            @RequestParam(value = "column", required = false) String column,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "sortColumn", required = false, defaultValue = "id") String sortColumn,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "asc") String sortDirection,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            Model model) {

        // Phân trang và sắp xếp
        Page<Player> playerPage = playerService.getPlayersWithPaginationAndSorting(column, keyword, sortColumn, sortDirection, page, size);

        model.addAttribute("players", playerPage.getContent()); // Nội dung của trang hiện tại
        model.addAttribute("currentPage", playerPage.getNumber() + 1); // Trang hiện tại (bắt đầu từ 1)
        model.addAttribute("totalPages", playerPage.getTotalPages()); // Tổng số trang
        model.addAttribute("totalItems", playerPage.getTotalElements()); // Tổng số players
        model.addAttribute("sortColumn", sortColumn); // Cột đang được sắp xếp
        model.addAttribute("sortDirection", sortDirection); // Hướng sắp xếp (asc/desc)

        return "all_players";
    }
    @GetMapping("/add")
    public String addPlayer(Model model)
    {
        Player player=new Player();
        model.addAttribute("player",player);
        return "add_player";
    }
    @PostMapping("/save")
    public String savePlayer(Player player)
    {
        playerService.savePlayer(player);
        return "redirect:/players";
    }
    @GetMapping("/delete/{idPlayer}")
    public String deletePlayer(@PathVariable int idPlayer)
    {
        playerService.deletePlayer(idPlayer);
        return "redirect:/players";
    }
    @GetMapping("/edit/{idPlayer}")
    public String updatePlayer(@PathVariable int idPlayer,Model model)
    {
        Player player=playerService.findById(idPlayer);
        model.addAttribute("player",player);
        return "edit_player";
    }
    @GetMapping("/deleteAll")
    public String deleteAllPlayers()
    {
        playerService.deleteAllPlayers();
        return "redirect:/players";
    }


}
