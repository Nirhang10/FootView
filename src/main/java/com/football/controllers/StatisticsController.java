package com.football.controllers;

import com.football.dao.MatchDAO;
import com.football.dao.PlayerDAO;
import com.football.dao.TeamDAO;
import com.football.models.Match;
import com.football.models.Player;
import com.football.models.Team;
import com.football.utils.AlertUtil;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StatisticsController {

    private TableView<Team> standingsTable;
    private TableColumn<Team, String> teamNameCol;
    private TableColumn<Team, Integer> mpCol;
    private TableColumn<Team, Integer> wCol;
    private TableColumn<Team, Integer> dCol;
    private TableColumn<Team, Integer> lCol;
    private TableColumn<Team, Integer> gfCol;
    private TableColumn<Team, Integer> gaCol;
    private TableColumn<Team, Integer> gdCol;
    private TableColumn<Team, Integer> ptsCol;

    private TableView<Player> topScorersTable;
    private TableColumn<Player, String> scorerNameCol;
    private TableColumn<Player, String> scorerTeamCol;
    private TableColumn<Player, Integer> goalsCol;
    private TableColumn<Player, Integer> assistsCol;

    private TeamDAO teamDAO = new TeamDAO();
    private PlayerDAO playerDAO = new PlayerDAO();

    public void initialize(URL url, ResourceBundle rb) {
        setupTables();
        loadData();
    }

    private void setupTables() {
        if (teamNameCol != null) teamNameCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        if (mpCol != null) mpCol.setCellValueFactory(new PropertyValueFactory<>("matchesPlayed"));
        if (wCol != null) wCol.setCellValueFactory(new PropertyValueFactory<>("wins"));
        if (dCol != null) dCol.setCellValueFactory(new PropertyValueFactory<>("draws"));
        if (lCol != null) lCol.setCellValueFactory(new PropertyValueFactory<>("losses"));
        if (gfCol != null) gfCol.setCellValueFactory(new PropertyValueFactory<>("goalsFor"));
        if (gaCol != null) gaCol.setCellValueFactory(new PropertyValueFactory<>("goalsAgainst"));
        if (gdCol != null) gdCol.setCellValueFactory(new PropertyValueFactory<>("goalDifference"));
        if (ptsCol != null) ptsCol.setCellValueFactory(new PropertyValueFactory<>("points"));

        if (scorerNameCol != null) scorerNameCol.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        if (scorerTeamCol != null) scorerTeamCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        if (goalsCol != null) goalsCol.setCellValueFactory(new PropertyValueFactory<>("goals"));
        if (assistsCol != null) assistsCol.setCellValueFactory(new PropertyValueFactory<>("assists"));
    }

    private void loadData() {
        try {
            if (standingsTable != null) {
                List<Team> standings = teamDAO.getStandings();
                standingsTable.setItems(FXCollections.observableArrayList(standings));
            }
            if (topScorersTable != null) {
                List<Player> scorers = playerDAO.getTopScorers(10);
                topScorersTable.setItems(FXCollections.observableArrayList(scorers));
            }
        } catch (Exception e) {
            AlertUtil.showError("Error", "Failed to load statistics: " + e.getMessage());
        }
    }
}
