CREATE TABLE IF NOT EXISTS Person
(
  UserName VARCHAR(50) NOT NULL,
  Biography VARCHAR(2000),
  MemberSince DATE NOT NULL,
  LastLogin DATE NOT NULL,
  TotalPoints INT NOT NULL,
  AverageCompletion INT NOT NULL,
  SiteRank INT NOT NULL,
  Image BLOB,
  PRIMARY KEY (UserName)
);

CREATE TABLE IF NOT EXISTS Game
(
  GameName VARCHAR(50) NOT NULL,
  About VARCHAR(2000),
  Console VARCHAR(50) NOT NULL,
  Developer VARCHAR(50) NOT NULL,
  Publisher VARCHAR(50) NOT NULL,
  Genre VARCHAR(50) NOT NULL,
  ReleaseDate DATE NOT NULL,
  Image BLOB NOT NULL,
  PRIMARY KEY (GameName)
);

CREATE TABLE IF NOT EXISTS Achievement
(
  AchievementName VARCHAR(50) NOT NULL,
  RetroPoints INT NOT NULL,
  About VARCHAR(2000),
  TotalCollectable INT,
  Image BLOB NOT NULL,
  GameName VARCHAR(50) NOT NULL,
  PRIMARY KEY (AchievementName),
  FOREIGN KEY (GameName) REFERENCES Game(GameName)
);

CREATE TABLE IF NOT EXISTS PersonAccount
(
  Email VARCHAR(50) NOT NULL,
  Pass VARCHAR(50) NOT NULL,
  UserName VARCHAR(50) NOT NULL,
  PRIMARY KEY (Email),
  FOREIGN KEY (UserName) REFERENCES Person(UserName)
);

CREATE TABLE IF NOT EXISTS UserGame
(
  UserName VARCHAR(50) NOT NULL,
  GameName VARCHAR(50) NOT NULL,
  PRIMARY KEY (UserName, GameName),
  FOREIGN KEY (UserName) REFERENCES Person(UserName),
  FOREIGN KEY (GameName) REFERENCES Game(GameName)
);

CREATE TABLE IF NOT EXISTS UserAchievement
(
  Clear BOOL NOT NULL,
  UnlockDate DATE NOT NULL,
  TotalCollected INT,
  UserName VARCHAR(50) NOT NULL,
  AchievementName VARCHAR(50) NOT NULL,
  PRIMARY KEY (UserName, AchievementName),
  FOREIGN KEY (UserName) REFERENCES Person(UserName),
  FOREIGN KEY (AchievementName) REFERENCES Achievement(AchievementName)
);