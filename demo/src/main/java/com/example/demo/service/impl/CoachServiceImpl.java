package com.example.demo.service.impl;

import com.example.demo.entity.Coach;
import com.example.demo.repository.CoachRepository;
import com.example.demo.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CoachServiceImpl implements CoachService {
    @Autowired
    CoachRepository coachRepository;


    @Override
    public void saveCoach(Coach coach) {
        coachRepository.save(coach);
    }

    @Override
    public void deleteCoach(Coach coach) {
        coachRepository.delete(coach);
    }


    @Override
    public List<Coach> findAllCoach() {
        List<Coach> coachList = coachRepository.findAll();
        return coachList;
    }

    @Override
    public Coach findById(int idCoach) {
        return coachRepository.findById(idCoach).orElse(null);
    }
}
