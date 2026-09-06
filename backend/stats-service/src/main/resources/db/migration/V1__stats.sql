CREATE TABLE daily_snapshots (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    day_date DATE NOT NULL,
    planned INTEGER NOT NULL DEFAULT 0,
    done INTEGER NOT NULL DEFAULT 0,
    unfinished INTEGER NOT NULL DEFAULT 0,
    postponed INTEGER NOT NULL DEFAULT 0,
    missed_journals INTEGER NOT NULL DEFAULT 0,
    submitted_journals INTEGER NOT NULL DEFAULT 0,
    mood INTEGER,
    energy INTEGER,
    streak_length INTEGER NOT NULL DEFAULT 0,
    UNIQUE (user_id, day_date)
);
