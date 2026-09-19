# Antigravity Prompt - Momentum Client-Server Architecture Refactor

## 🎯 OBJECTIF

Transformer l'architecture microservices Momentum en une architecture **client-serveur classique** compatible avec les plateformes de déploiement gratuit (Railway.app, Fly.io), tout en **préservant absolument le design frontend Angular existant**.

## 📋 CONTEXTE ACTUEL

### Architecture Microservices (Actuelle)
- **Backend** : 10+ services Spring Boot 4.1.1 microservices
- **Infrastructure** : PostgreSQL (7 bases), RabbitMQ, Eureka, Config Server, API Gateway
- **Frontend** : Angular 19.2 avec design system complet (Three.js, GSAP, ApexCharts, Tailwind)
- **Problème** : Trop complexe pour déploiement gratuit (besoin de carte bancaire pour Oracle Cloud)

### Contraintes à Respecter
- ✅ **Préserver le design frontend Angular** (Three.js, GSAP, ApexCharts, Tailwind, composants UI) - **CRITIQUE**
- ✅ **Garder Spring Boot 4.1.1** (ne pas downgrader)
- ✅ **Garder Java 17**
- ✅ **Garder PostgreSQL 16**
- ✅ **Garder Angular 19.2**
- ✅ **Toutes les fonctionnalités doivent être préservées** (planning, journal, streak, goals, stats, notifications)
- ✅ **Architecture client-serveur classique pour déploiement gratuit** (Railway.app, Fly.io)

## 🏗️ NOUVELLE ARCHITECTURE CLIENT-SERVEUR

### Backend : Application Spring Boot Unique (Serveur)
- **Une seule application Spring Boot** contenant tous les endpoints
- **Une seule base de données PostgreSQL** (7 tables au lieu de 7 bases)
- **Architecture bien structurée par fonctionnalité** (packages auth, planning, journal, streak, goals, stats, notifications)
- **RabbitMQ optionnel** (peut être intégré en mode local ou simplifié)
- **Suppression de Eureka, Config Server, API Gateway** (non nécessaires en client-serveur)
- **Frontend Angular séparé** (Client HTTP classique vers le serveur)

### Frontend : Angular (Client HTTP)
- **Conserver intact** : 
  - Design system (glass-card, streak-orb, halo-background, pill-button, pill-nav, floating-badge)
  - Three.js pour 3D
  - GSAP pour animations
  - ApexCharts pour graphiques
  - Tailwind CSS pour styling
  - Dark-only theme avec accents ember/flame
- **Pages** : Dashboard, Planning, Journal, Streak, Goals (préservées)
- **API calls** : Adapter pour pointer vers le serveur Spring Boot unique
- **Communication** : Requêtes HTTP classiques vers le serveur backend

## 🔄 MIGRATION STRATÉGIE

### Étape 1 : Création du Serveur Spring Boot (Backend)
1. **Créer une nouvelle application Spring Boot** : `momentum-server`
2. **Intégrer tous les services dans une seule application** :
   - Copier les controllers de chaque service
   - Copier les services domain
   - Copier les repositories JPA
   - Copier les DTOs et mappers
   - Copier les RabbitMQ listeners (adapter pour local ou supprimer)
3. **Simplifier la configuration** :
   - Une seule configuration Spring Boot
   - Une seule datasource PostgreSQL
   - Supprimer la configuration Eureka, Config Server, Gateway
4. **Adapter les routes** : Préfixer toutes les routes (ex: `/api/planning/*`, `/api/journal/*`, etc.)

### Étape 2 : Migration Base de Données
1. **Fusionner les Flyway migrations** :
   - Combiner toutes les migrations en une seule
   - Renommer les tables si nécessaire pour éviter les conflits
   - Adapter les clés étrangères si besoin
2. **Créer une seule base de données** avec toutes les tables

### Étape 3 : Adaptation Frontend (Client)
1. **Modifier l'API** dans `api.ts` :
   - Changer les URLs de microservices vers l'URL du serveur unique
   - Conserver les DTOs existants
2. **Adapter le proxy** dans `proxy.conf.json` ou équivalent
3. **Tester toutes les fonctionnalités**

### Étape 4 : Suppression des dépendances microservices
1. **Supprimer du serveur** :
   - Spring Cloud Eureka
   - Spring Cloud Config Server
   - Spring Cloud Gateway
   - Spring Cloud LoadBalancer
2. **Conserver** :
   - Spring Boot
   - Spring Data JPA
   - Spring AMQP (RabbitMQ, si nécessaire)
   - Spring Actuator
   - Spring Validation

### Étape 5 : Adaptation RabbitMQ (Optionnel)
1. **Mode local** : Intégrer RabbitMQ en mode embedded ou utiliser une file d'attente en mémoire
2. **Ou simplifier** : Transformer la communication événementielle en appels directs internes au serveur

## 📁 STRUCTURE DU SERVEUR (BACKEND)

```
momentum-server/
├── src/main/java/com/momentum/server/
│   ├── MomentumServerApplication.java
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   ├── WebConfig.java
│   │   └── DatabaseConfig.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── PlanningController.java
│   │   ├── JournalController.java
│   │   ├── StreakController.java
│   │   ├── GoalController.java
│   │   ├── StatsController.java
│   │   └── NotificationController.java
│   ├── service/
│   │   ├── AuthService.java
│   │   ├── PlanningService.java
│   │   ├── JournalService.java
│   │   ├── StreakService.java
│   │   ├── GoalService.java
│   │   ├── StatsService.java
│   │   └── NotificationService.java
│   ├── domain/
│   │   ├── auth/
│   │   ├── planning/
│   │   ├── journal/
│   │   ├── streak/
│   │   ├── goals/
│   │   ├── stats/
│   │   └── notifications/
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── DayRepository.java
│   │   ├── JournalEntryRepository.java
│   │   ├── StreakRepository.java
│   │   ├── GoalRepository.java
│   │   ├── HabitRepository.java
│   │   ├── MilestoneRepository.java
│   │   ├── DailySnapshotRepository.java
│   │   └── NotificationRepository.java
│   ├── dto/
│   │   ├── auth/
│   │   ├── planning/
│   │   ├── journal/
│   │   ├── streak/
│   │   ├── goals/
│   │   ├── stats/
│   │   └── notifications/
│   ├── mapper/
│   │   ├── AuthMapper.java
│   │   ├── PlanningMapper.java
│   │   ├── JournalMapper.java
│   │   ├── StreakMapper.java
│   création
│   │   ├── GoalMapper.java
│   │   └── StatsMapper.java
│   └── messaging/
│       ├── EventPublisher.java
│       └── EventListener.java (si RabbitMQ utilisé)
├── src/main/resources/
│   ├── application.yml
│   ├── application-prod.yml
│   └── db/migration/
│       └── V1__init_schema.sql
└── pom.xml
```

## 🔧 DÉPENDANCES SIMPLIFIÉES

### Pom.xml du Serveur
```xml
<dependencies>
    <!-- Spring Boot -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    
    <!-- Database -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-database-postgresql</artifactId>
    </dependency>
    
    <!-- RabbitMQ (optionnel) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-amqp</artifactId>
    </dependency>
    
    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    
    <!-- MapStruct -->
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
    </dependency>
    
    <!-- Testing -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## 🎨 FRONTEND PRÉSERVATION

### CRITIQUE : Ne PAS TOUCHER AU DESIGN SYSTEM
- **Ne modifier aucun composant UI** : glass-card, streak-orb, halo-background, pill-button, pill-nav, floating-badge
- **Ne pas modifier les animations GSAP**
- **Ne pas modifier Three.js visualisations**
- **Ne pas modifier ApexCharts**
- **Ne pas modifier Tailwind configuration**
- **Ne pas modifier les couleurs/typographie**
- **Ne pas modifier les pages Angular**

### Modifications Frontend (Uniquement)
1. **Adapter l'API** dans `core/api.ts` : changer les URLs de microservices vers l'URL monolithique
2. **Adapter le proxy** : modifier `proxy.conf.json` pour pointer vers le monolithique
3. **Tester** : s'assurer que tout fonctionne comme avant

## 🚀 DÉPLOIEMENT SUR RAILWAY.APP

### Configuration Railway
1. **Frontend Railway** : `frontend/railway.json` (déjà créé)
2. **Backend Railway** : `backend/server/nixpacks.toml` (à créer)
3. **Variables d'environnement** : `POSTGRES_USER`, `POSTGRES_PASSWORD`, `JWT_SECRET`, `CORS_ORIGINS`
4. **Base de données Railway** : Créer un service PostgreSQL Railway

### Étapes de déploiement
1. **Pousser le code sur GitHub**
2. **Importer le repository sur Railway**
3. **Créer 2 services Railway** :
   - Frontend (Node.js, pointant vers le dossier frontend)
   - Backend (Java, pointant vers le serveur)
4. **Créer un service PostgreSQL Railway**
5. **Configurer les variables d'environnement**
6. **Déployer**

## 📋 CHECKLIST DE VALIDATION

### Frontend (Client)
- [ ] Design system préservé (composants UI, animations, Three.js, ApexCharts)
- [ ] Pages fonctionnelles (Dashboard, Planning, Journal, Streak, Goals)
- [ ] API calls adaptés pour le serveur unique
- [ ] Build frontend réussi

### Backend (Serveur)
- [ ] Tous les services intégrés dans le serveur unique
- [ ] Routes adaptées avec préfixes (/api/planning/*, etc.)
- [] Base de données unifiée avec toutes les tables
- [] Authentification JWT préservée
- [ ] Toutes les fonctionnalités préservées
- [ ] Tests existants adaptés ou recréés
- [ ] Build backend réussi

### Déploiement
- [ ] Configuration Railway créée
- [ Variables d'environnement configurées
- [ ] PostgreSQL Railway créé
- [ ] Frontend (Client) Railway déployé
- [ ] Backend (Serveur) Railway déployé
- [ ] Application fonctionnelle

## 🎯 INSTRUCTIONS SPÉCIFIQUES

### 1. Cloner le repository
```bash
git clone https://github.com/RBonsa404/Momentum.git
cd Momentum
```

### 2. Créer le serveur Spring Boot
```bash
# Créer une nouvelle application Spring Boot
cd backend
# Créer momentum-server avec la structure décrite ci-dessus
```

### 3. Migrer le code
- Copier tous les controllers des services existants
- Copier tous les services domain
- Copier tous les repositories
- Copier tous les DTOs et mappers
- Adapter les imports et packages
- Adapter les routes
- Fusionner les Flyway migrations

### 4. Simplifier la configuration
- Créer un seul `application.yml`
- Configurer une seule datasource
- Supprimer la configuration microservices

### 5. Adapter le frontend (client)
- Modifier `core/api.ts` pour l'URL du serveur
- Modifier le proxy
- Tester les appels API

### 6. Tester
- Lancer le serveur localement
- Lancer le frontend localement
- Tester toutes les fonctionnalités
- Vérifier que le design est préservé

### 7. Déployer sur Railway
- Pousser le code sur GitHub
- Importer sur Railway
- Configurer les services
- Déployer

## ⚠️ POINTS D'ATTENTION

1. **Design Frontend** : ABSOLUMENT PRÉSERVER - C'est la priorité absolue
2. **Spring Boot Version** : Garder 4.1.1
3. **Fonctionnalités** : Toutes doivent être préservées
4. **Données** : Migration de base de données soigneuse
5. **Tests** : Adapter ou recréer les tests existants
6. **Sécurité** : JWT authentification préservée

## 🎯 OBJECTIF FINAL

Une application Momentum fonctionnelle avec :
- ✅ Design frontend Angular préservé identique
- ✅ Architecture client-serveur classique (backend serveur + frontend client)
- ✅ Déployable gratuitement sur Railway.app
- ✅ Toutes les fonctionnalités préservées
- ✅ Spring Boot 4.1.1, Java 17, PostgreSQL 16, Angular 19.2

---

**NOTE : Si tu rencontres des problèmes de complexité ou de migration, priorise le frontend et crée un backend serveur minimal avec les fonctionnalités essentielles (auth, planning, journal) et ajoute les autres fonctionnalités progressivement.**