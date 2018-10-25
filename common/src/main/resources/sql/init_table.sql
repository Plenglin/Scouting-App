BEGIN TRANSACTION;

CREATE TABLE IF NOT EXISTS "Competition" (
  `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  `date` TEXT,
  `game_def` TEXT
);

CREATE TABLE IF NOT EXISTS "FRCMatch" (
  `competition` INTEGER NOT NULL,
  `number` INTEGER,
  `time` INTEGER,

  PRIMARY KEY(`number`, `competition`),
  FOREIGN KEY(`competition`) REFERENCES `Competition`(`id`)
);

CREATE TABLE IF NOT EXISTS `Alliance` (
  `id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  `match`	INTEGER NOT NULL,
  `color`	TEXT NOT NULL CHECK(color = 'red' OR color = 'blue'),
  FOREIGN KEY(`match`) REFERENCES `FRCMatch`(`number`)
);

CREATE TABLE IF NOT EXISTS "MatchRobot" (
  `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  `alliance` INTEGER NOT NULL,
  `team` INTEGER NOT NULL,

  FOREIGN KEY(`alliance`) REFERENCES `Alliance`(`id`)
);

CREATE TABLE IF NOT EXISTS "RobotEvent" (
  `time` INTEGER NOT NULL,
  `robot` INTEGER NOT NULL,
  `event_type` TEXT NOT NULL,
  `end_state` TEXT NOT NULL,

  FOREIGN KEY(`robot`) REFERENCES `MatchRobot`(`id`)
);

COMMIT;