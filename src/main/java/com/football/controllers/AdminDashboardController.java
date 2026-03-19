package com.football.controllers;

import com.football.dao.MatchDAO;
import com.football.dao.PlayerDAO;
import com.football.dao.TeamDAO;
import com.football.dao.UserDAO;
import com.football.ui.AppUI;
import com.football.utils.AlertUtil;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardController {

    private BorderPane mainPane;
    private Label totalTeamsLabel;
    private Label totalMatchesLabel;
    private Label totalPlayersLabel;
    private Label totalUsersLabel;

    private TeamDAO teamDAO = new TeamDAO();
    private MatchDAO matchDAO = new MatchDAO();
    private PlayerDAO playerDAO = new PlayerDAO();
    private UserDAO userDAO = new UserDAO();

    public void initialize(URL url, ResourceBundle rb) {
        updateSummaryCards();
    }

    private void updateSummaryCards() {
        try {
            if (totalTeamsLabel != null)
                totalTeamsLabel.setText(String.valueOf(teamDAO.getTeamCount()));
            if (totalMatchesLabel != null)
                totalMatchesLabel.setText(String.valueOf(matchDAO.getMatchCount()));
            if (totalPlayersLabel != null)
                totalPlayersLabel.setText(String.valueOf(playerDAO.getPlayerCount()));
            if (totalUsersLabel != null)
                totalUsersLabel.setText(String.valueOf(userDAO.getUserCount()));
        } catch (Exception e) {
            System.err.println("Error loading summary: " + e.getMessage());
        }
    }

    private void showDashboard() {
        updateSummaryCards();
    }

    private void showTeams() {
        AlertUtil.showInfo("Info", "Team management is now available in the Java-only dashboard.");
    }

    private void showMatches() {
        AlertUtil.showInfo("Info", "Match management is now available in the Java-only dashboard.");
    }

    private void showPlayers() {
        AlertUtil.showInfo("Info", "Player management is now available in the Java-only dashboard.");
    }

    private void showUsers() {
        AlertUtil.showInfo("Info", "User management is now available in the Java-only dashboard.");
    }

    private void handleLogout() {
        Stage stage = (Stage) mainPane.getScene().getWindow();
        new AppUI(stage).showLogin();
    }
}
