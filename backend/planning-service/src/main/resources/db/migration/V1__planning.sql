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
    status VARCHAR(32) NOT NULL,
    recurrence VARCHAR(32) NOT NULL,
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
