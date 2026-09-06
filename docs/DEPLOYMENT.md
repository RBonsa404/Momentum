# Déploiement web Momentum

Cible recommandée **gratuite** pour un usage perso : **Oracle Cloud Always Free** (Ampere A1, jusqu’à ~24 Go RAM / 4 OCPU). Un seul VPS Docker Compose suffit. Les free tiers PaaS (Railway, Render, Fly) ne tiennent pas 8 JVM Spring + PostgreSQL + RabbitMQ.

Hors scope : Kubernetes.

## 1. Préparer l’instance

1. Créer un compte Oracle Cloud, un VCN, une instance Ampere (Ubuntu 24.04).
2. Security lists / NSG : ouvrir **22**, **80**, **443** uniquement. Ne pas exposer 5432, 5672, 8761, 8888, 8081–8087.
3. Associer une IP publique et un DNS (A record) vers cette IP.

```bash
sudo apt update && sudo apt install -y docker.io docker-compose-v2 git
sudo usermod -aG docker $USER
```

## 2. Secrets

Sur le serveur, cloner le repo puis :

```bash
cp .env.example .env
nano .env
```

Obligatoire :

- `JWT_SECRET` : au moins 32 octets (`openssl rand -base64 48`)
- `POSTGRES_PASSWORD`, `RABBITMQ_PASSWORD`
- `BOOTSTRAP_USER_EMAIL` / `BOOTSTRAP_USER_PASSWORD`
- `CORS_ORIGINS=https://ton-domaine.tld`
- `DOMAIN=ton-domaine.tld` (pour Caddy)

Ne jamais committer `.env`.

## 3. Build et lancement

Sur une machine de build (ou sur le VPS si assez de RAM) :

```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64   # ou le JDK 17 ARM du VPS
cd backend
mvn -DskipTests package
cd ..
docker compose -f docker-compose.yml -f docker-compose.prod.yml up -d --build
```

Caddy écoute 80/443, proxy `/api` vers `api-gateway:8080`, le reste vers le frontend Nginx. Les chemins `/actuator*` sont renvoyés en 404 depuis Internet.

Vérifier :

```bash
curl -I https://ton-domaine.tld
curl https://ton-domaine.tld/api/auth/login -X POST -H 'Content-Type: application/json' \
  -d '{"email":"...","password":"..."}'
```

## 4. Caddy / HTTPS

Fichier [infra/caddy/Caddyfile](../infra/caddy/Caddyfile). Caddy obtient le certificat Let’s Encrypt tout seul si le DNS pointe bien.

Variables d’environnement Caddy (`docker-compose.prod.yml`) : `DOMAIN`.

## 5. Backups PostgreSQL

Une base logique par service (`auth_db`, `planning_db`, `journal_db`, `streak_db`, `goal_db`, `stats_db`, `notification_db`).

```bash
mkdir -p /opt/momentum/backups
for db in auth_db planning_db journal_db streak_db goal_db stats_db notification_db; do
  docker compose exec -T postgres pg_dump -U momentum "$db" | gzip > "/opt/momentum/backups/${db}-$(date +%F).sql.gz"
done
```

Cron quotidien (crontab) :

```
15 2 * * * cd /opt/momentum && /opt/momentum/scripts/backup-pg.sh
```

Restauration :

```bash
gunzip -c auth_db-2026-09-06.sql.gz | docker compose exec -T postgres psql -U momentum -d auth_db
```

Conserver au moins 7 jours, hors du volume Docker.

## 6. Mises à jour

```bash
git pull
cd backend && mvn -DskipTests package && cd ..
docker compose -f docker-compose.yml -f docker-compose.prod.yml up -d --build
```

Les volumes Postgres / RabbitMQ / Caddy sont persistants.

## 7. Checklist sécurité

- JWT secret unique, long, jamais dans Git
- CORS limité au domaine HTTPS
- Actuator non exposé publiquement (Caddy 404)
- Postgres et RabbitMQ non publiés sur 0.0.0.0
- Mot de passe bootstrap changé après la première connexion (`/api/auth/change-password`)
- SSH par clé, fail2ban recommandé
- Mises à jour OS + images Docker

## 8. Ressources JVM

Sur Ampere 24 Go : laisser ~8 Go à Postgres/OS, ~1–1.5 Go par service Spring (`JAVA_TOOL_OPTIONS=-XX:MaxRAMPercentage=75`). Ajuster dans `docker-compose.prod.yml` si un service swap.
