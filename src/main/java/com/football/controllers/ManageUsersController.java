package com.football.controllers;

import com.football.dao.UserDAO;
import com.football.models.User;
import com.football.utils.AlertUtil;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ManageUsersController {

    private TableView<User> usersTable;
    private TableColumn<User, Integer> idCol;
    private TableColumn<User, String> usernameCol;
    private TableColumn<User, String> emailCol;
    private TableColumn<User, String> statusCol;
    private TableColumn<User, String> createdAtCol;

    private UserDAO userDAO = new UserDAO();

    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        loadUsers();
    }

    private void setupTable() {
        if (idCol != null) idCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        if (usernameCol != null) usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        if (emailCol != null) emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        if (statusCol != null) statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        if (createdAtCol != null) createdAtCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
    }

    private void loadUsers() {
        List<User> users = userDAO.getAllCustomers();
        usersTable.setItems(FXCollections.observableArrayList(users));
    }

    private void handleToggleStatus() {
        User selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("No Selection", "Please select a user to update.");
            return;
        }

        User.Status newStatus = selected.getStatus() == User.Status.ACTIVE
                ? User.Status.BLOCKED
                : User.Status.ACTIVE;

        String action = newStatus == User.Status.BLOCKED ? "block" : "unblock";
        boolean confirmed = AlertUtil.showConfirmation("Confirm",
                "Are you sure you want to " + action + " user " + selected.getUsername() + "?");

        if (confirmed) {
            if (userDAO.updateUserStatus(selected.getUserId(), newStatus)) {
                AlertUtil.showInfo("Success", "User " + action + "ed successfully.");
                loadUsers();
            } else {
                AlertUtil.showError("Error", "Failed to update user status.");
            }
        }
    }

    private void handleRefresh() {
        loadUsers();
    }
}
