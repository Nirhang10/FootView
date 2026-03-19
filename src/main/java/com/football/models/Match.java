package com.football.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Match {

    public enum Status {
        SCHEDULED, IN_PROGRESS, COMPLETED, POSTPONED, CANCELLED
    }

    private int matchId;
    private int homeTeamId;
    private int awayTeamId;
    private LocalDate matchDate;
    private LocalTime matchTime;
    private int homeScore;
    private int awayScore;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String homeTeamName;
    private String awayTeamName;

    public Match() {}

    public Match(int matchId, int homeTeamId, int awayTeamId, LocalDate matchDate,
                 LocalTime matchTime, int homeScore, int awayScore, Status status,
                 LocalDateTime createdAt, LocalDateTime updatedAt,
                 String homeTeamName, String awayTeamName) {
        this.matchId = matchId;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.matchDate = matchDate;
        this.matchTime = matchTime;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
    }

    public String getResult() {
        if (status != Status.COMPLETED) return "N/A";
        if (homeScore > awayScore) return homeTeamName + " Win";
        if (awayScore > homeScore) return awayTeamName + " Win";
        return "Draw";
    }

    public String getScoreDisplay() {
        if (status == Status.SCHEDULED || status == Status.POSTPONED || status == Status.CANCELLED) {
            return "vs";
        }
        return homeScore + " - " + awayScore;
    }

    public int getMatchId() { return matchId; }
    public void setMatchId(int matchId) { this.matchId = matchId; }

    public int getHomeTeamId() { return homeTeamId; }
    public void setHomeTeamId(int homeTeamId) { this.homeTeamId = homeTeamId; }

    public int getAwayTeamId() { return awayTeamId; }
    public void setAwayTeamId(int awayTeamId) { this.awayTeamId = awayTeamId; }

    public LocalDate getMatchDate() { return matchDate; }
    public void setMatchDate(LocalDate matchDate) { this.matchDate = matchDate; }

    public LocalTime getMatchTime() { return matchTime; }
    public void setMatchTime(LocalTime matchTime) { this.matchTime = matchTime; }

    public int getHomeScore() { return homeScore; }
    public void setHomeScore(int homeScore) { this.homeScore = homeScore; }

    public int getAwayScore() { return awayScore; }
    public void setAwayScore(int awayScore) { this.awayScore = awayScore; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getHomeTeamName() { return homeTeamName; }
    public void setHomeTeamName(String homeTeamName) { this.homeTeamName = homeTeamName; }

    public String getAwayTeamName() { return awayTeamName; }
    public void setAwayTeamName(String awayTeamName) { this.awayTeamName = awayTeamName; }

    @Override
    public String toString() {
        return homeTeamName + " vs " + awayTeamName + " (" + matchDate + ")";
    }
}
