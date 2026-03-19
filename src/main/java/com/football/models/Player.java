package com.football.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Player {

    public enum Position {
        Goalkeeper, Defender, Midfielder, Forward
    }

    private int playerId;
    private int teamId;
    private String playerName;
    private Position position;
    private int jerseyNumber;
    private LocalDate dateOfBirth;
    private String nationality;
    private double height;
    private double weight;
    private int matchesPlayed;
    private int goals;
    private int assists;
    private int yellowCards;
    private int redCards;
    private LocalDateTime createdAt;
    private String teamName;

    public Player() {}

    public Player(int playerId, int teamId, String playerName, Position position,
                  int jerseyNumber, LocalDate dateOfBirth, String nationality,
                  double height, double weight, int matchesPlayed, int goals,
                  int assists, int yellowCards, int redCards,
                  LocalDateTime createdAt, String teamName) {
        this.playerId = playerId;
        this.teamId = teamId;
        this.playerName = playerName;
        this.position = position;
        this.jerseyNumber = jerseyNumber;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.height = height;
        this.weight = weight;
        this.matchesPlayed = matchesPlayed;
        this.goals = goals;
        this.assists = assists;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
        this.createdAt = createdAt;
        this.teamName = teamName;
    }

    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }

    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }

    public int getJerseyNumber() { return jerseyNumber; }
    public void setJerseyNumber(int jerseyNumber) { this.jerseyNumber = jerseyNumber; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public int getMatchesPlayed() { return matchesPlayed; }
    public void setMatchesPlayed(int matchesPlayed) { this.matchesPlayed = matchesPlayed; }

    public int getGoals() { return goals; }
    public void setGoals(int goals) { this.goals = goals; }

    public int getAssists() { return assists; }
    public void setAssists(int assists) { this.assists = assists; }

    public int getYellowCards() { return yellowCards; }
    public void setYellowCards(int yellowCards) { this.yellowCards = yellowCards; }

    public int getRedCards() { return redCards; }
    public void setRedCards(int redCards) { this.redCards = redCards; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    @Override
    public String toString() {
        return playerName + " (" + position + ")";
    }
}
