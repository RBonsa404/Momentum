# 🧪 Tutoriel de Test en Local - Momentum Monolithe

Ce guide détaille étape par étape la procédure pour installer, lancer et tester l'application **Momentum** (Backend Spring Boot 4.1.1 + Frontend Angular 19.2 + PostgreSQL 16) sur votre machine locale.

---

## 📋 Prérequis

Avant de commencer, assurez-vous d'avoir installé sur votre machine :
- **Java 17 JDK** (ou version supérieure) : `java -version`
- **Maven 3.8+** (ou utilitaire intégré) : `mvn -version`
- **Node.js 18+** et **npm** : `node -v` && `npm -v`
- **Docker Desktop** (recommandé pour lancer PostgreSQL en 1 clic) : `docker --version`
- *(Optionnel si sans Docker)* **PostgreSQL 16** installé localement.

---

## ⚡ Méthode 1 : Lancement Rapide avec Docker (Recommandé)

Cette méthode permet de démarrer la base de données PostgreSQL 16 et le serveur backend Spring Boot en une seule commande.

### 1. Démarrer le Backend & la Base de données
Ouvrez un terminal et exécutez :

```bash
cd backend/momentum-server
docker compose up --build
```

> **Que se passe-t-il ?**
> - Un conteneur PostgreSQL 16 est créé sur le port `5432`.
> - La base de données `momentum` est initialisée.
> - Le serveur backend Spring Boot compile, exécute la migration Flyway (`V1__init_schema.sql`) et démarre sur le port `8080`.

### 2. Démarrer le Frontend Angular
Ouvrez un deuxième terminal à la racine du projet :

```bash
cd frontend
npm install
npm start
```

Accédez ensuite à l'application via votre navigateur : **`http://localhost:4200`**

---

## 🛠️ Méthode 2 : Lancement Manuel (Sans Docker)

Si vous préférez exécuter les composants séparément sans Docker :

### Étape 1 : Préparer la base de données PostgreSQL
1. Démarrez votre serveur PostgreSQL 16 local.
2. Créez la base de données et l'utilisateur avec psql ou PgAdmin :
```sql
CREATE DATABASE momentum;
CREATE USER momentum WITH PASSWORD 'momentum';
GRANT ALL PRIVILEGES ON DATABASE momentum TO momentum;
```

### Étape 2 : Démarrer le serveur Backend
```bash
cd backend/momentum-server
mvn spring-boot:run
```
Le serveur backend démarre et écoute sur `http://localhost:8080`.

### Étape 3 : Démarrer le Frontend Angular
```bash
cd frontend
npm start
```

---

## 🧪 Scénario de Test des Fonctionnalités

Pour vérifier que l'intégralité du système fonctionne correctement :

### 1. Authentification & JWT
1. Ouvrez `http://localhost:4200`.
2. Connectez-vous ou inscrivez-vous avec un email/mot de passe.
3. Vérifiez dans l'onglet **Network (F12)** de votre navigateur que la requête `POST /api/auth/login` renvoie un `accessToken` et un `refreshToken`.
4. Observez l'envoi automatique du header `Authorization: Bearer <token>` sur les requêtes subséquentes.

### 2. Planning Quotidien (`/api/planning`)
1. Naviguez dans l'onglet **Planning**.
2. Ajoutez un **Bloc de Temps** (ex: *Focus Code* de 09:00 à 11:00).
3. Ajoutez une **Tâche** et des **Sous-tâches**.
4. Cochez une tâche comme terminée ou clôturez la journée (*Close Day*) : les tâches non terminées seront automatiquement reportées à la journée du lendemain.

### 3. Journal & Missed Detection (`/api/journal`)
1. Allez dans la section **Journal**.
2. Remplissez le bilan de la journée (Victoires, Difficultés, Gratitude, Mood, Énergie) et validez.
3. Vérifiez la mise à jour immédiate de vos statistiques.

### 4. Streaks & Jokers (`/api/streak`)
1. Consultez la série (*Streak*) actuelle.
2. Modifiez la règle de streak dans les paramètres (ex: définir les jours OFF ou le nombre de Jokers autorisés par mois).

### 5. Objectifs SMART & Habitudes (`/api/goals`)
1. Créez un **Objectif SMART** (titre, critères SMART, date d'échéance).
2. Ajoutez des **Milestones** et cochez-les : la barre de progression (%) s'ajuste dynamiquement.
3. Créez une **Habitude** et incrémentez son compteur quotidien.

### 6. Tableau de bord & Statistiques (`/api/stats`)
1. Ouvrez le **Dashboard**.
2. Observez le calcul automatique du **Score de Procrastination** et la grille **Heatmap** de votre activité.

---

## 🔍 Dépannage & Logs

- **Vérifier les logs backend** :
  - Docker : `docker logs -f momentum-server`
  - Console Spring Boot : vérifiez l'absence d'erreurs au démarrage.
- **Port 8080 déjà utilisé** :
  - Modifiez `server.port` dans `backend/momentum-server/src/main/resources/application.yml` ou passez `-Dserver.port=8081`.
- **Problème de CORS** :
  - Le serveur autorise par défaut `http://localhost:4200`. Si vous utilisez un autre port, ajustez la variable `CORS_ORIGINS` dans l'`application.yml`.
