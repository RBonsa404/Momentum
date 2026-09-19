# 🚀 Guide de Déploiement Complet - Momentum

Ce guide fournit des instructions pas-à-pas très détaillées pour déployer l'application **Momentum** (Backend Spring Boot 4.1.1 + Frontend Angular 19.2 + Base de données PostgreSQL 16) sur des plateformes d'hébergement cloud gratuites (comme **Railway.app**, **Fly.io**, **Render.com**, **Vercel** / **Netlify**).

---

## 🏗️ Architecture Monolithe Préparée

Grâce au regroupement de l'architecture microservices en un monolithe serveur autonome (`momentum-server`) :
- **1 seul service Web backend** à déployer (au lieu de 10+ microservices).
- **1 seule base de données PostgreSQL 16** (au lieu de 7 bases séparées).
- **0 broker de message (RabbitMQ)** ni serveur de découverte (Eureka) requis.
- **Frontend Angular 19.2** déployable sur n'importe quel CDN gratuit (Vercel, Netlify, Cloudflare Pages) ou hébergé avec le backend.

---

## 🟢 Option 1 : Déploiement Gratuit sur Railway.app (Recommandé)

Railway.app offre une prise en main très simple avec support Docker et PostgreSQL natif.

### Étape 1 : Créer la Base de Données PostgreSQL
1. Connectez-vous sur [Railway.app](https://railway.app/).
2. Cliquez sur **+ New Project** ➔ **Provision PostgreSQL**.
3. Une fois la base créée, cliquez dessus, allez dans l'onglet **Variables** et copiez la valeur de **`DATABASE_URL`** (format: `postgresql://postgres:pass@...:5432/railway`).

### Étape 2 : Déployer le Backend `momentum-server`
1. Dans le même projet Railway, cliquez sur **+ New** ➔ **GitHub Repo** ➔ sélectionnez `RBonsa404/Momentum`.
2. Cliquez sur **Settings** du service :
   - **Root Directory** : `backend/momentum-server`
   - **Build Command** : Railway détectera automatiquement le `Dockerfile` présent dans le dossier.
3. Allez dans l'onglet **Variables** du service et ajoutez :
   - `SPRING_PROFILES_ACTIVE` = `prod`
   - `DATABASE_URL` = `${{Postgres.DATABASE_URL}}` *(ou collez le lien JDBC format: `jdbc:postgresql://host:port/db`)*
   - `POSTGRES_USER` = `${{Postgres.POSTGRES_USER}}`
   - `POSTGRES_PASSWORD` = `${{Postgres.POSTGRES_PASSWORD}}`
   - `JWT_SECRET` = `<une_cle_secrete_aleatoire_d_au_moins_256_bits>`
   - `CORS_ORIGINS` = `https://votre-frontend.vercel.app,http://localhost:4200`
4. Allez dans **Networking** et cliquez sur **Generate Domain** pour obtenir l'URL publique de votre backend (ex: `https://momentum-server-production.up.railway.app`).

---

## ✈️ Option 2 : Déploiement Gratuit sur Fly.io

Fly.io permet un déploiement basé sur Docker très performant.

### Étape 1 : Installer le CLI Fly
```bash
powershell -Command "iwr https://fly.io/install.ps1 -useb | iex"
fly auth login
```

### Étape 2 : Créer l'application et la Base PostgreSQL
```bash
cd backend/momentum-server

# 1. Créer la base de données PostgreSQL gratuite sur Fly
fly postgres create --name momentum-db --region cdg

# 2. Initialiser l'application backend
fly launch --no-deploy --name momentum-server

# 3. Lier la base de données au backend
fly postgres attach --app momentum-server momentum-db

# 4. Configurer les variables d'environnement secrètes
fly secrets set JWT_SECRET="une_cle_tres_secrete_d_au_moins_256_bits_123456" \
                CORS_ORIGINS="https://momentum-frontend.vercel.app"
```

### Étape 3 : Déployer
```bash
fly deploy
```

---

## 🌐 Option 3 : Déploiement du Frontend Angular 19.2 (Vercel / Netlify)

### Sur Vercel (Gratuit & Ultra rapide) :
1. Connectez-vous sur [Vercel.com](https://vercel.com).
2. Cliquez sur **Add New Project** ➔ Importez le dépôt `RBonsa404/Momentum`.
3. Configurez les paramètres du projet :
   - **Framework Preset** : `Angular`
   - **Root Directory** : `frontend`
   - **Build Command** : `npm run build`
   - **Output Directory** : `dist/frontend/browser`
4. Ajoutez la variable d'environnement ou mettez à jour `environment.prod.ts` avec l'URL du backend Railway/Fly.io.
5. Cliquez sur **Deploy**.

---

## 🛠️ Schéma des Variables d'Environnement Produit

Voici le récapitulatif des variables configurables pour le serveur backend :

| Variable | Description | Exemple |
| :--- | :--- | :--- |
| `SPRING_PROFILES_ACTIVE` | Active le profil Spring de production | `prod` |
| `DATABASE_URL` | URL de connexion JDBC PostgreSQL | `jdbc:postgresql://db.railway.app:5432/railway` |
| `POSTGRES_USER` | Identifiant PostgreSQL | `postgres` |
| `POSTGRES_PASSWORD` | Mot de passe PostgreSQL | `secret_password` |
| `JWT_SECRET` | Clé Secrète pour la signature des JWT (HMAC-SHA256) | `cle_super_secrete_min_256_bits_123456789` |
| `CORS_ORIGINS` | Origines autorisées séparées par des virgules | `https://momentum.vercel.app` |

---

## ✅ Vérification Post-Déploiement

1. **Migration Flyway** : Flyway applique automatiquement le script `V1__init_schema.sql` lors du premier démarrage sur la base distante.
2. **Endpoint Health Check** : Testez `https://votre-backend-url/api/auth/me` (devrait retourner HTTP 401 Unauthorized sans token, confirmant que le serveur fonctionne).
3. **Test Frontend** : Connectez-vous sur le site frontend déployé et créez un compte de test.
