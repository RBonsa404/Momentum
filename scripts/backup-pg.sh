#!/usr/bin/env bash
set -euo pipefail
mkdir -p /opt/momentum/backups
for db in auth_db planning_db journal_db streak_db goal_db stats_db notification_db; do
  docker compose exec -T postgres pg_dump -U "${POSTGRES_USER:-momentum}" "$db" \
    | gzip > "/opt/momentum/backups/${db}-$(date +%F).sql.gz"
done
