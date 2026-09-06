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
