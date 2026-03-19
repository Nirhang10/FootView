package com.football.controllers;

import com.football.dao.UserDAO;
import com.football.ui.AppUI;
import com.football.utils.AlertUtil;
import com.football.utils.ValidationUtil;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

    private TextField usernameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;

    private UserDAO userDAO = new UserDAO();

    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (ValidationUtil.areFieldsEmpty(username, email, password, confirmPassword)) {
            AlertUtil.showError("Validation Error", "All fields are required.");
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            AlertUtil.showError("Validation Error", "Please enter a valid email address.");
            return;
        }

        if (!ValidationUtil.isStrongPassword(password)) {
            AlertUtil.showError("Validation Error", "Password must be at least 8 characters long.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            AlertUtil.showError("Validation Error", "Passwords do not match.");
            return;
        }

        if (userDAO.findByUsername(username) != null) {
            AlertUtil.showError("Registration Error", "Username already exists.");
            return;
        }

        if (userDAO.findByEmail(email) != null) {
            AlertUtil.showError("Registration Error", "Email already registered.");
            return;
        }

        boolean success = userDAO.register(username, email, password);
        if (success) {
            AlertUtil.showInfo("Success", "Account created successfully! Please log in.");
            goToLogin();
        } else {
            AlertUtil.showError("Registration Error", "Failed to create account. Please try again.");
        }
    }

    private void handleBackToLogin(ActionEvent event) {
        goToLogin();
    }

    private void goToLogin() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        new AppUI(stage).showLogin();
    }
}
