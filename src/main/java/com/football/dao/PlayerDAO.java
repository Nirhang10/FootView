package com.football.dao;

import com.football.models.Player;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {

    private Connection connection;

    public PlayerDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT p.*, t.team_name FROM players p LEFT JOIN teams t ON p.team_id = t.team_id ORDER BY p.player_name";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                players.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all players: " + e.getMessage());
        }
        return players;
    }

    public List<Player> getPlayersByTeam(int teamId) {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT p.*, t.team_name FROM players p LEFT JOIN teams t ON p.team_id = t.team_id WHERE p.team_id = ? ORDER BY p.player_name";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, teamId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                players.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting players by team: " + e.getMessage());
        }
        return players;
    }

    public boolean addPlayer(Player player) {
        String sql = "INSERT INTO players (team_id, player_name, position, jersey_number, date_of_birth, nationality, height, weight, matches_played, goals, assists) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, player.getTeamId());
            stmt.setString(2, player.getPlayerName());
            stmt.setString(3, player.getPosition().name());
            stmt.setInt(4, player.getJerseyNumber());
            stmt.setObject(5, player.getDateOfBirth());
            stmt.setString(6, player.getNationality());
            stmt.setDouble(7, player.getHeight());
            stmt.setDouble(8, player.getWeight());
            stmt.setInt(9, player.getMatchesPlayed());
            stmt.setInt(10, player.getGoals());
            stmt.setInt(11, player.getAssists());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding player: " + e.getMessage());
        }
        return false;
    }

    public boolean updatePlayerPerformance(int playerId, int goals, int assists, int matchesPlayed) {
        String sql = "UPDATE players SET goals=?, assists=?, matches_played=? WHERE player_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, goals);
            stmt.setInt(2, assists);
            stmt.setInt(3, matchesPlayed);
            stmt.setInt(4, playerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating player performance: " + e.getMessage());
        }
        return false;
    }

    public boolean updatePlayer(Player player) {
        String sql = "UPDATE players SET team_id=?, player_name=?, position=?, jersey_number=?, date_of_birth=?, nationality=?, height=?, weight=?, matches_played=?, goals=?, assists=?, yellow_cards=?, red_cards=? WHERE player_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, player.getTeamId());
            stmt.setString(2, player.getPlayerName());
            stmt.setString(3, player.getPosition().name());
            stmt.setInt(4, player.getJerseyNumber());
            stmt.setObject(5, player.getDateOfBirth());
            stmt.setString(6, player.getNationality());
            stmt.setDouble(7, player.getHeight());
            stmt.setDouble(8, player.getWeight());
            stmt.setInt(9, player.getMatchesPlayed());
            stmt.setInt(10, player.getGoals());
            stmt.setInt(11, player.getAssists());
            stmt.setInt(12, player.getYellowCards());
            stmt.setInt(13, player.getRedCards());
            stmt.setInt(14, player.getPlayerId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating player: " + e.getMessage());
        }
        return false;
    }

    public boolean deletePlayer(int playerId) {
        String sql = "DELETE FROM players WHERE player_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, playerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting player: " + e.getMessage());
        }
        return false;
    }

    public int getPlayerCount() {
        String sql = "SELECT COUNT(*) FROM players";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error getting player count: " + e.getMessage());
        }
        return 0;
    }

    public List<Player> getTopScorers(int limit) {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT p.*, t.team_name FROM players p LEFT JOIN teams t ON p.team_id = t.team_id ORDER BY p.goals DESC LIMIT ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                players.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting top scorers: " + e.getMessage());
        }
        return players;
    }

    private Player mapResultSet(ResultSet rs) throws SQLException {
        Player player = new Player();
        player.setPlayerId(rs.getInt("player_id"));
        player.setTeamId(rs.getInt("team_id"));
        player.setPlayerName(rs.getString("player_name"));
        player.setPosition(parsePosition(rs.getString("position")));
        player.setJerseyNumber(rs.getInt("jersey_number"));
        Date dob = rs.getDate("date_of_birth");
        if (dob != null)
            player.setDateOfBirth(dob.toLocalDate());
        player.setNationality(rs.getString("nationality"));
        player.setHeight(rs.getDouble("height"));
        player.setWeight(rs.getDouble("weight"));
        player.setMatchesPlayed(rs.getInt("matches_played"));
        player.setGoals(rs.getInt("goals"));
        player.setAssists(rs.getInt("assists"));
        player.setYellowCards(rs.getInt("yellow_cards"));
        player.setRedCards(rs.getInt("red_cards"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null)
            player.setCreatedAt(createdAt.toLocalDateTime());
        try {
            player.setTeamName(rs.getString("team_name"));
        } catch (SQLException ignored) {
        }
        return player;
    }

    private Player.Position parsePosition(String rawPosition) {
        if (rawPosition == null) {
            return Player.Position.Forward;
        }

        // Normalize DB values like FORWARD, forward, or MID_FIELDER to enum names.
        String normalized = rawPosition.trim().replace('-', '_').replace(' ', '_');
        for (Player.Position position : Player.Position.values()) {
            if (position.name().equalsIgnoreCase(normalized)) {
                return position;
            }
        }

        System.err.println("Unknown player position from DB: " + rawPosition + ". Defaulting to Forward.");
        return Player.Position.Forward;
    }
}
