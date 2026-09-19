CREATE TABLE streaks (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    current_length INTEGER NOT NULL DEFAULT 0,
    longest_length INTEGER NOT NULL DEFAULT 0,
    last_qualified_date DATE,
    status VARCHAR(32) NOT NULL
);

CREATE TABLE streak_rules (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    off_weekdays VARCHAR(128) NOT NULL DEFAULT '',
    jokers_per_month INTEGER NOT NULL DEFAULT 2,
    jokers_remaining INTEGER NOT NULL DEFAULT 2,
    last_joker_reset DATE
);
