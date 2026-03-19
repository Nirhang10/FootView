package com.football.controllers;

import com.football.dao.PlayerDAO;
import com.football.dao.TeamDAO;
import com.football.models.Player;
import com.football.models.Team;
import com.football.utils.AlertUtil;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PlayersController {

    private TableView<Player> playersTable;
    private TableColumn<Player, String> nameCol;
    private TableColumn<Player, String> teamCol;
    private TableColumn<Player, String> positionCol;
    private TableColumn<Player, Integer> jerseyCol;
    private TableColumn<Player, String> nationalityCol;
    private TableColumn<Player, Integer> goalsCol;
    private TableColumn<Player, Integer> assistsCol;

    private PlayerDAO playerDAO = new PlayerDAO();
    private TeamDAO teamDAO = new TeamDAO();

    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        loadPlayers();
    }

    private void setupTable() {
        if (nameCol != null) nameCol.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        if (teamCol != null) teamCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        if (positionCol != null) positionCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        if (jerseyCol != null) jerseyCol.setCellValueFactory(new PropertyValueFactory<>("jerseyNumber"));
        if (nationalityCol != null) nationalityCol.setCellValueFactory(new PropertyValueFactory<>("nationality"));
        if (goalsCol != null) goalsCol.setCellValueFactory(new PropertyValueFactory<>("goals"));
        if (assistsCol != null) assistsCol.setCellValueFactory(new PropertyValueFactory<>("assists"));
    }

    private void loadPlayers() {
        List<Player> players = playerDAO.getAllPlayers();
        playersTable.setItems(FXCollections.observableArrayList(players));
    }

    private void handleAddPlayer() {
        Player player = showPlayerDialog(null);
        if (player != null) {
            if (playerDAO.addPlayer(player)) {
                AlertUtil.showInfo("Success", "Player added successfully.");
                loadPlayers();
            } else {
                AlertUtil.showError("Error", "Failed to add player.");
            }
        }
    }

    private void handleEditPlayer() {
        Player selected = playersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("No Selection", "Please select a player to edit.");
            return;
        }
        Player updated = showPlayerDialog(selected);
        if (updated != null) {
            updated.setPlayerId(selected.getPlayerId());
            if (playerDAO.updatePlayer(updated)) {
                AlertUtil.showInfo("Success", "Player updated successfully.");
                loadPlayers();
            } else {
                AlertUtil.showError("Error", "Failed to update player.");
            }
        }
    }

    private void handleDeletePlayer() {
        Player selected = playersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("No Selection", "Please select a player to delete.");
            return;
        }
        boolean confirmed = AlertUtil.showConfirmation("Confirm Delete",
                "Are you sure you want to delete " + selected.getPlayerName() + "?");
        if (confirmed) {
            if (playerDAO.deletePlayer(selected.getPlayerId())) {
                AlertUtil.showInfo("Success", "Player deleted successfully.");
                loadPlayers();
            } else {
                AlertUtil.showError("Error", "Failed to delete player.");
            }
        }
    }

    private Player showPlayerDialog(Player existing) {
        List<Team> teams = teamDAO.getAllTeams();
        Dialog<Player> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Add Player" : "Edit Player");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField(existing != null ? existing.getPlayerName() : "");
        TextField jerseyField = new TextField(existing != null ? String.valueOf(existing.getJerseyNumber()) : "");
        TextField nationalityField = new TextField(existing != null ? existing.getNationality() : "");
        ComboBox<Team> teamBox = new ComboBox<>(FXCollections.observableArrayList(teams));
        ComboBox<Player.Position> positionBox = new ComboBox<>(FXCollections.observableArrayList(Player.Position.values()));

        if (existing != null) {
            positionBox.setValue(existing.getPosition());
            teams.stream().filter(t -> t.getTeamId() == existing.getTeamId()).findFirst().ifPresent(teamBox::setValue);
        }

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Name:"), 0, 0); grid.add(nameField, 1, 0);
        grid.add(new Label("Team:"), 0, 1); grid.add(teamBox, 1, 1);
        grid.add(new Label("Position:"), 0, 2); grid.add(positionBox, 1, 2);
        grid.add(new Label("Jersey #:"), 0, 3); grid.add(jerseyField, 1, 3);
        grid.add(new Label("Nationality:"), 0, 4); grid.add(nationalityField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Player player = new Player();
                player.setPlayerName(nameField.getText().trim());
                player.setNationality(nationalityField.getText().trim());
                player.setPosition(positionBox.getValue() != null ? positionBox.getValue() : Player.Position.Forward);
                if (teamBox.getValue() != null) player.setTeamId(teamBox.getValue().getTeamId());
                try {
                    player.setJerseyNumber(Integer.parseInt(jerseyField.getText().trim()));
                } catch (NumberFormatException e) {
                    player.setJerseyNumber(0);
                }
                if (existing != null) {
                    player.setMatchesPlayed(existing.getMatchesPlayed());
                    player.setGoals(existing.getGoals());
                    player.setAssists(existing.getAssists());
                    player.setYellowCards(existing.getYellowCards());
                    player.setRedCards(existing.getRedCards());
                }
                return player;
            }
            return null;
        });

        Optional<Player> result = dialog.showAndWait();
        return result.orElse(null);
    }
}
