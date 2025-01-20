package com.example.demo.service;

import com.example.demo.entity.Player;

import java.util.List;

public interface PlayerService {
    List<Player> showAllPlayers();
    Player findById(int idPlayer);
    void savePlayer(Player player);
    void deletePlayer(int idPlayer);
    void updatePlayer(Player player);
    void deleteAllPlayers();
    public List<Player> searchPlayersByColumn(String column, String keyword);
}
