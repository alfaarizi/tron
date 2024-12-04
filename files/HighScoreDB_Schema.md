-- HIGHSCORE_DB SCHEMA

```sql
CREATE TABLE Player (
    playerno INT AUTO_INCREMENT,
    pname VARCHAR(30) NOT NULL,
    phash VARCHAR(255) NOT NULL,
    registerdate DATE NOT NULL,
    PRIMARY KEY (playerno)
);

CREATE TABLE Game (
    gameid INT AUTO_INCREMENT,
    gname VARCHAR(30) NOT NULL,
    releasedate DATE NOT NULL,
    PRIMARY KEY (gameid)
);

CREATE TABLE Sessions (
    sessionsid INT AUTO_INCREMENT,
    playerno INT NOT NULL,
    gameid INT NOT NULL,
    score INT CHECK (score >= 0),
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    PRIMARY KEY (sessionsid),
    FOREIGN KEY (playerno) REFERENCES Player(playerno) ON DELETE CASCADE,
    FOREIGN KEY (gameid) REFERENCES Game(gameid) ON DELETE CASCADE
);

CREATE TABLE Leaderboard ( 
    playerno INT NOT NULL,
    gameid INT NOT NULL,
    highscore INT CHECK (highscore >= 0),
    PRIMARY KEY (playerno, gameid),
    FOREIGN KEY (playerno) REFERENCES Player(playerno) ON DELETE CASCADE,
    FOREIGN KEY (gameid) REFERENCES Game(gameid) ON DELETE CASCADE
);
```