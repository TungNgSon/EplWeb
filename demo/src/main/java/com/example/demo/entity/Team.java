package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Data
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;
    @Column
    private String country;
    @Column
    private int ranked;
    @Column
    private String imageURL;
    @Column
    private String videoURL;
    @Column(length=5000)
    private String locationURL;
    public double getLatitude() {
        return extractCoordinate("latitude");
    }

    public double getLongitude() {
        return extractCoordinate("longitude");
    }

    private double extractCoordinate(String type) {
        String regex = "!2d(-?\\d+\\.\\d+)!3d(-?\\d+\\.\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(locationURL);
        if (matcher.find()) {
            if ("latitude".equals(type)) {
                return Double.parseDouble(matcher.group(1));
            } else if ("longitude".equals(type)) {
                return Double.parseDouble(matcher.group(2));
            }
        }
        throw new IllegalArgumentException("Invalid Google Maps URL");
    }
    @OneToMany (mappedBy = "team")
    private List<Player> players=new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "coach_id")
    private Coach coach;

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getLocationURL() {
        return locationURL;
    }

    public void setLocationURL(String locationURL) {
        this.locationURL = locationURL;
    }

    public int getRanked() {
        return ranked;
    }

    public void setRanked(int ranked) {
        this.ranked = ranked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
