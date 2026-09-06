# Momentum

Application personnelle de suivi d’évolution quotidienne : agenda horaire, tâches imbriquées, récap du soir, streaks, objectifs SMART et dashboard (score de procrastination).

Stack figée : **Java 17**, **Spring Boot 4.1.1**, **Spring Cloud 2025.1.x**, PostgreSQL 16 (une base par service), RabbitMQ, Angular 19, Three.js, GSAP, ApexCharts.

## Démarrage local

1. Copier `.env.example` vers `.env` et changer le secret JWT.
2. Infra uniquement :

```bash
docker compose up postgres rabbitmq -d
```

3. Backend (JDK 17) :

```bash
cd backend
mvn -DskipTests package
```

Puis lancer Eureka (`8761`), Config (`8888`), Gateway (`8080`), puis les services métier.

4. Frontend :

```bash
cd frontend
npm install
npm start
```

SPA : `http://localhost:4200` (proxy `/api` → gateway `8080`).

Compte bootstrap : `you@momentum.local` / `ChangeMeNow!`

## Docker Compose complet

Compiler les JAR puis :

```bash
cd backend && mvn -DskipTests package
docker compose up --build
```

- Gateway : http://localhost:8080  
- Eureka : http://localhost:8761  
- RabbitMQ UI : http://localhost:15672  
- Frontend : http://localhost:4200  

## Tests

```bash
cd backend && mvn test
```

Couvre notamment : lockout JWT, récurrence, report de tâches, moteur de streak, formule de procrastination.

Déploiement web : [docs/DEPLOYMENT.md](docs/DEPLOYMENT.md)
