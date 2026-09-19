-- ============================================================
-- Momentum Unified Schema — V1__init_schema.sql
-- Fusion de toutes les migrations des 7 microservices
-- Ordre : auth → planning → journal → streak → goals → stats → notifications
-- ============================================================

-- ===== AUTH =====
CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    locked BOOLEAN NOT NULL DEFAULT FALSE,
    failed_attempts INTEGER NOT NULL DEFAULT 0,
    locked_until TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users (id),
    token_hash VARCHAR(64) NOT NULL UNIQUE,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- ===== PLANNING =====
CREATE TABLE days (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    date DATE NOT NULL,
    closed BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE (user_id, date)
);

CREATE TABLE time_blocks (
    id UUID PRIMARY KEY,
    day_id UUID NOT NULL REFERENCES days (id),
    title VARCHAR(255) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);

CREATE TABLE tasks (
    id UUID PRIMARY KEY,
    day_id UUID NOT NULL REFERENCES days (id),
    time_block_id UUID,
    title VARCHAR(255) NOT NULL,
    notes TEXT,
    status VARCHAR(32) NOT NULL DEFAULT 'TODO',
    recurrence VARCHAR(32) NOT NULL DEFAULT 'NONE',
    postponed_count INTEGER NOT NULL DEFAULT 0,
    origin_task_id UUID
);

CREATE TABLE sub_tasks (
    id UUID PRIMARY KEY,
    task_id UUID NOT NULL REFERENCES tasks (id),
    title VARCHAR(255) NOT NULL,
    done BOOLEAN NOT NULL DEFAULT FALSE,
    sort_order INTEGER NOT NULL DEFAULT 0
);

-- ===== JOURNAL =====
CREATE TABLE journal_entries (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    day_date DATE NOT NULL,
    wins TEXT NOT NULL,
    struggles TEXT,
    gratitude TEXT,
    mood INTEGER NOT NULL,
    energy INTEGER NOT NULL,
    tags VARCHAR(512),
    submitted_at TIMESTAMP WITH TIME ZONE NOT NULL,
    UNIQUE (user_id, day_date)
);

CREATE TABLE tracked_users (
    user_id UUID PRIMARY KEY
);

-- ===== STREAK =====
CREATE TABLE streaks (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    current_length INTEGER NOT NULL DEFAULT 0,
    longest_length INTEGER NOT NULL DEFAULT 0,
    last_qualified_date DATE,
    status VARCHAR(32) NOT NULL DEFAULT 'INACTIVE'
);

CREATE TABLE streak_rules (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    off_weekdays VARCHAR(128) NOT NULL DEFAULT '',
    jokers_per_month INTEGER NOT NULL DEFAULT 2,
    jokers_remaining INTEGER NOT NULL DEFAULT 2,
    last_joker_reset DATE
);

-- ===== GOALS =====
CREATE TABLE goals (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    specific_text TEXT,
    measurable_text TEXT,
    achievable_text TEXT,
    relevant_text TEXT,
    due_date DATE,
    progress_percent INTEGER NOT NULL DEFAULT 0,
    linked_task_origin_id UUID
);

CREATE TABLE milestones (
    id UUID PRIMARY KEY,
    goal_id UUID NOT NULL REFERENCES goals (id),
    title VARCHAR(255) NOT NULL,
    due_date DATE,
    done BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE habits (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    current_streak INTEGER NOT NULL DEFAULT 0
);

-- ===== STATS =====
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

-- ===== NOTIFICATIONS =====
CREATE TABLE notifications (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    channel VARCHAR(32) NOT NULL DEFAULT 'IN_APP',
    title VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    read_flag BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- ===== INDEX =====
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);
CREATE INDEX idx_days_user_date ON days(user_id, date);
CREATE INDEX idx_tasks_day_id ON tasks(day_id);
CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_journal_entries_user_date ON journal_entries(user_id, day_date);
CREATE INDEX idx_daily_snapshots_user_date ON daily_snapshots(user_id, day_date);
CREATE INDEX idx_notifications_user_id ON notifications(user_id, created_at DESC);
CREATE INDEX idx_goals_user_id ON goals(user_id);
CREATE INDEX idx_habits_user_id ON habits(user_id);
