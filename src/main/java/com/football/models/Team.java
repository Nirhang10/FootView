package com.football.models;

import java.time.LocalDateTime;

public class Team {

    private int teamId;
    private String teamName;
    private int foundedYear;
    private String stadium;
    private String coach;
    private String city;
    private String logoUrl;
    private int wins;
    private int draws;
    private int losses;
    private int goalsFor;
    private int goalsAgainst;
    private int points;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Team() {}

    public Team(int teamId, String teamName, int foundedYear, String stadium, String coach,
                String city, String logoUrl, int wins, int draws, int losses,
                int goalsFor, int goalsAgainst, int points,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.foundedYear = foundedYear;
        this.stadium = stadium;
        this.coach = coach;
        this.city = city;
        this.logoUrl = logoUrl;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        this.points = points;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getMatchesPlayed() {
        return wins + draws + losses;
    }

    public int getGoalDifference() {
        return goalsFor - goalsAgainst;
    }

    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public int getFoundedYear() { return foundedYear; }
    public void setFoundedYear(int foundedYear) { this.foundedYear = foundedYear; }

    public String getStadium() { return stadium; }
    public void setStadium(String stadium) { this.stadium = stadium; }

    public String getCoach() { return coach; }
    public void setCoach(String coach) { this.coach = coach; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public int getWins() { return wins; }
    public void setWins(int wins) { this.wins = wins; }

    public int getDraws() { return draws; }
    public void setDraws(int draws) { this.draws = draws; }

    public int getLosses() { return losses; }
    public void setLosses(int losses) { this.losses = losses; }

    public int getGoalsFor() { return goalsFor; }
    public void setGoalsFor(int goalsFor) { this.goalsFor = goalsFor; }

    public int getGoalsAgainst() { return goalsAgainst; }
    public void setGoalsAgainst(int goalsAgainst) { this.goalsAgainst = goalsAgainst; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return teamName;
    }
}
