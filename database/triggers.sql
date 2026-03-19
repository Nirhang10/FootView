-- Football Score Information System
-- Database Triggers

USE football_score_db;

DELIMITER //

-- Trigger: Update team stats when a match is completed
CREATE TRIGGER after_match_completed
AFTER UPDATE ON matches
FOR EACH ROW
BEGIN
    IF NEW.status = 'COMPLETED' AND OLD.status != 'COMPLETED' THEN
        -- Update home team stats
        IF NEW.home_score > NEW.away_score THEN
            UPDATE teams SET
                wins = wins + 1,
                goals_for = goals_for + NEW.home_score,
                goals_against = goals_against + NEW.away_score,
                points = points + 3,
                updated_at = CURRENT_TIMESTAMP
            WHERE team_id = NEW.home_team_id;
            UPDATE teams SET
                losses = losses + 1,
                goals_for = goals_for + NEW.away_score,
                goals_against = goals_against + NEW.home_score,
                updated_at = CURRENT_TIMESTAMP
            WHERE team_id = NEW.away_team_id;
        ELSEIF NEW.home_score < NEW.away_score THEN
            UPDATE teams SET
                losses = losses + 1,
                goals_for = goals_for + NEW.home_score,
                goals_against = goals_against + NEW.away_score,
                updated_at = CURRENT_TIMESTAMP
            WHERE team_id = NEW.home_team_id;
            UPDATE teams SET
                wins = wins + 1,
                goals_for = goals_for + NEW.away_score,
                goals_against = goals_against + NEW.home_score,
                points = points + 3,
                updated_at = CURRENT_TIMESTAMP
            WHERE team_id = NEW.away_team_id;
        ELSE
            UPDATE teams SET
                draws = draws + 1,
                goals_for = goals_for + NEW.home_score,
                goals_against = goals_against + NEW.away_score,
                points = points + 1,
                updated_at = CURRENT_TIMESTAMP
            WHERE team_id = NEW.home_team_id;
            UPDATE teams SET
                draws = draws + 1,
                goals_for = goals_for + NEW.away_score,
                goals_against = goals_against + NEW.home_score,
                points = points + 1,
                updated_at = CURRENT_TIMESTAMP
            WHERE team_id = NEW.away_team_id;
        END IF;
    END IF;
END //

DELIMITER ;
