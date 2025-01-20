package com.example.demo.service;

import com.example.demo.entity.Coach;

import java.util.List;

public interface CoachService {
    void saveCoach(Coach coach);
    void deleteCoach(Coach coach);
    List<Coach> findAllCoach();
    Coach findById(int idCoach);


}
