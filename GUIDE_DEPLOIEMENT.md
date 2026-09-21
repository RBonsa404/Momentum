# 🚀 Guide de Déploiement Complet - Momentum

Ce guide fournit des instructions pas-à-pas ultra-détaillées pour déployer l'application **Momentum** (Backend Spring Boot 4.1.1 + Frontend Angular 19.2 + Base de données PostgreSQL 16) sur la plateforme Cloud **Railway.app** (ainsi que sur d'autres hébergeurs).

---

## 🏗️ Architecture du Projet

L'application **Momentum** est structurée autour d'un serveur monolithique moderne et autonome :
- **Backend** : Spring Boot 4.1.1 (`backend/momentum-server`), conteneurisé avec Maven Multi-Stage Dockerfile (Port 8080).
- **Base de données** : PostgreSQL 16 (Gestion des schémas et migrations automatiques via Flyway).
- **Frontend** : Angular 19.2 (`frontend`), servi via un conteneur Nginx (Port 80) ou déployé sur un CDN (Vercel, Netlify).

---

## 🟢 Tutoriel Déploiement Complet Pas-à-Pas sur Railway.app

### 📋 PRÉREQUIS
1. Un compte sur [Railway.app](https://railway.app/) (connexion avec GitHub recommandée).
2. Le dépôt GitHub du projet (`RBonsa404/Momentum`) poussé sur votre compte GitHub.

---

### ÉTAPE 1 : Créer un nouveau projet sur Railway
1. Connectez-vous sur la console [Railway Dashboard](https://railway.app/dashboard).
2. Cliquez sur le bouton **+ New Project** (ou **Create a New Project**).
3. Dans le menu contextuel qui apparaît, ne sélectionnez rien pour l'instant et passez à l'étape suivante.

---

### ÉTAPE 2 : Ajouter et configurer la base de données PostgreSQL
1. Dans votre canvas de projet Railway, cliquez sur **+ New** (ou appuyez sur `Cmd+K` / `Ctrl+K`).
2. Sélectionnez **Database** ➔ **Add PostgreSQL**.
3. Railway va automatiquement créer et démarrer une instance **PostgreSQL 16**.
4. Cliquez sur la carte **PostgreSQL** qui vient de s'afficher :
   - Allez dans l'onglet **Variables**.
   - Vous y trouverez les variables générées automatiquement par Railway : `PGHOST`, `PGPORT`, `PGDATABASE`, `POSTGRES_USER`, `POSTGRES_PASSWORD`, `DATABASE_URL`.
   - *Astuce JDBC Spring Boot* : Spring Boot requiert le préfixe `jdbc:postgresql://`. Nous utiliserons la syntaxe de référence Railway `${{Postgres.PGHOST}}` à l'étape 3.

---

### ÉTAPE 3 : Déployer le Backend `momentum-server`

1. **Importer le dépôt GitHub** :
   - Cliquez sur **+ New** dans votre projet Railway.
   - Sélectionnez **GitHub Repo**.
   - Choisissez le dépôt `RBonsa404/Momentum` (si demandé, autorisez Railway à accéder à votre d'accès GitHub).

2. **Configurer les paramètres de Build & Root Directory** :
   - Cliquez sur le service créé pour ouvrir son panneau de configuration.
   - Dans l'onglet **Settings** :
     - **Service Name** : Renommez le service en `momentum-server`.
     - Dans la section **Source** / **Build** :
       - **Root Directory** : Définissez `/backend/momentum-server` *(Très important : cela indique à Railway de construire le conteneur à partir du Dockerfile multi-stage localisé dans backend/momentum-server)*.
       - **Custom Build Command** : Laissez vide (Railway utilisera automatiquement le `Dockerfile`).
     - Dans la section **Deploy** :
       - **Healthcheck Path** : `/actuator/health`

3. **Configurer les Variables d'Environnement** :
   - Allez dans l'onglet **Variables** du service `momentum-server`.
   - Cliquez sur **Raw Editor** (ou ajoutez les clés/valeurs une par une) :

| Clé (Variable) | Valeur recommandée | Description / Remarques |
| :--- | :--- | :--- |
| `SPRING_PROFILES_ACTIVE` | `prod` | Active le profil `application-prod.yml` |
| `DATABASE_URL` | `jdbc:postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}` | URL de connexion JDBC vers le service Postgres Railway |
| `POSTGRES_USER` | `${{Postgres.POSTGRES_USER}}` | Utilisateur PostgreSQL injecté depuis le service Postgres |
| `POSTGRES_PASSWORD` | `${{Postgres.POSTGRES_PASSWORD}}` | Mot de passe PostgreSQL injecté |
| `JWT_SECRET` | `votre_cle_tres_secrete_d_au_moins_256_bits_123456789_momentum` | Clé HMAC-SHA256 pour sécuriser les tokens JWT |
| `CORS_ORIGINS` | `https://votre-frontend.up.railway.app,http://localhost:4200` | Origines autorisées (mettez à jour avec le domaine frontend définitif) |
| `PORT` | `8080` | Port d'écoute de l'application Spring Boot |

4. **Générer l'URL Publique (Domain)** :
   - Allez dans l'onglet **Networking** du service `momentum-server`.
   - Dans la section **Public Networking**, cliquez sur **Generate Domain**.
   - Railway va générer une URL HTTPS publique du type : `https://momentum-server-production-xxxx.up.railway.app`.
   - Copiez cette URL, elle servira à configurer l'accès API pour le Frontend.

---

### ÉTAPE 4 : Déployer le Frontend Angular (`momentum-web`)

Vous pouvez déployer le Frontend soit directement sur **Railway**, soit sur **Vercel**.

#### Option A : Déploiement Frontend sur Railway (recommandé pour tout centraliser)

1. **Ajouter le service Frontend** :
   - Dans le même projet Railway, cliquez sur **+ New** ➔ **GitHub Repo**.
   - Sélectionnez à nouveau le dépôt `RBonsa404/Momentum`.
2. **Configurer le service** :
   - Dans l'onglet **Settings** du nouveau service :
     - **Service Name** : `momentum-frontend`.
     - **Root Directory** : `/frontend`.
     - Railway détectera le `Dockerfile` Nginx multi-stage du dossier `frontend`.
3. **Générer le domaine Frontend** :
   - Dans **Networking** ➔ Cliquez sur **Generate Domain** (ex: `https://momentum-frontend-production.up.railway.app`).
4. **Mettre à jour CORS sur le Backend** :
   - Retournez sur le service `momentum-server` ➔ Onglet **Variables**.
   - Mettez à jour `CORS_ORIGINS` avec l'URL exacte de votre frontend Railway (ex: `https://momentum-frontend-production.up.railway.app`).
   - Railway va automatiquement redéployer le backend.

#### Option B : Déploiement Frontend sur Vercel (Gratuit & Ultra Rapide)

1. Connectez-vous sur [Vercel.com](https://vercel.com).
2. Cliquez sur **Add New** ➔ **Project** ➔ Importez `RBonsa404/Momentum`.
3. Configurez :
   - **Framework Preset** : `Angular`
   - **Root Directory** : `frontend`
   - **Build Command** : `npm run build`
   - **Output Directory** : `dist/momentum-web/browser`
4. Cliquez sur **Deploy**. Copiez l'URL obtenue (ex: `https://momentum.vercel.app`) et ajoutez-la à la variable `CORS_ORIGINS` dans Railway.

---

### ÉTAPE 5 : Validation & Test Post-Déploiement

1. **Vérifier les Logs de Déploiement Backend** :
   - Sur Railway, cliquez sur `momentum-server` ➔ Onglet **Deployments** ➔ Cliquez sur **View Logs**.
   - Vérifiez l'exécution automatique des scripts Flyway :
     `Flyway Community Edition ... Successfully applied 1 migration to schema 'public'`
   - Vérifiez l'affichage de la bannière Spring Boot et le message `Started MomentumServerApplication in X.XXX seconds`.

2. **Tester l'Endpoint de Santé (Health Check)** :
   - Ouvrez dans votre navigateur : `https://<votre-backend-railway>.up.railway.app/actuator/health`
   - Vous devez recevoir une réponse JSON : `{"status":"UP"}`.

3. **Tester l'application via le Frontend** :
   - Accédez à l'URL de votre Frontend (`https://<votre-frontend>.up.railway.app`).
   - Créez un nouveau compte utilisateur (Inscription/Register) puis connectez-vous.

---

## 🛠️ Récapitulatif des Variables d'Environnement (Backend)

| Variable | Description | Exemple |
| :--- | :--- | :--- |
| `SPRING_PROFILES_ACTIVE` | Active le profil Spring de production | `prod` |
| `DATABASE_URL` | URL JDBC de la base de données PostgreSQL | `jdbc:postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}` |
| `POSTGRES_USER` | Identifiant de connexion PostgreSQL | `${{Postgres.POSTGRES_USER}}` |
| `POSTGRES_PASSWORD` | Mot de passe de la base de données | `${{Postgres.POSTGRES_PASSWORD}}` |
| `JWT_SECRET` | Clé secrète de signature des tokens JWT (min 256 bits) | `cle_tres_secrete_et_longue_1234567890_jwt` |
| `CORS_ORIGINS` | Domaines frontend autorisés à communiquer avec l'API | `https://momentum-frontend.up.railway.app` |
| `PORT` | Port d'écoute HTTP du serveur backend | `8080` |

---

## ❓ Résolution des Problèmes Fréquents (Troubleshooting)

### 1. Erreur : `Driver failure / Cannot connect to database`
- **Cause** : Format de `DATABASE_URL` inadapté pour JDBC.
- **Solution** : Ne collez pas l'URL brute `postgres://...`. Utilisez la référence JDBC Railway :
  `jdbc:postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}`

### 2. Erreur : `CORS Policy Blocked` sur le Frontend
- **Cause** : Le domaine du frontend n'est pas déclaré dans `CORS_ORIGINS`.
- **Solution** : Allez dans les variables du service backend sur Railway et ajoutez l'URL exacte du frontend (en incluant `https://` sans slash à la fin) dans `CORS_ORIGINS`.

### 3. Erreur : `Out of Memory` lors de la compilation Maven
- **Cause** : Le build Maven dépasse les ressources du plan gratuit Railway.
- **Solution** : Le `Dockerfile` multi-stage dans `backend/momentum-server/Dockerfile` est optimisé avec Maven 3.9 et Java 17 Temurin Alpine pour réduire l'empreinte mémoire.

---

## ✈️ Alternatives d'Hébergement Cloud

- **Fly.io** : Utiliser `fly launch` dans `backend/momentum-server` avec une base Postgres Fly.
- **Render.com** : Déploiement Web Service Docker avec Render PostgreSQL.
- **Vercel / Netlify** : Idéal pour le Frontend Angular statique.

