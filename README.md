# ⚡ Momentum - Application Client-Serveur de Productivité

**Momentum** est une application web moderne de productivité, d'organisation quotidienne, de suivi d'habitudes, de journal de bord et d'analyse de la procrastination.

---

## 🎨 Design & Technologies

### 💻 Frontend (Angular 19.2)
- **Design System Futuriste** : Effets glassmorphism, animations fluides et thèmes sombres/lumineux.
- **Rendu 3D & DataViz** : Visualisations interactives avec Three.js, GSAP et ApexCharts.
- **Styling** : Tailwind CSS v3 & CSS personnalisés.

### ⚙️ Backend (`momentum-server`)
- **Framework** : Spring Boot 4.1.1 + Java 17.
- **Architecture Monolithe Standalone** : Communication in-memory directe, suppression des dépendances lourdes (RabbitMQ, Eureka, Spring Cloud).
- **Base de Données** : PostgreSQL 16 avec migrations automatique Flyway.
- **Sécurité** : Authentification JWT (Access Token 15 min + Refresh Token révocable 7 jours).

---

## 📚 Guides & Documentation

- 🧪 **[Tutoriel de Test en Local](./TUTO_TEST_LOCAL.md)** : Guide étape par étape pour installer et lancer l'application localement (Docker Compose ou Manuel).
- 🚀 **[Guide de Déploiement Cloud](./GUIDE_DEPLOIEMENT.md)** : Guide détaillé pour déployer gratuitement sur Railway.app, Fly.io, Render ou Vercel.

---

## 🚀 Démarrage Rapide

```bash
# 1. Lancer le backend et la base PostgreSQL avec Docker
cd backend/momentum-server
docker compose up --build

# 2. Dans un autre terminal, lancer le frontend Angular
cd frontend
npm install
npm start
```

Ouvrez ensuite votre navigateur sur **`http://localhost:4200`**.