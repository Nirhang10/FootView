package com.football.controllers;

import com.football.dao.TeamDAO;
import com.football.models.Team;
import com.football.utils.AlertUtil;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TeamsController {

    private TableView<Team> teamsTable;
    private TableColumn<Team, String> nameCol;
    private TableColumn<Team, String> stadiumCol;
    private TableColumn<Team, String> coachCol;
    private TableColumn<Team, String> cityCol;
    private TableColumn<Team, Integer> winsCol;
    private TableColumn<Team, Integer> drawsCol;
    private TableColumn<Team, Integer> lossesCol;
    private TableColumn<Team, Integer> pointsCol;

    private TeamDAO teamDAO = new TeamDAO();

    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        loadTeams();
    }

    private void setupTable() {
        if (nameCol != null) nameCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        if (stadiumCol != null) stadiumCol.setCellValueFactory(new PropertyValueFactory<>("stadium"));
        if (coachCol != null) coachCol.setCellValueFactory(new PropertyValueFactory<>("coach"));
        if (cityCol != null) cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        if (winsCol != null) winsCol.setCellValueFactory(new PropertyValueFactory<>("wins"));
        if (drawsCol != null) drawsCol.setCellValueFactory(new PropertyValueFactory<>("draws"));
        if (lossesCol != null) lossesCol.setCellValueFactory(new PropertyValueFactory<>("losses"));
        if (pointsCol != null) pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));
    }

    private void loadTeams() {
        List<Team> teams = teamDAO.getAllTeams();
        teamsTable.setItems(FXCollections.observableArrayList(teams));
    }

    private void handleAddTeam() {
        Team team = showTeamDialog(null);
        if (team != null) {
            if (teamDAO.addTeam(team)) {
                AlertUtil.showInfo("Success", "Team added successfully.");
                loadTeams();
            } else {
                AlertUtil.showError("Error", "Failed to add team.");
            }
        }
    }

    private void handleEditTeam() {
        Team selected = teamsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("No Selection", "Please select a team to edit.");
            return;
        }
        Team updated = showTeamDialog(selected);
        if (updated != null) {
            updated.setTeamId(selected.getTeamId());
            if (teamDAO.updateTeam(updated)) {
                AlertUtil.showInfo("Success", "Team updated successfully.");
                loadTeams();
            } else {
                AlertUtil.showError("Error", "Failed to update team.");
            }
        }
    }

    private void handleDeleteTeam() {
        Team selected = teamsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("No Selection", "Please select a team to delete.");
            return;
        }
        boolean confirmed = AlertUtil.showConfirmation("Confirm Delete",
                "Are you sure you want to delete " + selected.getTeamName() + "?");
        if (confirmed) {
            if (teamDAO.deleteTeam(selected.getTeamId())) {
                AlertUtil.showInfo("Success", "Team deleted successfully.");
                loadTeams();
            } else {
                AlertUtil.showError("Error", "Failed to delete team.");
            }
        }
    }

    private Team showTeamDialog(Team existing) {
        Dialog<Team> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Add Team" : "Edit Team");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField(existing != null ? existing.getTeamName() : "");
        TextField stadiumField = new TextField(existing != null ? existing.getStadium() : "");
        TextField coachField = new TextField(existing != null ? existing.getCoach() : "");
        TextField cityField = new TextField(existing != null ? existing.getCity() : "");
        TextField foundedField = new TextField(existing != null ? String.valueOf(existing.getFoundedYear()) : "");

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Team Name:"), 0, 0); grid.add(nameField, 1, 0);
        grid.add(new Label("Stadium:"), 0, 1); grid.add(stadiumField, 1, 1);
        grid.add(new Label("Coach:"), 0, 2); grid.add(coachField, 1, 2);
        grid.add(new Label("City:"), 0, 3); grid.add(cityField, 1, 3);
        grid.add(new Label("Founded Year:"), 0, 4); grid.add(foundedField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Team team = new Team();
                team.setTeamName(nameField.getText().trim());
                team.setStadium(stadiumField.getText().trim());
                team.setCoach(coachField.getText().trim());
                team.setCity(cityField.getText().trim());
                try {
                    team.setFoundedYear(Integer.parseInt(foundedField.getText().trim()));
                } catch (NumberFormatException e) {
                    team.setFoundedYear(0);
                }
                return team;
            }
            return null;
        });

        Optional<Team> result = dialog.showAndWait();
        return result.orElse(null);
    }
}
