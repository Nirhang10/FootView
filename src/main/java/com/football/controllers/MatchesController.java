package com.football.controllers;

import com.football.dao.MatchDAO;
import com.football.dao.TeamDAO;
import com.football.models.Match;
import com.football.models.Team;
import com.football.utils.AlertUtil;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MatchesController {

    private TableView<Match> matchesTable;
    private TableColumn<Match, String> dateCol;
    private TableColumn<Match, String> homeTeamCol;
    private TableColumn<Match, String> scoreCol;
    private TableColumn<Match, String> awayTeamCol;
    private TableColumn<Match, String> statusCol;

    private MatchDAO matchDAO = new MatchDAO();
    private TeamDAO teamDAO = new TeamDAO();

    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        loadMatches();
    }

    private void setupTable() {
        if (dateCol != null) dateCol.setCellValueFactory(new PropertyValueFactory<>("matchDate"));
        if (homeTeamCol != null) homeTeamCol.setCellValueFactory(new PropertyValueFactory<>("homeTeamName"));
        if (scoreCol != null) scoreCol.setCellValueFactory(new PropertyValueFactory<>("scoreDisplay"));
        if (awayTeamCol != null) awayTeamCol.setCellValueFactory(new PropertyValueFactory<>("awayTeamName"));
        if (statusCol != null) statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadMatches() {
        List<Match> matches = matchDAO.getAllMatches();
        matchesTable.setItems(FXCollections.observableArrayList(matches));
    }

    private void handleScheduleMatch() {
        List<Team> teams = teamDAO.getAllTeams();
        if (teams.size() < 2) {
            AlertUtil.showWarning("Warning", "At least 2 teams are needed to schedule a match.");
            return;
        }

        Dialog<Match> dialog = new Dialog<>();
        dialog.setTitle("Schedule Match");

        ButtonType saveButtonType = new ButtonType("Schedule", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        ComboBox<Team> homeTeamBox = new ComboBox<>(FXCollections.observableArrayList(teams));
        ComboBox<Team> awayTeamBox = new ComboBox<>(FXCollections.observableArrayList(teams));
        DatePicker datePicker = new DatePicker(LocalDate.now().plusDays(7));

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Home Team:"), 0, 0); grid.add(homeTeamBox, 1, 0);
        grid.add(new Label("Away Team:"), 0, 1); grid.add(awayTeamBox, 1, 1);
        grid.add(new Label("Match Date:"), 0, 2); grid.add(datePicker, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Team home = homeTeamBox.getValue();
                Team away = awayTeamBox.getValue();
                if (home == null || away == null || home.getTeamId() == away.getTeamId()) return null;
                Match match = new Match();
                match.setHomeTeamId(home.getTeamId());
                match.setAwayTeamId(away.getTeamId());
                match.setMatchDate(datePicker.getValue());
                match.setStatus(Match.Status.SCHEDULED);
                return match;
            }
            return null;
        });

        Optional<Match> result = dialog.showAndWait();
        result.ifPresent(match -> {
            if (matchDAO.addMatch(match)) {
                AlertUtil.showInfo("Success", "Match scheduled successfully.");
                loadMatches();
            } else {
                AlertUtil.showError("Error", "Failed to schedule match.");
            }
        });
    }

    private void handleUpdateScore() {
        Match selected = matchesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("No Selection", "Please select a match to update.");
            return;
        }

        Dialog<int[]> dialog = new Dialog<>();
        dialog.setTitle("Update Score");

        ButtonType saveButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextField homeScoreField = new TextField(String.valueOf(selected.getHomeScore()));
        TextField awayScoreField = new TextField(String.valueOf(selected.getAwayScore()));
        ComboBox<Match.Status> statusBox = new ComboBox<>(FXCollections.observableArrayList(Match.Status.values()));
        statusBox.setValue(selected.getStatus());

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label(selected.getHomeTeamName() + " Score:"), 0, 0); grid.add(homeScoreField, 1, 0);
        grid.add(new Label(selected.getAwayTeamName() + " Score:"), 0, 1); grid.add(awayScoreField, 1, 1);
        grid.add(new Label("Status:"), 0, 2); grid.add(statusBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    return new int[]{
                        Integer.parseInt(homeScoreField.getText().trim()),
                        Integer.parseInt(awayScoreField.getText().trim()),
                        statusBox.getValue().ordinal()
                    };
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        Optional<int[]> result = dialog.showAndWait();
        result.ifPresent(scores -> {
            Match.Status newStatus = Match.Status.values()[scores[2]];
            if (matchDAO.updateScore(selected.getMatchId(), scores[0], scores[1], newStatus)) {
                AlertUtil.showInfo("Success", "Score updated successfully.");
                loadMatches();
            } else {
                AlertUtil.showError("Error", "Failed to update score.");
            }
        });
    }

    private void handleDeleteMatch() {
        Match selected = matchesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("No Selection", "Please select a match to delete.");
            return;
        }
        boolean confirmed = AlertUtil.showConfirmation("Confirm Delete",
                "Are you sure you want to delete this match?");
        if (confirmed) {
            if (matchDAO.deleteMatch(selected.getMatchId())) {
                AlertUtil.showInfo("Success", "Match deleted successfully.");
                loadMatches();
            } else {
                AlertUtil.showError("Error", "Failed to delete match.");
            }
        }
    }
}
