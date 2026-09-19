# Tutorial Complet Momentum

Ce guide couvre trois étapes essentielles :
1. 📤 Mettre à jour le code sur GitHub
2. 🧪 Tester l'application en local
3. 🚀 Déployer l'application sur un serveur gratuit

---

## 1. 📤 Mettre à jour le code sur GitHub

### Étape 1.1 : Initialiser le repository Git

Si ce n'est pas déjà fait, initialisez Git dans votre projet :

```bash
cd C:\Users\rachi\Desktop\DON'T_OPEN\MOMENTUM
git init
```

### Étape 1.2 : Créer le repository sur GitHub

1. Allez sur https://github.com/new
2. Nommez le repository : `momentum`
3. Cochez "Public" ou "Private" selon votre préférence
4. Ne cochez PAS "Initialize this repository with a README"
5. Cliquez sur "Create repository"

### Étape 1.3 : Lier votre repository local à GitHub

```bash
cd C:\Users\rachi\Desktop\DON'T_OPEN\MOMENTUM
git remote add origin https://github.com/VOTRE_USERNAME/momentum.git
```

Remplacez `VOTRE_USERNAME` par votre nom d'utilisateur GitHub.

### Étape 1.4 : Créer un fichier .gitignore

Créez un fichier `.gitignore` à la racine du projet pour exclure les fichiers inutiles :

```bash
# À la racine du projet
cd C:\Users\rachi\Desktop\DON'T_OPEN\MOMENTUM
```

Contenu du `.gitignore` :

```gitignore
# Backend
backend/target/
backend/.mvn/
backend/mvnw
backend/mvnw.cmd
backend/.settings/
backend/.project
backend/.classpath

# Frontend
frontend/node_modules/
frontend/dist/
frontend/.angular/
frontend/package-lock.json

# IDE
.idea/
.vscode/
*.swp
*.swo
*~

# Logs
*.log
logs/

# Environment
.env
.env.local
.env.*.local

# OS
.DS_Store
Thumbs.db

# Docker
postgres-data/
rabbitmq-data/
caddy-data/
caddy-config/
```

### Étape 1.5 : Faire le premier commit

```bash
cd C:\Users\rachi\Desktop\DON'T_OPEN\MOMENTUM

# Ajouter tous les fichiers
git add .

# Premier commit
git commit -m "Initial commit - Momentum app complete"

# Pousser sur GitHub (branche main)
git branch -M main
git push -u origin main
```

### Étape 1.6 : Mettre à jour après modifications

Pour les futures mises à jour :

```bash
cd C:\Users\rachi\Desktop\DON'T_OPEN\MOMENTUM

# Vérifier les modifications
git status

# Ajouter les fichiers modifiés
git add .

# Commiter avec message descriptif
git commit -m "Description des modifications"

# Pousser sur GitHub
git push
```

---

## 2. 🧪 Tester l'application en local

### Étape 2.1 : Prérequis

Assurez-vous d'avoir installé :
- **Java 17** : Vérifiez avec `java -version`
- **Node.js 22** : Vérifiez avec `node -v`
- **Docker & Docker Compose** : Vérifiez avec `docker --version` et `docker-compose --version`

### Étape 2.2 : Cloner le repository (si sur une autre machine)

```bash
git clone https://github.com/VOTRE_USERNAME/momentum.git
cd momentum
```

### Étape 2.3 : Démarrer l'infrastructure Docker

```bash
cd C:\Users\rachi\Desktop\DON'T_OPEN\MOMENTUM
docker-compose up postgres rabbitmq
```

Attendez que les services soient healthy (environ 30 secondes).

**Important : Si RabbitMQ refuse la connexion**
```bash
docker exec momentum-rabbitmq-1 rabbitmqctl add_user momentum momentum
docker exec momentum-rabbitmq-1 rabbitmqctl set_permissions -p / momentum ".*" ".*" ".*"
```

### Étape 2.4 : Construire le backend

```bash
cd backend
mvn clean install
```

Cela prend environ 2-3 minutes et exécute tous les tests.

### Étape 2.5 : Démarrer les services backend

Ouvrez plusieurs terminaux et lancez les services dans cet ordre :

**Terminal 1 - Eureka Server :**
```bash
cd backend/eureka-server
mvn spring-boot:run
```

**Terminal 2 - Config Server :**
```bash
cd backend/config-server
mvn spring-boot:run
```

**Terminal 3 - API Gateway :**
```bash
cd backend/api-gateway
mvn spring-boot:run
```

**Terminal 4 - Auth Service :**
```bash
cd backend/auth-service
mvn spring-boot:run
```

**Terminal 5 - Planning Service :**
```bash
cd backend/planning-service
mvn spring-boot:run
```

**Terminal 6 - Journal Service :**
```bash
cd backend/journal-service
mvn spring-boot:run
```

**Terminal 7 - Streak Service :**
```bash
cd backend/streak-service
mvn spring-boot:run
```

**Terminal 8 - Goal Service :**
```bash
cd backend/goal-service
mvn spring-boot:run
```

**Terminal 9 - Stats Service :**
```bash
cd backend/stats-service
mvn spring-boot:run
```

**Terminal 10 - Notification Service :**
```bash
cd backend/notification-service
mvn spring-boot:run
```

### Étape 2.6 : Installer et démarrer le frontend

**Nouveau terminal - Frontend :**
```bash
cd frontend
npm install
npm start
```

### Étape 2.7 : Tester l'application

1. **Accéder à l'application** : http://localhost:4200
2. **S'inscrire** : Créez un compte ou utilisez les credentials bootstrap
   - Email : `you@momentum.local`
   - Password : `ChangeMeNow!`
3. **Tester les fonctionnalités** :
   - ✅ Dashboard - Vérifiez les stats et la heatmap
   - ✅ Planning - Créez des blocs horaires et des tâches
   - ✅ Journal - Soumettez une entrée journal
   - ✅ Streak - Vérifiez la visualisation 3D
   - ✅ Goals - Créez des objectifs et des habitudes

### Étape 2.8 : Vérifier les endpoints de monitoring

- **Eureka Dashboard** : http://localhost:8761
- **Config Server** : http://localhost:8888
- **RabbitMQ Management** : http://localhost:15672 (momentum/momentum)
- **API Gateway** : http://localhost:8080

### Étape 2.9 : Arrêter tous les services

```bash
# Ctrl+C dans chaque terminal
# Ou pour Docker :
docker-compose down
```

---

## 3. 🚀 Déployer l'application sur un serveur gratuit

### Option A : Oracle Cloud Always Free (Recommandé)

Oracle Cloud offre un serveur gratuit (Always Free) parfait pour cette application.

#### Étape 3A.1 : Créer un compte Oracle Cloud

1. Allez sur https://www.oracle.com/cloud/free/
2. Créez un compte (nécessite une carte bancaire mais pas de frais)
3. Attendez l'activation du compte (quelques minutes)

#### Étape 3A.2 : Créer une instance Compute

1. Connectez-vous à Oracle Cloud Console
2. Allez dans "Compute" → "Instances"
3. Cliquez sur "Create Instance"
4. Configurez :
   - **Name** : `momentum-app`
   - **Compartment** : Choisissez votre compartment
   - **Shape** : `VM.Standard.E2.1.Micro` (Gratuit)
   - **Operating System** : `Oracle Linux` ou `Ubuntu`
   - **SSH Keys** : Ajoutez votre clé SSH publique
5. Cliquez sur "Create"

#### Étape 3A.3 : Connecter au serveur

```bash
ssh -i ~/.ssh/votre_cle.pem ubuntu@IP_PUBLIQUE_DU_SERVEUR
```

#### Étape 3A.4 : Installer Docker sur le serveur

```bash
# Mettre à jour le système
sudo apt update && sudo apt upgrade -y

# Installer Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Ajouter l'utilisateur au groupe docker
sudo usermod -aG docker $USER

# Installer Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Vérifier l'installation
docker --version
docker-compose --version
```

#### Étape 3A.5 : Cloner le repository

```bash
# Installer Git
sudo apt install git -y

# Cloner le repository
git clone https://github.com/VOTRE_USERNAME/momentum.git
cd momentum
```

#### Étape 3A.6 : Configurer les variables d'environnement

Créez un fichier `.env` :

```bash
nano .env
```

Contenu du `.env` :

```env
POSTGRES_USER=momentum_secure_password
POSTGRES_PASSWORD=another_secure_password
RABBITMQ_USER=momentum_rabbit
RABBITMQ_PASSWORD=secure_rabbit_password
JWT_SECRET=votre_jwt_secret_256_bits_très_long_et_sécurisé
CORS_ORIGINS=https://votre-domaine.com
BOOTSTRAP_USER_EMAIL=you@momentum.local
BOOTSTRAP_USER_PASSWORD=ChangeMeNow!
DOMAIN=votre-domaine.com
```

**IMPORTANT** : Remplacez les mots de passe par des valeurs sécurisées.

#### Étape 3A.7 : Modifier docker-compose pour production

Le fichier `docker-compose.prod.yml` est déjà configuré. Assurez-vous que le `DOMAIN` est correctement défini.

#### Étape 3A.8 : Lancer l'application

```bash
# Construire et démarrer tous les services
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d --build

# Vérifier les logs
docker-compose logs -f
```

#### Étape 3A.9 : Configurer le nom de domaine

1. Achetez un domaine ou utilisez un sous-domaine gratuit
2. Configurez les DNS pour pointer vers l'IP publique de votre serveur
3. Caddy configurera automatiquement HTTPS avec Let's Encrypt

#### Étape 3A.10 : Accéder à l'application

- **Application** : https://votre-domaine.com
- **API** : https://votre-domaine.com/api/*
- **Monitoring** : Configurez un firewall pour restreindre l'accès

---

### Option B : Render (Alternative plus simple)

Render offre un déploiement gratuit simplifié pour les applications web.

#### Étape 3B.1 : Préparer pour Render

Pour Render, vous devrez séparer le frontend et le backend car Render ne supporte pas facilement Docker Compose multi-services sur le plan gratuit.

**Créez un `render.yaml` à la racine :**

```yaml
services:
  - type: web
    name: momentum-frontend
    env: node
    buildCommand: cd frontend && npm install && npm run build
    startCommand: cd frontend && npm start
    envVars:
      - key: NODE_ENV
        value: production
```

**Note** : Pour Render, vous devrez utiliser des services externes pour PostgreSQL et RabbitMQ (ou utiliser des plans payants).

---

### Option C : Railway (Alternative)

Railway offre un déploiement gratuit avec support Docker.

#### Étape 3C.1 : Créer un compte Railway

1. Allez sur https://railway.app/
2. Créez un compte avec GitHub
3. Importez votre repository

#### Étape 3C.2 : Configurer les variables d'environnement

Dans Railway, ajoutez les variables d'environnement nécessaires (POSTGRES_USER, POSTGRES_PASSWORD, etc.)

#### Étape 3C.3 : Déployer

Railway détectera automatiquement le `docker-compose.yml` et déploiera les services.

---

## 🔍 Vérification et Monitoring

### Vérifier le déploiement

```bash
# Sur le serveur
docker-compose ps
docker-compose logs

# Vérifier les health checks
curl http://localhost:8761/actuator/health
curl http://localhost:8888/actuator/health
curl http://localhost:8080/actuator/health
```

### Monitoring de base

```bash
# Voir l'utilisation des ressources
htop

# Voir les logs en temps réel
docker-compose logs -f

# Redémarrer un service spécifique
docker-compose restart api-gateway
```

---

## 🛠️ Dépannage

### Problèmes GitHub

**Erreur d'authentification :**
```bash
# Utiliser SSH au lieu de HTTPS
git remote set-url origin git@github.com:VOTRE_USERNAME/momentum.git
```

**Fichiers trop volumineux :**
```bash
# Nettoyer le cache Git
git gc --prune=now --aggressive
```

### Problèmes Local

**Ports déjà utilisés :**
```bash
# Trouver le processus utilisant le port
netstat -ano | findstr :8080

# Tuer le processus
taskkill /PID <PID> /F
```

**Docker ne démarre pas :**
```bash
# Réinitialiser Docker
docker-compose down -v
docker system prune -a
docker-compose up
```

### Problèmes Déploiement

**Mémoire insuffisante sur Oracle Cloud :**
- L'instance gratuite (VM.Standard.E2.1.Micro) a 1 Go de RAM
- Tous les services peuvent ne pas tenir
- Solution : Utiliser un plan payant ou optimiser les services

**Caddy HTTPS ne fonctionne pas :**
```bash
# Vérifier que le domaine pointe vers l'IP correcte
nslookup votre-domaine.com

# Vérifier les logs Caddy
docker-compose logs caddy
```

---

## 📚 Ressources Utiles

- **GitHub Docs** : https://docs.github.com
- **Docker Docs** : https://docs.docker.com
- **Oracle Cloud Free Tier** : https://www.oracle.com/cloud/free/
- **Render** : https://render.com
- **Railway** : https://railway.app

---

## ✅ Checklist Finale

**GitHub :**
- [ ] Repository créé sur GitHub
- [ ] Code poussé avec .gitignore
- [ ] Branche main configurée

**Local :**
- [ ] Infrastructure Docker démarrée
- [ ] Backend construit et tous les services démarrés
- [ ] Frontend construit et démarré
- [ ] Application accessible sur localhost:4200
- [ ] Toutes les fonctionnalités testées

**Déploiement :**
- [ ] Serveur gratuit provisionné
- [ ] Docker installé sur le serveur
- [ ] Repository cloné sur le serveur
- [ ] Variables d'environnement configurées
- [ ] Application déployée et accessible
- [ ] HTTPS configuré avec nom de domaine

---

## 🎉 Félicitations !

Vous avez maintenant :
- ✅ Le code sur GitHub
- ✅ L'application testée en local
- ✅ L'application déployée sur un serveur gratuit

**Bonne utilisation de Momentum !** 🚀