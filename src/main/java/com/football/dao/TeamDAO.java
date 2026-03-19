package com.football.dao;

import com.football.models.Team;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO {

    private Connection connection;

    public TeamDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Team> getAllTeams() {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT * FROM teams ORDER BY team_name";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                teams.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all teams: " + e.getMessage());
        }
        return teams;
    }

    public Team getTeamById(int teamId) {
        String sql = "SELECT * FROM teams WHERE team_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, teamId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return mapResultSet(rs);
        } catch (SQLException e) {
            System.err.println("Error getting team by ID: " + e.getMessage());
        }
        return null;
    }

    public boolean addTeam(Team team) {
        String sql = "INSERT INTO teams (team_name, founded_year, stadium, coach, city, logo_url) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, team.getTeamName());
            stmt.setInt(2, team.getFoundedYear());
            stmt.setString(3, team.getStadium());
            stmt.setString(4, team.getCoach());
            stmt.setString(5, team.getCity());
            stmt.setString(6, team.getLogoUrl());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding team: " + e.getMessage());
        }
        return false;
    }

    public boolean updateTeam(Team team) {
        String sql = "UPDATE teams SET team_name=?, founded_year=?, stadium=?, coach=?, city=?, logo_url=?, points=? WHERE team_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, team.getTeamName());
            stmt.setInt(2, team.getFoundedYear());
            stmt.setString(3, team.getStadium());
            stmt.setString(4, team.getCoach());
            stmt.setString(5, team.getCity());
            stmt.setString(6, team.getLogoUrl());
            stmt.setInt(7, team.getPoints());
            stmt.setInt(8, team.getTeamId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating team: " + e.getMessage());
        }
        return false;
    }

    public boolean updateTeamStatistics(int teamId, int wins, int draws, int losses, int goalsFor, int goalsAgainst,
            int points) {
        String sql = "UPDATE teams SET wins=?, draws=?, losses=?, goals_for=?, goals_against=?, points=? WHERE team_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, wins);
            stmt.setInt(2, draws);
            stmt.setInt(3, losses);
            stmt.setInt(4, goalsFor);
            stmt.setInt(5, goalsAgainst);
            stmt.setInt(6, points);
            stmt.setInt(7, teamId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating team statistics: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteTeam(int teamId) {
        String sql = "DELETE FROM teams WHERE team_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, teamId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting team: " + e.getMessage());
        }
        return false;
    }

    public int getTeamCount() {
        String sql = "SELECT COUNT(*) FROM teams";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error getting team count: " + e.getMessage());
        }
        return 0;
    }

    public List<Team> getStandings() {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT * FROM teams ORDER BY points DESC, (goals_for - goals_against) DESC, goals_for DESC";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                teams.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting standings: " + e.getMessage());
        }
        return teams;
    }

    private Team mapResultSet(ResultSet rs) throws SQLException {
        Team team = new Team();
        team.setTeamId(rs.getInt("team_id"));
        team.setTeamName(rs.getString("team_name"));
        team.setFoundedYear(rs.getInt("founded_year"));
        team.setStadium(rs.getString("stadium"));
        team.setCoach(rs.getString("coach"));
        team.setCity(rs.getString("city"));
        team.setLogoUrl(rs.getString("logo_url"));
        team.setWins(rs.getInt("wins"));
        team.setDraws(rs.getInt("draws"));
        team.setLosses(rs.getInt("losses"));
        team.setGoalsFor(rs.getInt("goals_for"));
        team.setGoalsAgainst(rs.getInt("goals_against"));
        team.setPoints(rs.getInt("points"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null)
            team.setCreatedAt(createdAt.toLocalDateTime());
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null)
            team.setUpdatedAt(updatedAt.toLocalDateTime());
        return team;
    }
}
