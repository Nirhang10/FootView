-- Football Score Information System
-- Sample Data

USE football_score_db;

-- Admin user (password: admin123)
INSERT INTO users (username, email, password_hash, user_type, status) VALUES
('admin', 'admin@football.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lFHq', 'ADMIN', 'ACTIVE');

-- Customer users (password: admin123)
INSERT INTO users (username, email, password_hash, user_type, status) VALUES
('john_doe', 'john@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lFHq', 'CUSTOMER', 'ACTIVE'),
('jane_smith', 'jane@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lFHq', 'CUSTOMER', 'ACTIVE'),
('bob_jones', 'bob@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lFHq', 'CUSTOMER', 'ACTIVE');

-- Teams
INSERT INTO teams (team_name, founded_year, stadium, coach, city, wins, draws, losses, goals_for, goals_against, points) VALUES
('Manchester City', 1880, 'Etihad Stadium', 'Pep Guardiola', 'Manchester', 8, 2, 0, 28, 8, 26),
('Arsenal', 1886, 'Emirates Stadium', 'Mikel Arteta', 'London', 7, 2, 1, 25, 10, 23),
('Liverpool', 1892, 'Anfield', 'Jurgen Klopp', 'Liverpool', 7, 1, 2, 24, 12, 22),
('Chelsea', 1905, 'Stamford Bridge', 'Mauricio Pochettino', 'London', 5, 3, 2, 18, 14, 18),
('Manchester United', 1878, 'Old Trafford', 'Erik ten Hag', 'Manchester', 5, 2, 3, 17, 15, 17),
('Tottenham Hotspur', 1882, 'Tottenham Hotspur Stadium', 'Ange Postecoglou', 'London', 4, 3, 3, 20, 18, 15),
('Newcastle United', 1892, 'St. James Park', 'Eddie Howe', 'Newcastle', 4, 2, 4, 16, 16, 14),
('Aston Villa', 1874, 'Villa Park', 'Unai Emery', 'Birmingham', 4, 1, 5, 15, 19, 13);

-- Players for Manchester City
INSERT INTO players (team_id, player_name, position, jersey_number, nationality, matches_played, goals, assists) VALUES
(1, 'Ederson', 'Goalkeeper', 31, 'Brazilian', 10, 0, 0),
(1, 'Ruben Dias', 'Defender', 3, 'Portuguese', 10, 1, 0),
(1, 'Kevin De Bruyne', 'Midfielder', 17, 'Belgian', 8, 2, 5),
(1, 'Erling Haaland', 'Forward', 9, 'Norwegian', 10, 12, 2),
(1, 'Phil Foden', 'Midfielder', 47, 'English', 10, 5, 4);

-- Players for Arsenal
INSERT INTO players (team_id, player_name, position, jersey_number, nationality, matches_played, goals, assists) VALUES
(2, 'David Raya', 'Goalkeeper', 22, 'Spanish', 10, 0, 0),
(2, 'Ben White', 'Defender', 4, 'English', 10, 1, 2),
(2, 'Martin Odegaard', 'Midfielder', 8, 'Norwegian', 10, 4, 3),
(2, 'Bukayo Saka', 'Forward', 7, 'English', 10, 6, 5),
(2, 'Gabriel Martinelli', 'Forward', 11, 'Brazilian', 10, 5, 2);

-- Players for Liverpool
INSERT INTO players (team_id, player_name, position, jersey_number, nationality, matches_played, goals, assists) VALUES
(3, 'Alisson Becker', 'Goalkeeper', 1, 'Brazilian', 10, 0, 0),
(3, 'Virgil van Dijk', 'Defender', 4, 'Dutch', 10, 2, 0),
(3, 'Trent Alexander-Arnold', 'Defender', 66, 'English', 10, 1, 6),
(3, 'Mohamed Salah', 'Forward', 11, 'Egyptian', 10, 8, 4),
(3, 'Darwin Nunez', 'Forward', 9, 'Uruguayan', 9, 5, 2);

-- Sample Matches
INSERT INTO matches (home_team_id, away_team_id, match_date, match_time, home_score, away_score, status) VALUES
(1, 2, '2024-01-15', '15:00:00', 3, 1, 'COMPLETED'),
(3, 4, '2024-01-16', '17:30:00', 2, 0, 'COMPLETED'),
(5, 6, '2024-01-20', '14:00:00', 1, 1, 'COMPLETED'),
(2, 3, '2024-01-27', '16:00:00', 0, 2, 'COMPLETED'),
(1, 5, '2024-02-03', '15:00:00', 3, 0, 'COMPLETED'),
(4, 7, '2024-02-10', '15:00:00', 2, 1, 'COMPLETED'),
(6, 8, '2024-02-17', '15:00:00', 3, 2, 'COMPLETED'),
(2, 5, '2024-03-01', '15:00:00', 0, 0, 'SCHEDULED'),
(1, 3, '2024-03-08', '17:30:00', 0, 0, 'SCHEDULED'),
(4, 6, '2024-03-15', '14:00:00', 0, 0, 'SCHEDULED');
