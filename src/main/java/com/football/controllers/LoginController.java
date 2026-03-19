package com.football.controllers;

import com.football.dao.UserDAO;
import com.football.models.User;
import com.football.ui.AppUI;
import com.football.utils.AlertUtil;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    private TextField usernameField;
    private PasswordField passwordField;

    private UserDAO userDAO = new UserDAO();

    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            AlertUtil.showError("Login Error", "Please enter both username and password.");
            return;
        }

        User user = userDAO.authenticate(username, password);
        if (user == null) {
            AlertUtil.showError("Login Failed", "Invalid username or password, or your account is blocked.");
            return;
        }

        Stage stage = (Stage) usernameField.getScene().getWindow();
        new AppUI(stage).showDashboardForUser(user);
    }

    private void handleRegisterLink(ActionEvent event) {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        new AppUI(stage).showRegister();
    }
}
