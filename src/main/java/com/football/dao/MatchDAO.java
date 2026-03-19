package com.football.dao;

import com.football.models.Match;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MatchDAO {

    private Connection connection;

    public MatchDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Match> getAllMatches() {
        List<Match> matches = new ArrayList<>();
        String sql = "SELECT m.*, ht.team_name AS home_team_name, at.team_name AS away_team_name " +
                     "FROM matches m " +
                     "JOIN teams ht ON m.home_team_id = ht.team_id " +
                     "JOIN teams at ON m.away_team_id = at.team_id " +
                     "ORDER BY m.match_date DESC, m.match_time DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                matches.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all matches: " + e.getMessage());
        }
        return matches;
    }

    public List<Match> getMatchesByStatus(Match.Status status) {
        List<Match> matches = new ArrayList<>();
        String sql = "SELECT m.*, ht.team_name AS home_team_name, at.team_name AS away_team_name " +
                     "FROM matches m " +
                     "JOIN teams ht ON m.home_team_id = ht.team_id " +
                     "JOIN teams at ON m.away_team_id = at.team_id " +
                     "WHERE m.status = ? " +
                     "ORDER BY m.match_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                matches.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting matches by status: " + e.getMessage());
        }
        return matches;
    }

    public boolean addMatch(Match match) {
        String sql = "INSERT INTO matches (home_team_id, away_team_id, match_date, match_time, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, match.getHomeTeamId());
            stmt.setInt(2, match.getAwayTeamId());
            stmt.setObject(3, match.getMatchDate());
            stmt.setObject(4, match.getMatchTime());
            stmt.setString(5, match.getStatus().name());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding match: " + e.getMessage());
        }
        return false;
    }

    public boolean updateMatch(Match match) {
        String sql = "UPDATE matches SET home_team_id=?, away_team_id=?, match_date=?, match_time=?, home_score=?, away_score=?, status=? WHERE match_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, match.getHomeTeamId());
            stmt.setInt(2, match.getAwayTeamId());
            stmt.setObject(3, match.getMatchDate());
            stmt.setObject(4, match.getMatchTime());
            stmt.setInt(5, match.getHomeScore());
            stmt.setInt(6, match.getAwayScore());
            stmt.setString(7, match.getStatus().name());
            stmt.setInt(8, match.getMatchId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating match: " + e.getMessage());
        }
        return false;
    }

    public boolean updateScore(int matchId, int homeScore, int awayScore, Match.Status status) {
        String sql = "UPDATE matches SET home_score=?, away_score=?, status=? WHERE match_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, homeScore);
            stmt.setInt(2, awayScore);
            stmt.setString(3, status.name());
            stmt.setInt(4, matchId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating score: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteMatch(int matchId) {
        String sql = "DELETE FROM matches WHERE match_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, matchId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting match: " + e.getMessage());
        }
        return false;
    }

    public int getMatchCount() {
        String sql = "SELECT COUNT(*) FROM matches";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error getting match count: " + e.getMessage());
        }
        return 0;
    }

    public List<Match> getUpcomingMatches() {
        return getMatchesByStatus(Match.Status.SCHEDULED);
    }

    public List<Match> getRecentResults() {
        return getMatchesByStatus(Match.Status.COMPLETED);
    }

    private Match mapResultSet(ResultSet rs) throws SQLException {
        Match match = new Match();
        match.setMatchId(rs.getInt("match_id"));
        match.setHomeTeamId(rs.getInt("home_team_id"));
        match.setAwayTeamId(rs.getInt("away_team_id"));
        Date matchDate = rs.getDate("match_date");
        if (matchDate != null) match.setMatchDate(matchDate.toLocalDate());
        Time matchTime = rs.getTime("match_time");
        if (matchTime != null) match.setMatchTime(matchTime.toLocalTime());
        match.setHomeScore(rs.getInt("home_score"));
        match.setAwayScore(rs.getInt("away_score"));
        match.setStatus(Match.Status.valueOf(rs.getString("status").toUpperCase()));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) match.setCreatedAt(createdAt.toLocalDateTime());
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) match.setUpdatedAt(updatedAt.toLocalDateTime());
        match.setHomeTeamName(rs.getString("home_team_name"));
        match.setAwayTeamName(rs.getString("away_team_name"));
        return match;
    }
}
