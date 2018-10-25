CREATE TABLE "Competition" (
  `id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `date` TEXT,
  `game_def` TEXT
);

CREATE TABLE "FRCMatch" (
  `id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `competition` INTEGER NOT NULL,
  `number` INTEGER NOT NULL,
  `time` INTEGER,

  UNIQUE(`number`, `competition`),
  FOREIGN KEY(`competition`) REFERENCES `Competition`(`id`)
);

CREATE TABLE `Alliance` (
  `id`	INTEGER PRIMARY KEY AUTOINCREMENT,
  `match`	INTEGER NOT NULL,
  `color`	TEXT NOT NULL CHECK(color = 'RED' OR color = 'BLUE'),
  FOREIGN KEY(`match`) REFERENCES `FRCMatch`(`id`)
);

CREATE TABLE "MatchRobot" (
  `id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `alliance` INTEGER NOT NULL,
  `team` INTEGER NOT NULL,

  FOREIGN KEY(`alliance`) REFERENCES `Alliance`(`id`)
);

CREATE TABLE "RobotEvent" (
  `time` INTEGER NOT NULL,
  `robot` INTEGER NOT NULL,
  `event_type` TEXT NOT NULL,
  `end_state` TEXT NOT NULL,

  FOREIGN KEY(`robot`) REFERENCES `MatchRobot`(`id`)
);

CREATE TRIGGER create_alliances_when_insert_match AFTER INSERT ON FRCMatch
BEGIN
  INSERT INTO Alliance("match", color) VALUES (NEW.id, "RED"), (NEW.id, "BLUE");
  END;
END;
