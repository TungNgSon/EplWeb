package com.example.demo.service.impl;

import com.example.demo.entity.Player;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.service.PlayerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
public class PlayerServiceImpl implements PlayerService
{
    @Autowired
    private PlayerRepository playerRepository;
    @Override
    public List<Player> showAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Player findById(int idPlayer) {
        return playerRepository.findById(idPlayer).orElse(null);
    }

    @Override
    public void savePlayer(Player player) {
        playerRepository.save(player);
    }

    @Override
    public void deletePlayer(int idPlayer) {
        playerRepository.deleteById(idPlayer);
    }

    @Override
    public void updatePlayer(Player player) {
        playerRepository.save(player);
    }

    @Override
    public void deleteAllPlayers() {
        playerRepository.deleteAll();
    }

    @Override
    public List<Player> searchPlayersByColumn(String column, String keyword) {
        if(keyword==null || keyword.isEmpty() ||column==null)
        {
            return playerRepository.findAll();
        }
        switch (column)
        {
            case "name":
                return playerRepository.findByNameContainingIgnoreCase(keyword);

            case "country":
                return playerRepository.findByCountryContainingIgnoreCase(keyword);
            case "position":
                return playerRepository.findByPositionContainingIgnoreCase(keyword);
            case "age":
                return playerRepository.findByAge(Integer.parseInt(keyword));

            default:
                return playerRepository.findAll();
        }
    }

    public Page<Player> getPlayersWithPaginationAndSorting(String column, String keyword, String sortColumn, String sortDirection, int page, int size) {
        // Tạo đối tượng Sort
        Sort sort = Sort.by(sortColumn);
        if ("desc".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        }

        // Tạo đối tượng PageRequest để phân trang
        PageRequest pageRequest = PageRequest.of(page - 1, size, sort); // Trang Spring bắt đầu từ 0

        // Nếu tìm kiếm, gọi phương thức tìm kiếm với phân trang
        if (keyword != null && column != null && !keyword.isEmpty()) {
            return searchPlayersByColumnWithPagination(column, keyword, pageRequest);
        }

        // Nếu không tìm kiếm, trả về danh sách phân trang và sắp xếp toàn bộ
        return playerRepository.findAll(pageRequest);
    }

    public Page<Player> searchPlayersByColumnWithPagination(String column, String keyword, PageRequest pageRequest) {
        switch (column) {
            case "name":
                return playerRepository.findByNameContainingIgnoreCase(keyword, pageRequest);
            case "country":
                return playerRepository.findByCountryContainingIgnoreCase(keyword, pageRequest);
            case "position":
                return playerRepository.findByPositionContainingIgnoreCase(keyword, pageRequest);
            case "age":
                try {
                    int age = Integer.parseInt(keyword); // Trường hợp từ khóa là tuổi
                    return playerRepository.findByAge(age, pageRequest);
                } catch (NumberFormatException e) {
                    return Page.empty(); // Nếu từ khóa không phải số
                }
            default:
                return Page.empty();
        }
    }

}
