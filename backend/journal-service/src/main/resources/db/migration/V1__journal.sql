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
