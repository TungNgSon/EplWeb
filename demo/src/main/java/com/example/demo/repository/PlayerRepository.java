package com.example.demo.repository;

import com.example.demo.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Integer> {
public List<Player> findByNameContainingIgnoreCase(String name);
public List<Player> findByCountryContainingIgnoreCase(String name);
public List<Player> findByPositionContainingIgnoreCase(String name);
public List<Player> findByAge(int age);
Page<Player> findByNameContainingIgnoreCase(String name, Pageable pageable);
Page<Player> findByCountryContainingIgnoreCase(String country, Pageable pageable);
Page<Player> findByPositionContainingIgnoreCase(String position, Pageable pageable);
Page<Player> findByAge(int age, Pageable pageable);


}
