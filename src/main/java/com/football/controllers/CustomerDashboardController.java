package com.football.controllers;

import com.football.dao.MatchDAO;
import com.football.dao.TeamDAO;
import com.football.dao.PlayerDAO;
import com.football.models.Match;
import com.football.models.Team;
import com.football.models.Player;
import com.football.ui.AppUI;
import com.football.utils.AlertUtil;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerDashboardController {

    private TableView<Team> teamsTable;
    private TableColumn<Team, String> teamNameCol;
    private TableColumn<Team, String> stadiumCol;
    private TableColumn<Team, String> coachCol;
    private TableColumn<Team, String> cityCol;

    private TableView<Match> matchesTable;
    private TableColumn<Match, String> matchDateCol;
    private TableColumn<Match, String> homeTeamCol;
    private TableColumn<Match, String> scoreCol;
    private TableColumn<Match, String> awayTeamCol;
    private TableColumn<Match, String> matchStatusCol;

    private TableView<Team> standingsTable;
    private TableColumn<Team, Integer> positionCol;
    private TableColumn<Team, String> standingTeamCol;
    private TableColumn<Team, Integer> winsCol;
    private TableColumn<Team, Integer> drawsCol;
    private TableColumn<Team, Integer> lossesCol;
    private TableColumn<Team, Integer> pointsCol;

    private TableView<Player> topScorersTable;
    private TableColumn<Player, String> scorerNameCol;
    private TableColumn<Player, String> scorerTeamCol;
    private TableColumn<Player, Integer> goalsCol;
    private TableColumn<Player, Integer> assistsCol;

    private TeamDAO teamDAO = new TeamDAO();
    private MatchDAO matchDAO = new MatchDAO();
    private PlayerDAO playerDAO = new PlayerDAO();

    public void initialize(URL url, ResourceBundle rb) {
        setupTeamsTable();
        setupMatchesTable();
        setupStandingsTable();
        setupTopScorersTable();
        loadData();
    }

    private void setupTeamsTable() {
        if (teamNameCol != null)
            teamNameCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        if (stadiumCol != null)
            stadiumCol.setCellValueFactory(new PropertyValueFactory<>("stadium"));
        if (coachCol != null)
            coachCol.setCellValueFactory(new PropertyValueFactory<>("coach"));
        if (cityCol != null)
            cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
    }

    private void setupMatchesTable() {
        if (matchDateCol != null)
            matchDateCol.setCellValueFactory(new PropertyValueFactory<>("matchDate"));
        if (homeTeamCol != null)
            homeTeamCol.setCellValueFactory(new PropertyValueFactory<>("homeTeamName"));
        if (scoreCol != null)
            scoreCol.setCellValueFactory(new PropertyValueFactory<>("scoreDisplay"));
        if (awayTeamCol != null)
            awayTeamCol.setCellValueFactory(new PropertyValueFactory<>("awayTeamName"));
        if (matchStatusCol != null)
            matchStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setupStandingsTable() {
        if (standingTeamCol != null)
            standingTeamCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        if (winsCol != null)
            winsCol.setCellValueFactory(new PropertyValueFactory<>("wins"));
        if (drawsCol != null)
            drawsCol.setCellValueFactory(new PropertyValueFactory<>("draws"));
        if (lossesCol != null)
            lossesCol.setCellValueFactory(new PropertyValueFactory<>("losses"));
        if (pointsCol != null)
            pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));
    }

    private void setupTopScorersTable() {
        if (scorerNameCol != null)
            scorerNameCol.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        if (scorerTeamCol != null)
            scorerTeamCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        if (goalsCol != null)
            goalsCol.setCellValueFactory(new PropertyValueFactory<>("goals"));
        if (assistsCol != null)
            assistsCol.setCellValueFactory(new PropertyValueFactory<>("assists"));
    }

    private void loadData() {
        try {
            if (teamsTable != null) {
                List<Team> teams = teamDAO.getAllTeams();
                teamsTable.setItems(FXCollections.observableArrayList(teams));
            }
            if (matchesTable != null) {
                List<Match> matches = matchDAO.getAllMatches();
                matchesTable.setItems(FXCollections.observableArrayList(matches));
            }
            if (standingsTable != null) {
                List<Team> standings = teamDAO.getStandings();
                standingsTable.setItems(FXCollections.observableArrayList(standings));
            }
            if (topScorersTable != null) {
                List<Player> scorers = playerDAO.getTopScorers(10);
                topScorersTable.setItems(FXCollections.observableArrayList(scorers));
            }
        } catch (Exception e) {
            AlertUtil.showError("Error", "Failed to load data: " + e.getMessage());
        }
    }

    private void handleLogout() {
        Stage stage = (Stage) teamsTable.getScene().getWindow();
        new AppUI(stage).showLogin();
    }
}
