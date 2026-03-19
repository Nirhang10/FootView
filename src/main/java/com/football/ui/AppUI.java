package com.football.ui;

import com.football.dao.MatchDAO;
import com.football.dao.PlayerDAO;
import com.football.dao.TeamDAO;
import com.football.dao.UserDAO;
import com.football.models.Match;
import com.football.models.Player;
import com.football.models.Team;
import com.football.models.User;
import com.football.utils.AlertUtil;
import com.football.utils.ValidationUtil;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Supplier;

public class AppUI {

    private static final String APP_STYLESHEET = "/css/style.css";

    private final Stage stage;
    private final UserDAO userDAO = new UserDAO();
    private final TeamDAO teamDAO = new TeamDAO();
    private final MatchDAO matchDAO = new MatchDAO();
    private final PlayerDAO playerDAO = new PlayerDAO();

    public AppUI(Stage stage) {
        this.stage = stage;
    }

    public void showLogin() {
        setScene("Football Score Information System", createLoginView(), 620, 600, false);
    }

    private Parent createLoginView() {
        Label icon = new Label("⚽");
        icon.getStyleClass().add("login-icon");

        Label title = new Label("Football Score System");
        title.getStyleClass().add("login-title");

        Label subtitle = new Label("Login to your account");
        subtitle.getStyleClass().add("login-subtitle");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        styleInput(usernameField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        styleInput(passwordField);

        CheckBox rememberMe = new CheckBox("Remember me");
        rememberMe.getStyleClass().add("login-remember");

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("btn-primary");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setDefaultButton(true);
        loginButton.setOnAction(event -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (ValidationUtil.areFieldsEmpty(username, password)) {
                AlertUtil.showError("Login Error", "Please enter both username and password.");
                return;
            }

            User user = userDAO.authenticate(username, password);
            if (user == null) {
                AlertUtil.showError("Login Failed", "Invalid username or password, or your account is blocked.");
                return;
            }

            if (user.getUserType() == User.UserType.ADMIN) {
                showAdminDashboard(user);
            } else {
                showCustomerDashboard(user);
            }
        });

        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("btn-link");
        registerButton.setOnAction(event -> showRegister());

        HBox linkRow = new HBox(6, new Label("Don't have an account?"), registerButton);
        linkRow.getStyleClass().add("login-link-row");
        linkRow.setAlignment(Pos.CENTER);

        Label demoTitle = new Label("Demo Credentials:");
        demoTitle.getStyleClass().add("login-demo-title");
        Label customerDemo = new Label("Customer: Any username/password");
        customerDemo.getStyleClass().add("login-demo-text");
        Label adminDemo = new Label("Admin: username \"admin\" / password \"admin\"");
        adminDemo.getStyleClass().add("login-demo-text");
        VBox demoBox = new VBox(4, demoTitle, customerDemo, adminDemo);
        demoBox.getStyleClass().add("login-demo-box");

        Label version = new Label("v1.0.0 - Football Score Information System");
        version.getStyleClass().add("login-version");

        VBox form = new VBox(
                12,
                icon,
                title,
                subtitle,
                usernameField,
                passwordField,
                rememberMe,
                loginButton,
                linkRow,
                demoBox,
                version);
        form.getStyleClass().add("login-form");
        form.setMaxWidth(420);
        form.setAlignment(Pos.TOP_CENTER);

        VBox root = new VBox(form);
        root.getStyleClass().add("login-pane");
        root.setPadding(new Insets(28));
        root.setAlignment(Pos.CENTER);
        return root;
    }

    public void showRegister() {
        setScene("Register", createRegisterView(), 560, 450, false);
    }

    private Parent createRegisterView() {
        Label title = new Label("Create Account");
        title.getStyleClass().add("login-title");

        Label subtitle = new Label("Get started as a customer account");
        subtitle.getStyleClass().add("login-subtitle");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        styleInput(usernameField);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        styleInput(emailField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password (min 8 chars)");
        styleInput(passwordField);

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        styleInput(confirmPasswordField);

        Button registerButton = new Button("Create Account");
        registerButton.getStyleClass().add("btn-primary");
        registerButton.setOnAction(event -> {
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
                showLogin();
            } else {
                AlertUtil.showError("Registration Error", "Failed to create account. Please try again.");
            }
        });

        Button backButton = new Button("Back to Login");
        backButton.getStyleClass().add("btn-secondary");
        backButton.setOnAction(event -> showLogin());

        HBox actionRow = new HBox(10, registerButton, backButton);
        actionRow.setAlignment(Pos.CENTER_LEFT);

        VBox form = new VBox(12,
                title,
                subtitle,
                usernameField,
                emailField,
                passwordField,
                confirmPasswordField,
                actionRow);
        form.getStyleClass().add("login-form");
        form.setMaxWidth(420);

        VBox root = new VBox(form);
        root.getStyleClass().add("login-pane");
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(26));
        return root;
    }

    public void showAdminDashboard(User user) {
        setScene("Admin Dashboard - " + user.getUsername(), createAdminView(user), 1360, 840, true);
    }

    private Parent createAdminView(User user) {
        Label pageTitle = new Label("Dashboard");
        pageTitle.getStyleClass().add("page-title");

        Label pageSubtitle = new Label("Welcome back, Admin! Here's what's happening today.");
        pageSubtitle.getStyleClass().add("page-subtitle");

        VBox contentHost = new VBox();
        VBox.setVgrow(contentHost, Priority.ALWAYS);

        Button dashboardBtn = new Button("Dashboard");
        Button teamsBtn = new Button("Manage Teams");
        Button matchesBtn = new Button("Manage Matches");
        Button playersBtn = new Button("Manage Players");
        Button usersBtn = new Button("Manage Users");
        Button statsBtn = new Button("Statistics");
        Button settingsBtn = new Button("Settings");
        Button logoutBtn = new Button("Logout");

        List<Button> navButtons = List.of(
                dashboardBtn,
                teamsBtn,
                matchesBtn,
                playersBtn,
                usersBtn,
                statsBtn,
                settingsBtn);
        navButtons.forEach(button -> {
            button.getStyleClass().add("side-nav-btn");
            button.setMaxWidth(Double.MAX_VALUE);
        });

        logoutBtn.getStyleClass().add("side-nav-logout");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setOnAction(event -> showLogin());

        Runnable showDashboard = () -> {
            pageTitle.setText("Dashboard");
            pageSubtitle.setText("Welcome back, Admin! Here's what's happening today.");
            contentHost.getChildren().setAll(createAdminDashboardHome());
        };

        dashboardBtn.setOnAction(event -> {
            setActiveNavButton(navButtons, dashboardBtn);
            showDashboard.run();
        });
        teamsBtn.setOnAction(event -> {
            setActiveNavButton(navButtons, teamsBtn);
            pageTitle.setText("Manage Teams");
            pageSubtitle.setText("Add, edit, or delete teams");
            contentHost.getChildren().setAll(createTeamsManager());
        });
        matchesBtn.setOnAction(event -> {
            setActiveNavButton(navButtons, matchesBtn);
            pageTitle.setText("Manage Matches");
            pageSubtitle.setText("Schedule, update, or delete matches");
            contentHost.getChildren().setAll(createMatchesManager());
        });
        playersBtn.setOnAction(event -> {
            setActiveNavButton(navButtons, playersBtn);
            pageTitle.setText("Manage Players");
            pageSubtitle.setText("Add, edit, or delete players");
            contentHost.getChildren().setAll(createPlayersManager());
        });
        usersBtn.setOnAction(event -> {
            setActiveNavButton(navButtons, usersBtn);
            pageTitle.setText("Manage Users");
            pageSubtitle.setText("View and manage user accounts");
            contentHost.getChildren().setAll(createUsersManager());
        });
        statsBtn.setOnAction(event -> {
            setActiveNavButton(navButtons, statsBtn);
            pageTitle.setText("Statistics");
            pageSubtitle.setText("League table, top scorers, and team stats");
            contentHost.getChildren().setAll(createStatisticsView());
        });
        settingsBtn.setOnAction(event -> {
            setActiveNavButton(navButtons, settingsBtn);
            pageTitle.setText("Settings & Reports");
            pageSubtitle.setText("System settings and report tools");
            contentHost.getChildren().setAll(createSettingsView());
        });

        Label brandTitle = new Label("Admin Panel");
        brandTitle.getStyleClass().add("sidebar-brand-title");
        Label brandSubtitle = new Label("Football Score System");
        brandSubtitle.getStyleClass().add("sidebar-brand-subtitle");
        HBox brand = new HBox(10, new Label("⚽"), new VBox(2, brandTitle, brandSubtitle));
        brand.getStyleClass().add("sidebar-brand");

        Region grow = new Region();
        VBox.setVgrow(grow, Priority.ALWAYS);

        VBox sidebar = new VBox(
                12,
                brand,
                dashboardBtn,
                teamsBtn,
                matchesBtn,
                playersBtn,
                usersBtn,
                statsBtn,
                settingsBtn,
                grow,
                logoutBtn);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(260);
        sidebar.setPadding(new Insets(14));

        setActiveNavButton(navButtons, dashboardBtn);
        showDashboard.run();

        VBox center = new VBox(10, pageTitle, pageSubtitle, contentHost);
        center.getStyleClass().add("main-content");
        center.setPadding(new Insets(16));
        VBox.setVgrow(contentHost, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.getStyleClass().add("app-shell");
        root.setLeft(sidebar);
        root.setCenter(center);
        return root;
    }

    private Parent createAdminDashboardHome() {
        VBox teamsCount = summaryCard("Total Teams", () -> String.valueOf(teamDAO.getTeamCount()));
        VBox matchesCount = summaryCard("Total Matches", () -> String.valueOf(matchDAO.getMatchCount()));
        VBox playersCount = summaryCard("Total Players", () -> String.valueOf(playerDAO.getPlayerCount()));
        VBox usersCount = summaryCard("Total Users", () -> String.valueOf(userDAO.getUserCount()));

        HBox summary = new HBox(12, teamsCount, matchesCount, playersCount, usersCount);

        VBox recent = new VBox(8,
                new Label("Recent Activity"),
                activityRow("New user registered"),
                activityRow("Match score updated"),
                activityRow("Team details edited"));
        recent.getStyleClass().add("panel-card");

        Button quickTeam = new Button("Add New Team");
        quickTeam.getStyleClass().add("btn-primary");
        quickTeam.setMaxWidth(Double.MAX_VALUE);
        Button quickMatch = new Button("Schedule Match");
        quickMatch.getStyleClass().add("btn-secondary");
        quickMatch.setMaxWidth(Double.MAX_VALUE);
        Button quickPlayer = new Button("Add Player");
        quickPlayer.getStyleClass().add("btn-info");
        quickPlayer.setMaxWidth(Double.MAX_VALUE);

        VBox quick = new VBox(10, new Label("Quick Actions"), quickTeam, quickMatch, quickPlayer);
        quick.getStyleClass().add("panel-card");
        quick.setMinWidth(300);

        HBox lower = new HBox(12, recent, quick);
        HBox.setHgrow(recent, Priority.ALWAYS);

        VBox wrap = new VBox(12, summary, lower);
        return wrap;
    }

    private HBox activityRow(String text) {
        Label bullet = new Label("•");
        Label label = new Label(text);
        HBox row = new HBox(8, bullet, label);
        row.getStyleClass().add("activity-row");
        return row;
    }

    private VBox summaryCard(String title, Supplier<String> valueSupplier) {
        Label label = new Label(title);
        label.getStyleClass().add("card-label");

        Label value = new Label(valueSupplier.get());
        value.getStyleClass().add("card-value");

        VBox box = new VBox(4, label, value);
        box.getStyleClass().add("summary-card");
        box.setMinWidth(190);
        box.setMinHeight(92);
        return box;
    }

    private Parent createTeamsManager() {
        TableView<Team> table = new TableView<>();
        table.getColumns().add(column("ID", Team::getTeamId));
        table.getColumns().add(column("Name", Team::getTeamName));
        table.getColumns().add(column("City", Team::getCity));
        table.getColumns().add(column("Coach", Team::getCoach));
        table.getColumns().add(column("Points", Team::getPoints));

        TextField nameField = new TextField();
        TextField foundedField = new TextField();
        TextField stadiumField = new TextField();
        TextField coachField = new TextField();
        TextField cityField = new TextField();
        TextField logoField = new TextField();
        TextField pointsField = new TextField("0");

        nameField.setPromptText("Team Name");
        foundedField.setPromptText("Founded Year");
        stadiumField.setPromptText("Stadium");
        coachField.setPromptText("Coach");
        cityField.setPromptText("City");
        logoField.setPromptText("Logo URL");
        pointsField.setPromptText("Points");

        Runnable refresh = () -> table.setItems(FXCollections.observableArrayList(teamDAO.getAllTeams()));

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            if (selected == null) {
                return;
            }
            nameField.setText(selected.getTeamName());
            foundedField.setText(String.valueOf(selected.getFoundedYear()));
            stadiumField.setText(selected.getStadium());
            coachField.setText(selected.getCoach());
            cityField.setText(selected.getCity());
            logoField.setText(selected.getLogoUrl());
            pointsField.setText(String.valueOf(selected.getPoints()));
        });

        Button add = new Button("Add");
        add.getStyleClass().add("btn-secondary");
        add.setOnAction(event -> {
            Team team = new Team();
            try {
                team.setTeamName(nameField.getText().trim());
                team.setFoundedYear(Integer.parseInt(foundedField.getText().trim()));
                team.setStadium(stadiumField.getText().trim());
                team.setCoach(coachField.getText().trim());
                team.setCity(cityField.getText().trim());
                team.setLogoUrl(logoField.getText().trim());
                team.setPoints(Integer.parseInt(pointsField.getText().trim()));
            } catch (NumberFormatException e) {
                AlertUtil.showError("Validation Error", "Founded year and points must be numbers.");
                return;
            }
            if (ValidationUtil.isEmpty(team.getTeamName())) {
                AlertUtil.showError("Validation Error", "Team name is required.");
                return;
            }
            if (teamDAO.addTeam(team)) {
                clear(nameField, foundedField, stadiumField, coachField, cityField, logoField, pointsField);
                pointsField.setText("0");
                refresh.run();
            } else {
                AlertUtil.showError("Database Error", "Failed to add team.");
            }
        });

        Button update = new Button("Update Selected");
        update.getStyleClass().add("btn-primary");
        update.setOnAction(event -> {
            Team selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                AlertUtil.showWarning("Selection", "Select a team first.");
                return;
            }
            try {
                selected.setTeamName(nameField.getText().trim());
                selected.setFoundedYear(Integer.parseInt(foundedField.getText().trim()));
                selected.setStadium(stadiumField.getText().trim());
                selected.setCoach(coachField.getText().trim());
                selected.setCity(cityField.getText().trim());
                selected.setLogoUrl(logoField.getText().trim());
                selected.setPoints(Integer.parseInt(pointsField.getText().trim()));
            } catch (NumberFormatException e) {
                AlertUtil.showError("Validation Error", "Founded year and points must be numbers.");
                return;
            }
            if (teamDAO.updateTeam(selected)) {
                refresh.run();
            } else {
                AlertUtil.showError("Database Error", "Failed to update team.");
            }
        });

        Button delete = new Button("Delete Selected");
        delete.getStyleClass().add("btn-danger");
        delete.setOnAction(event -> {
            Team selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                AlertUtil.showWarning("Selection", "Select a team first.");
                return;
            }
            if (!AlertUtil.showConfirmation("Delete Team", "Delete " + selected.getTeamName() + "?")) {
                return;
            }
            if (teamDAO.deleteTeam(selected.getTeamId())) {
                clear(nameField, foundedField, stadiumField, coachField, cityField, logoField, pointsField);
                pointsField.setText("0");
                refresh.run();
            } else {
                AlertUtil.showError("Database Error", "Failed to delete team.");
            }
        });

        refresh.run();

        VBox form = new VBox(8,
                new Label("Manage Teams"),
                new GridPaneBuilder()
                        .add("Name", nameField)
                        .add("Founded", foundedField)
                        .add("Stadium", stadiumField)
                        .add("Coach", coachField)
                        .add("City", cityField)
                        .add("Logo", logoField)
                        .add("Points", pointsField)
                        .build(),
                new HBox(8, add, update, delete));
        form.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(table);
        root.setBottom(form);
        return root;
    }

    private Parent createMatchesManager() {
        TableView<Match> table = new TableView<>();
        table.getColumns().add(column("ID", Match::getMatchId));
        table.getColumns().add(column("Date", match -> String.valueOf(match.getMatchDate())));
        table.getColumns().add(column("Time", match -> String.valueOf(match.getMatchTime())));
        table.getColumns().add(column("Home", Match::getHomeTeamName));
        table.getColumns().add(column("Away", Match::getAwayTeamName));
        table.getColumns().add(column("Score", Match::getScoreDisplay));
        table.getColumns().add(column("Status", match -> match.getStatus().name()));

        ComboBox<Team> homeTeamBox = new ComboBox<>();
        ComboBox<Team> awayTeamBox = new ComboBox<>();
        DatePicker matchDate = new DatePicker(LocalDate.now());
        TextField matchTime = new TextField("19:00");
        TextField homeScore = new TextField("0");
        TextField awayScore = new TextField("0");
        ComboBox<Match.Status> statusBox = new ComboBox<>(FXCollections.observableArrayList(Match.Status.values()));
        statusBox.setValue(Match.Status.SCHEDULED);

        Runnable refreshTeams = () -> {
            List<Team> teams = teamDAO.getAllTeams();
            homeTeamBox.setItems(FXCollections.observableArrayList(teams));
            awayTeamBox.setItems(FXCollections.observableArrayList(teams));
        };

        Runnable refreshMatches = () -> table.setItems(FXCollections.observableArrayList(matchDAO.getAllMatches()));

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            if (selected == null) {
                return;
            }
            if (selected.getMatchDate() != null) {
                matchDate.setValue(selected.getMatchDate());
            }
            if (selected.getMatchTime() != null) {
                matchTime.setText(selected.getMatchTime().toString());
            }
            homeScore.setText(String.valueOf(selected.getHomeScore()));
            awayScore.setText(String.valueOf(selected.getAwayScore()));
            statusBox.setValue(selected.getStatus());

            homeTeamBox.getItems().stream()
                    .filter(team -> team.getTeamId() == selected.getHomeTeamId())
                    .findFirst()
                    .ifPresent(homeTeamBox::setValue);

            awayTeamBox.getItems().stream()
                    .filter(team -> team.getTeamId() == selected.getAwayTeamId())
                    .findFirst()
                    .ifPresent(awayTeamBox::setValue);
        });

        Button add = new Button("Add");
        add.getStyleClass().add("btn-secondary");
        add.setOnAction(event -> {
            if (homeTeamBox.getValue() == null || awayTeamBox.getValue() == null) {
                AlertUtil.showError("Validation Error", "Pick both home and away teams.");
                return;
            }
            if (homeTeamBox.getValue().getTeamId() == awayTeamBox.getValue().getTeamId()) {
                AlertUtil.showError("Validation Error", "Home and away teams must be different.");
                return;
            }
            Match match = new Match();
            try {
                match.setHomeTeamId(homeTeamBox.getValue().getTeamId());
                match.setAwayTeamId(awayTeamBox.getValue().getTeamId());
                match.setMatchDate(matchDate.getValue());
                match.setMatchTime(LocalTime.parse(matchTime.getText().trim()));
                match.setStatus(statusBox.getValue());
            } catch (DateTimeParseException e) {
                AlertUtil.showError("Validation Error", "Time format must be HH:mm (example 19:30).");
                return;
            }
            if (matchDAO.addMatch(match)) {
                refreshMatches.run();
            } else {
                AlertUtil.showError("Database Error", "Failed to add match.");
            }
        });

        Button update = new Button("Update Selected");
        update.getStyleClass().add("btn-primary");
        update.setOnAction(event -> {
            Match selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                AlertUtil.showWarning("Selection", "Select a match first.");
                return;
            }
            if (homeTeamBox.getValue() == null || awayTeamBox.getValue() == null) {
                AlertUtil.showError("Validation Error", "Pick both home and away teams.");
                return;
            }
            try {
                selected.setHomeTeamId(homeTeamBox.getValue().getTeamId());
                selected.setAwayTeamId(awayTeamBox.getValue().getTeamId());
                selected.setMatchDate(matchDate.getValue());
                selected.setMatchTime(LocalTime.parse(matchTime.getText().trim()));
                selected.setHomeScore(Integer.parseInt(homeScore.getText().trim()));
                selected.setAwayScore(Integer.parseInt(awayScore.getText().trim()));
                selected.setStatus(statusBox.getValue());
            } catch (DateTimeParseException | NumberFormatException e) {
                AlertUtil.showError("Validation Error", "Check time (HH:mm) and score values.");
                return;
            }
            if (matchDAO.updateMatch(selected)) {
                refreshMatches.run();
            } else {
                AlertUtil.showError("Database Error", "Failed to update match.");
            }
        });

        Button delete = new Button("Delete Selected");
        delete.getStyleClass().add("btn-danger");
        delete.setOnAction(event -> {
            Match selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                AlertUtil.showWarning("Selection", "Select a match first.");
                return;
            }
            if (!AlertUtil.showConfirmation("Delete Match", "Delete selected match?")) {
                return;
            }
            if (matchDAO.deleteMatch(selected.getMatchId())) {
                refreshMatches.run();
            } else {
                AlertUtil.showError("Database Error", "Failed to delete match.");
            }
        });

        refreshTeams.run();
        refreshMatches.run();

        GridPane form = new GridPaneBuilder()
                .add("Home Team", homeTeamBox)
                .add("Away Team", awayTeamBox)
                .add("Date", matchDate)
                .add("Time", matchTime)
                .add("Home Score", homeScore)
                .add("Away Score", awayScore)
                .add("Status", statusBox)
                .build();

        BorderPane root = new BorderPane();
        root.setCenter(table);
        root.setBottom(new VBox(10, form, new HBox(8, add, update, delete)));
        root.setPadding(new Insets(10));
        return root;
    }

    private Parent createPlayersManager() {
        TableView<Player> table = new TableView<>();
        table.getColumns().add(column("ID", Player::getPlayerId));
        table.getColumns().add(column("Player", Player::getPlayerName));
        table.getColumns().add(column("Team", player -> player.getTeamName() == null ? "-" : player.getTeamName()));
        table.getColumns().add(column("Position", player -> player.getPosition().name()));
        table.getColumns().add(column("Goals", Player::getGoals));
        table.getColumns().add(column("Assists", Player::getAssists));

        ComboBox<Team> teamBox = new ComboBox<>();
        TextField nameField = new TextField();
        ComboBox<Player.Position> positionBox = new ComboBox<>(
                FXCollections.observableArrayList(Player.Position.values()));
        TextField jerseyField = new TextField();
        DatePicker dobPicker = new DatePicker(LocalDate.of(2000, 1, 1));
        TextField nationalityField = new TextField();
        TextField heightField = new TextField("175");
        TextField weightField = new TextField("70");
        TextField goalsField = new TextField("0");
        TextField assistsField = new TextField("0");

        Runnable refreshTeams = () -> teamBox.setItems(FXCollections.observableArrayList(teamDAO.getAllTeams()));
        Runnable refreshPlayers = () -> table.setItems(FXCollections.observableArrayList(playerDAO.getAllPlayers()));

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            if (selected == null) {
                return;
            }
            nameField.setText(selected.getPlayerName());
            positionBox.setValue(selected.getPosition());
            jerseyField.setText(String.valueOf(selected.getJerseyNumber()));
            dobPicker.setValue(selected.getDateOfBirth());
            nationalityField.setText(selected.getNationality());
            heightField.setText(String.valueOf(selected.getHeight()));
            weightField.setText(String.valueOf(selected.getWeight()));
            goalsField.setText(String.valueOf(selected.getGoals()));
            assistsField.setText(String.valueOf(selected.getAssists()));
            teamBox.getItems().stream()
                    .filter(team -> team.getTeamId() == selected.getTeamId())
                    .findFirst()
                    .ifPresent(teamBox::setValue);
        });

        Button add = new Button("Add");
        add.getStyleClass().add("btn-secondary");
        add.setOnAction(event -> {
            if (teamBox.getValue() == null || positionBox.getValue() == null
                    || ValidationUtil.isEmpty(nameField.getText())) {
                AlertUtil.showError("Validation Error", "Team, player name and position are required.");
                return;
            }
            Player player = new Player();
            try {
                player.setTeamId(teamBox.getValue().getTeamId());
                player.setPlayerName(nameField.getText().trim());
                player.setPosition(positionBox.getValue());
                player.setJerseyNumber(Integer.parseInt(jerseyField.getText().trim()));
                player.setDateOfBirth(dobPicker.getValue());
                player.setNationality(nationalityField.getText().trim());
                player.setHeight(Double.parseDouble(heightField.getText().trim()));
                player.setWeight(Double.parseDouble(weightField.getText().trim()));
                player.setGoals(Integer.parseInt(goalsField.getText().trim()));
                player.setAssists(Integer.parseInt(assistsField.getText().trim()));
            } catch (NumberFormatException e) {
                AlertUtil.showError("Validation Error", "Jersey, goals, assists, height and weight must be numeric.");
                return;
            }
            if (playerDAO.addPlayer(player)) {
                clear(nameField, jerseyField, nationalityField, heightField, weightField, goalsField, assistsField);
                goalsField.setText("0");
                assistsField.setText("0");
                refreshPlayers.run();
            } else {
                AlertUtil.showError("Database Error", "Failed to add player.");
            }
        });

        Button update = new Button("Update Selected");
        update.getStyleClass().add("btn-primary");
        update.setOnAction(event -> {
            Player selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                AlertUtil.showWarning("Selection", "Select a player first.");
                return;
            }
            if (teamBox.getValue() == null || positionBox.getValue() == null
                    || ValidationUtil.isEmpty(nameField.getText())) {
                AlertUtil.showError("Validation Error", "Team, player name and position are required.");
                return;
            }
            try {
                selected.setTeamId(teamBox.getValue().getTeamId());
                selected.setPlayerName(nameField.getText().trim());
                selected.setPosition(positionBox.getValue());
                selected.setJerseyNumber(Integer.parseInt(jerseyField.getText().trim()));
                selected.setDateOfBirth(dobPicker.getValue());
                selected.setNationality(nationalityField.getText().trim());
                selected.setHeight(Double.parseDouble(heightField.getText().trim()));
                selected.setWeight(Double.parseDouble(weightField.getText().trim()));
                selected.setGoals(Integer.parseInt(goalsField.getText().trim()));
                selected.setAssists(Integer.parseInt(assistsField.getText().trim()));
            } catch (NumberFormatException e) {
                AlertUtil.showError("Validation Error", "Jersey, goals, assists, height and weight must be numeric.");
                return;
            }
            if (playerDAO.updatePlayer(selected)) {
                refreshPlayers.run();
            } else {
                AlertUtil.showError("Database Error", "Failed to update player.");
            }
        });

        Button delete = new Button("Delete Selected");
        delete.getStyleClass().add("btn-danger");
        delete.setOnAction(event -> {
            Player selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                AlertUtil.showWarning("Selection", "Select a player first.");
                return;
            }
            if (!AlertUtil.showConfirmation("Delete Player", "Delete " + selected.getPlayerName() + "?")) {
                return;
            }
            if (playerDAO.deletePlayer(selected.getPlayerId())) {
                refreshPlayers.run();
            } else {
                AlertUtil.showError("Database Error", "Failed to delete player.");
            }
        });

        refreshTeams.run();
        refreshPlayers.run();

        GridPane form = new GridPaneBuilder()
                .add("Team", teamBox)
                .add("Player", nameField)
                .add("Position", positionBox)
                .add("Jersey", jerseyField)
                .add("DOB", dobPicker)
                .add("Nationality", nationalityField)
                .add("Height", heightField)
                .add("Weight", weightField)
                .add("Goals", goalsField)
                .add("Assists", assistsField)
                .build();

        BorderPane root = new BorderPane();
        root.setCenter(table);
        root.setBottom(new VBox(10, form, new HBox(8, add, update, delete)));
        root.setPadding(new Insets(10));
        return root;
    }

    private Parent createUsersManager() {
        TableView<User> table = new TableView<>();
        table.getColumns().add(column("ID", User::getUserId));
        table.getColumns().add(column("Username", User::getUsername));
        table.getColumns().add(column("Email", User::getEmail));
        table.getColumns().add(column("Status", user -> user.getStatus().name()));
        table.getColumns().add(column("Last Login", user -> String.valueOf(user.getLastLogin())));

        Runnable refresh = () -> table.setItems(FXCollections.observableArrayList(userDAO.getAllCustomers()));

        Button activate = new Button("Set ACTIVE");
        activate.getStyleClass().add("btn-primary");
        activate.setOnAction(event -> updateUserStatus(table, User.Status.ACTIVE, refresh));

        Button block = new Button("Set BLOCKED");
        block.getStyleClass().add("btn-danger");
        block.setOnAction(event -> updateUserStatus(table, User.Status.BLOCKED, refresh));

        Button refreshButton = new Button("Refresh");
        refreshButton.getStyleClass().add("btn-secondary");
        refreshButton.setOnAction(event -> refresh.run());

        refresh.run();

        BorderPane root = new BorderPane();
        root.setCenter(table);
        root.setBottom(new HBox(8, activate, block, refreshButton));
        root.setPadding(new Insets(10));
        return root;
    }

    private void updateUserStatus(TableView<User> table, User.Status status, Runnable refresh) {
        User selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("Selection", "Select a user first.");
            return;
        }
        if (userDAO.updateUserStatus(selected.getUserId(), status)) {
            refresh.run();
        } else {
            AlertUtil.showError("Database Error", "Failed to update user status.");
        }
    }

    private Parent createStatisticsView() {
        SplitPaneBuilder split = new SplitPaneBuilder();

        TableView<Team> standings = new TableView<>();
        standings.getColumns().add(column("Pos", team -> 0));
        standings.getColumns().add(column("Team", Team::getTeamName));
        standings.getColumns().add(column("MP", Team::getMatchesPlayed));
        standings.getColumns().add(column("W", Team::getWins));
        standings.getColumns().add(column("D", Team::getDraws));
        standings.getColumns().add(column("L", Team::getLosses));
        standings.getColumns().add(column("GF", Team::getGoalsFor));
        standings.getColumns().add(column("GA", Team::getGoalsAgainst));
        standings.getColumns().add(column("GD", Team::getGoalDifference));
        standings.getColumns().add(column("PTS", Team::getPoints));

        standings.getColumns().set(0, positionColumn("Pos", standings));

        TableView<Player> scorers = new TableView<>();
        scorers.getColumns().add(column("Player", Player::getPlayerName));
        scorers.getColumns().add(column("Team", player -> player.getTeamName() == null ? "-" : player.getTeamName()));
        scorers.getColumns().add(column("Goals", Player::getGoals));
        scorers.getColumns().add(column("Assists", Player::getAssists));

        TextField mpField = new TextField();
        TextField winsField = new TextField();
        TextField drawsField = new TextField();
        TextField lossesField = new TextField();
        TextField gfField = new TextField();
        TextField gdField = new TextField();
        TextField pointsField = new TextField();

        standings.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            if (selected == null) {
                return;
            }
            mpField.setText(String.valueOf(selected.getMatchesPlayed()));
            winsField.setText(String.valueOf(selected.getWins()));
            drawsField.setText(String.valueOf(selected.getDraws()));
            lossesField.setText(String.valueOf(selected.getLosses()));
            gfField.setText(String.valueOf(selected.getGoalsFor()));
            gdField.setText(String.valueOf(selected.getGoalDifference()));
            pointsField.setText(String.valueOf(selected.getPoints()));
        });

        Runnable refreshStats = () -> {
            standings.setItems(FXCollections.observableArrayList(teamDAO.getStandings()));
            scorers.setItems(FXCollections.observableArrayList(playerDAO.getTopScorers(10)));
        };

        Button saveStats = new Button("Save Team Stats");
        saveStats.getStyleClass().add("btn-primary");
        saveStats.setOnAction(event -> {
            Team selected = standings.getSelectionModel().getSelectedItem();
            if (selected == null) {
                AlertUtil.showWarning("Selection", "Select a team from standings first.");
                return;
            }

            try {
                int mp = Integer.parseInt(mpField.getText().trim());
                int wins = Integer.parseInt(winsField.getText().trim());
                int draws = Integer.parseInt(drawsField.getText().trim());
                int losses = Integer.parseInt(lossesField.getText().trim());
                int goalsFor = Integer.parseInt(gfField.getText().trim());
                int goalDiff = Integer.parseInt(gdField.getText().trim());
                int points = Integer.parseInt(pointsField.getText().trim());

                if (mp < 0 || wins < 0 || draws < 0 || losses < 0 || goalsFor < 0 || points < 0) {
                    AlertUtil.showError("Validation Error", "Statistics values cannot be negative.");
                    return;
                }
                if (wins + draws + losses != mp) {
                    AlertUtil.showError("Validation Error", "MP must equal W + D + L.");
                    return;
                }

                int goalsAgainst = goalsFor - goalDiff;
                if (goalsAgainst < 0) {
                    AlertUtil.showError("Validation Error", "GF and GD combination gives negative GA.");
                    return;
                }

                if (teamDAO.updateTeamStatistics(selected.getTeamId(), wins, draws, losses, goalsFor, goalsAgainst,
                        points)) {
                    refreshStats.run();
                } else {
                    AlertUtil.showError("Database Error", "Failed to update team statistics.");
                }
            } catch (NumberFormatException e) {
                AlertUtil.showError("Validation Error", "MP, W, D, L, GF, GD and PTS must be numbers.");
            }
        });

        Button refreshButton = new Button("Refresh");
        refreshButton.getStyleClass().add("btn-secondary");
        refreshButton.setOnAction(event -> refreshStats.run());

        GridPane statsEditor = new GridPaneBuilder()
                .add("MP", mpField)
                .add("W", winsField)
                .add("D", drawsField)
                .add("L", lossesField)
                .add("GF", gfField)
                .add("GD", gdField)
                .add("PTS", pointsField)
                .build();

        refreshStats.run();

        split.addPane(new VBox(8, new Label("League Standings"), standings, new Label("Edit Selected Team Statistics"),
                statsEditor, new HBox(8, saveStats, refreshButton)));
        split.addPane(new VBox(8, new Label("Top Scorers"), scorers));
        return split.build();
    }

    public void showCustomerDashboard(User user) {
        setScene("Customer Dashboard - " + user.getUsername(), createCustomerView(user), 1360, 840, true);
    }

    public void showDashboardForUser(User user) {
        if (user.getUserType() == User.UserType.ADMIN) {
            showAdminDashboard(user);
        } else {
            showCustomerDashboard(user);
        }
    }

    private Parent createCustomerView(User user) {
        Label brand = new Label("⚽  Football Score");
        brand.getStyleClass().add("top-brand");

        VBox contentHost = new VBox();
        VBox.setVgrow(contentHost, Priority.ALWAYS);

        Button dashBtn = new Button("Dashboard");
        Button teamsBtn = new Button("Teams");
        Button matchesBtn = new Button("Matches");
        Button statsBtn = new Button("Statistics");
        List<Button> navButtons = List.of(dashBtn, teamsBtn, matchesBtn, statsBtn);
        navButtons.forEach(button -> button.getStyleClass().add("top-nav-btn"));

        Label userBadge = new Label("👤  " + user.getUsername());
        userBadge.getStyleClass().add("user-badge");

        Button refresh = new Button("Refresh");
        refresh.getStyleClass().add("btn-secondary");
        refresh.setOnAction(event -> showCustomerDashboard(user));

        Button logout = new Button("Logout");
        logout.getStyleClass().add("btn-danger");
        logout.setOnAction(event -> showLogin());

        Runnable showDashboard = () -> contentHost.getChildren().setAll(createCustomerDashboardHome(user));

        dashBtn.setOnAction(event -> {
            setActiveNavButton(navButtons, dashBtn);
            showDashboard.run();
        });
        teamsBtn.setOnAction(event -> {
            setActiveNavButton(navButtons, teamsBtn);
            contentHost.getChildren().setAll(createTeamsCardsView());
        });
        matchesBtn.setOnAction(event -> {
            setActiveNavButton(navButtons, matchesBtn);
            contentHost.getChildren().setAll(createMatchesCardsView());
        });
        statsBtn.setOnAction(event -> {
            setActiveNavButton(navButtons, statsBtn);
            contentHost.getChildren().setAll(createCustomerStatisticsView());
        });

        HBox navCenter = new HBox(6, dashBtn, teamsBtn, matchesBtn, statsBtn);
        navCenter.setAlignment(Pos.CENTER);

        Region spacerL = new Region();
        Region spacerR = new Region();
        HBox.setHgrow(spacerL, Priority.ALWAYS);
        HBox.setHgrow(spacerR, Priority.ALWAYS);

        HBox top = new HBox(12, brand, spacerL, navCenter, spacerR, userBadge, refresh, logout);
        top.getStyleClass().add("top-nav-bar");
        top.setPadding(new Insets(12, 16, 12, 16));
        top.setAlignment(Pos.CENTER_LEFT);

        setActiveNavButton(navButtons, dashBtn);
        showDashboard.run();

        VBox center = new VBox(contentHost);
        center.getStyleClass().add("main-content");
        center.setPadding(new Insets(14));

        BorderPane root = new BorderPane();
        root.getStyleClass().add("app-shell");
        root.setTop(top);
        root.setCenter(center);
        return root;
    }

    private Parent createCustomerDashboardHome(User user) {
        VBox welcome = new VBox(6, new Label("Welcome Back!"), new Label(user.getUsername()));
        welcome.getStyleClass().add("panel-card");

        VBox matchesToday = summaryCard("Matches Today", () -> String.valueOf(
                matchDAO.getAllMatches().stream().filter(match -> LocalDate.now().equals(match.getMatchDate()))
                        .count()));
        VBox totalTeams = summaryCard("Total Teams", () -> String.valueOf(teamDAO.getTeamCount()));
        VBox totalPlayers = summaryCard("Total Players", () -> String.valueOf(playerDAO.getPlayerCount()));
        VBox totalMatches = summaryCard("Total Matches", () -> String.valueOf(matchDAO.getMatchCount()));

        HBox metrics = new HBox(12, matchesToday, totalTeams, totalPlayers, totalMatches);

        HBox row = new HBox(12,
                wrapSection("Today's Matches", createMatchesTableOnly()),
                wrapSection("League Standings", createStandingsTableOnly()));
        HBox.setHgrow(row.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(row.getChildren().get(1), Priority.ALWAYS);

        VBox root = new VBox(12, welcome, metrics, row, wrapSection("Top Scorers", createTopScorersTableOnly()));
        return root;
    }

    private Parent wrapSection(String title, Parent content) {
        VBox wrap = new VBox(8, new Label(title), content);
        wrap.getStyleClass().add("panel-card");
        return wrap;
    }

    private Parent createTeamsCardsView() {
        FlowPane flow = new FlowPane();
        flow.setHgap(12);
        flow.setVgap(12);
        List<Team> teams = teamDAO.getAllTeams();
        for (Team team : teams) {
            VBox card = new VBox(
                    8,
                    new Label(team.getTeamName()),
                    new Label(team.getStadium()),
                    new Label("W/D/L: " + team.getWins() + "/" + team.getDraws() + "/" + team.getLosses()),
                    new Label(team.getPoints() + " Points"));
            card.getStyleClass().add("team-card");
            card.setPrefWidth(260);
            flow.getChildren().add(card);
        }
        ScrollPane scroll = new ScrollPane(flow);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("transparent-scroll");
        return wrapSection("Teams", scroll);
    }

    private Parent createMatchesCardsView() {
        VBox list = new VBox(10);
        for (Match match : matchDAO.getAllMatches()) {
            String status = switch (match.getStatus()) {
                case IN_PROGRESS -> "Live";
                case SCHEDULED -> "Scheduled";
                case COMPLETED -> "Completed";
                case POSTPONED -> "Postponed";
                case CANCELLED -> "Cancelled";
            };
            HBox row = new HBox(
                    12,
                    new Label(String.valueOf(match.getMatchDate())),
                    new Label(String.valueOf(match.getMatchTime())),
                    new Label(match.getHomeTeamName() + " vs " + match.getAwayTeamName()),
                    statusChip(status));
            row.getStyleClass().add("match-row-card");
            list.getChildren().add(row);
        }
        ScrollPane scroll = new ScrollPane(list);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("transparent-scroll");
        return wrapSection("Matches", scroll);
    }

    private Label statusChip(String text) {
        Label chip = new Label(text);
        chip.getStyleClass().add("status-chip");
        String lower = text.toLowerCase();
        if (lower.contains("live")) {
            chip.getStyleClass().add("status-live");
        } else if (lower.contains("scheduled")) {
            chip.getStyleClass().add("status-scheduled");
        } else if (lower.contains("completed")) {
            chip.getStyleClass().add("status-completed");
        }
        return chip;
    }

    private Parent createCustomerStatisticsView() {
        TabPane tabs = new TabPane();
        tabs.getTabs().add(new Tab("League Table", createStandingsTableOnly()));
        tabs.getTabs().add(new Tab("Top Scorers", createTopScorersTableOnly()));

        ComboBox<Team> teams = new ComboBox<>(FXCollections.observableArrayList(teamDAO.getStandings()));
        teams.getSelectionModel().selectFirst();
        Label record = new Label();
        Label goals = new Label();
        Runnable render = () -> {
            Team selected = teams.getValue();
            if (selected == null) {
                return;
            }
            record.setText("W/D/L: " + selected.getWins() + "/" + selected.getDraws() + "/" + selected.getLosses());
            goals.setText("GF/GA/GD: " + selected.getGoalsFor() + "/" + selected.getGoalsAgainst() + "/"
                    + selected.getGoalDifference());
        };
        teams.setOnAction(event -> render.run());
        render.run();
        tabs.getTabs().add(new Tab("Team Stats", new VBox(10, teams, record, goals)));
        tabs.getTabs().forEach(tab -> tab.setClosable(false));
        return wrapSection("Statistics", tabs);
    }

    private Parent createSettingsView() {
        VBox appSettings = new VBox(
                10,
                new Label("Application Settings"),
                new Separator(),
                new Label("Application Name: Football Score System"),
                new Label("Version: 1.0.0"),
                new Label("Timezone: UTC (GMT+0)"),
                new Label("Language: English"));
        appSettings.getStyleClass().add("panel-card");

        VBox dbSettings = new VBox(
                8,
                new Label("Database Connection"),
                new Separator(),
                new Label("MySQL Server - localhost:3306"),
                new Label("Status: Active"));
        dbSettings.getStyleClass().add("panel-card");

        return new VBox(12, appSettings, dbSettings);
    }

    private void setActiveNavButton(List<Button> allButtons, Button activeButton) {
        for (Button button : allButtons) {
            button.getStyleClass().remove("nav-active");
        }
        if (!activeButton.getStyleClass().contains("nav-active")) {
            activeButton.getStyleClass().add("nav-active");
        }
    }

    private Parent createTeamsTableOnly() {
        TableView<Team> table = new TableView<>();
        table.getColumns().add(column("Name", Team::getTeamName));
        table.getColumns().add(column("City", Team::getCity));
        table.getColumns().add(column("Coach", Team::getCoach));
        table.getColumns().add(column("Stadium", Team::getStadium));
        table.setItems(FXCollections.observableArrayList(teamDAO.getAllTeams()));
        return new BorderPane(table);
    }

    private Parent createMatchesTableOnly() {
        TableView<Match> table = new TableView<>();
        table.getColumns().add(column("Date", match -> String.valueOf(match.getMatchDate())));
        table.getColumns().add(column("Home", Match::getHomeTeamName));
        table.getColumns().add(column("Score", Match::getScoreDisplay));
        table.getColumns().add(column("Away", Match::getAwayTeamName));
        table.getColumns().add(column("Status", match -> match.getStatus().name()));
        table.setItems(FXCollections.observableArrayList(matchDAO.getAllMatches()));
        return new BorderPane(table);
    }

    private Parent createStandingsTableOnly() {
        TableView<Team> table = new TableView<>();
        table.getColumns().add(positionColumn("Pos", table));
        table.getColumns().add(column("Team", Team::getTeamName));
        table.getColumns().add(column("MP", Team::getMatchesPlayed));
        table.getColumns().add(column("W", Team::getWins));
        table.getColumns().add(column("D", Team::getDraws));
        table.getColumns().add(column("L", Team::getLosses));
        table.getColumns().add(column("PTS", Team::getPoints));
        table.setItems(FXCollections.observableArrayList(teamDAO.getStandings()));
        return new BorderPane(table);
    }

    private Parent createTopScorersTableOnly() {
        TableView<Player> table = new TableView<>();
        table.getColumns().add(column("Player", Player::getPlayerName));
        table.getColumns().add(column("Team", player -> player.getTeamName() == null ? "-" : player.getTeamName()));
        table.getColumns().add(column("Goals", Player::getGoals));
        table.getColumns().add(column("Assists", Player::getAssists));
        table.setItems(FXCollections.observableArrayList(playerDAO.getTopScorers(10)));
        return new BorderPane(table);
    }

    private <T, V> TableColumn<T, V> column(String name, java.util.function.Function<T, V> mapper) {
        TableColumn<T, V> column = new TableColumn<>(name);
        column.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(mapper.apply(cell.getValue())));
        column.setPrefWidth(120);
        return column;
    }

    private <T> TableColumn<T, Integer> positionColumn(String name, TableView<T> table) {
        TableColumn<T, Integer> col = new TableColumn<>(name);
        col.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(table.getItems().indexOf(cell.getValue()) + 1));
        col.setPrefWidth(65);
        return col;
    }

    private void setScene(String title, Parent root, int width, int height, boolean resizable) {
        Scene scene = new Scene(root, width, height);
        applyStyles(scene);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setResizable(resizable);
        stage.centerOnScreen();
        if (!stage.isShowing()) {
            stage.show();
        }
    }

    private void applyStyles(Scene scene) {
        var cssUrl = getClass().getResource(APP_STYLESHEET);
        if (cssUrl != null) {
            scene.getStylesheets().setAll(cssUrl.toExternalForm());
        }
    }

    private void styleInput(TextInputControl input) {
        input.getStyleClass().add("form-input");
    }

    private void clear(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private static final class GridPaneBuilder {
        private final GridPane grid;
        private int row = 0;

        private GridPaneBuilder() {
            grid = new GridPane();
            grid.setHgap(8);
            grid.setVgap(8);
            grid.setPadding(new Insets(2));
        }

        GridPaneBuilder add(String labelText, javafx.scene.Node control) {
            Label label = new Label(labelText + ":");
            grid.add(label, 0, row);
            grid.add(control, 1, row);
            if (control instanceof Region region) {
                region.setPrefWidth(280);
            }
            row++;
            return this;
        }

        GridPane build() {
            return grid;
        }
    }

    private static final class SplitPaneBuilder {
        private final javafx.scene.control.SplitPane splitPane = new javafx.scene.control.SplitPane();

        SplitPaneBuilder addPane(Parent parent) {
            splitPane.getItems().add(parent);
            return this;
        }

        Parent build() {
            splitPane.setDividerPositions(0.55);
            return splitPane;
        }
    }
}
