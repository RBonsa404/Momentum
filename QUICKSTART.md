# Quick Start Guide

This guide will help you get Momentum running locally in 5 minutes.

## Prerequisites Check

Ensure you have installed:
- Java 17
- Node.js 22
- Docker & Docker Compose

## Step 1: Start Infrastructure (30 seconds)

```bash
docker-compose up postgres rabbitmq
```

This starts PostgreSQL and RabbitMQ with default credentials (momentum/momentum).

## Step 2: Build Backend (2 minutes)

```bash
cd backend
mvn clean install
```

This builds all 12 backend modules and runs tests. Total time: ~2.5 minutes.

## Step 3: Start Core Services (1 minute)

Open 3 separate terminals:

**Terminal 1 - Eureka:**
```bash
cd backend/eureka-server
mvn spring-boot:run
```

**Terminal 2 - Config Server:**
```bash
cd backend/config-server
mvn spring-boot:run
```

**Terminal 3 - API Gateway:**
```bash
cd backend/api-gateway
mvn spring-boot:run
```

## Step 4: Start Business Services (1 minute)

Open additional terminals for each service (you can start them in any order):

```bash
# Auth Service
cd backend/auth-service
mvn spring-boot:run

# Planning Service
cd backend/planning-service
mvn spring-boot:run

# Journal Service
cd backend/journal-service
mvn spring-boot:run

# Streak Service
cd backend/streak-service
mvn spring-boot:run

# Goal Service
cd backend/goal-service
mvn spring-boot:run

# Stats Service
cd backend/stats-service
mvn spring-boot:run

# Notification Service
cd backend/notification-service
mvn spring-boot:run
```

## Step 5: Start Frontend (1 minute)

```bash
cd frontend
npm install
npm start
```

The frontend will be available at http://localhost:4200

## Step 6: Access the Application

**Frontend:** http://localhost:4200
- Register a new account or use bootstrap credentials
- Default bootstrap email: you@momentum.local
- Default bootstrap password: ChangeMeNow!

**Monitoring:**
- Eureka Dashboard: http://localhost:8761
- Config Server: http://localhost:8888
- RabbitMQ Management: http://localhost:15672 (momentum/momentum)
- API Gateway: http://localhost:8080

## One-Command Docker Alternative

If you prefer Docker for everything:

```bash
docker-compose up --build
```

This builds and starts all services including the frontend. Total time: ~5-10 minutes.

## Troubleshooting

**Port conflicts:**
- Ensure ports 5432, 5672, 15672, 8761, 8888, 8080, 4200 are available
- Change ports in docker-compose.yml if needed

**Service not starting:**
- Check that postgres and rabbitmq are healthy first
- Check service logs for database connection errors
- Verify Eureka is running before starting dependent services

**Frontend build errors:**
- Delete node_modules and run `npm install` again
- Ensure Node.js 22 is installed
- Check angular.json configuration

**Backend build errors:**
- Ensure Java 17 is installed
- Verify Maven is configured correctly
- Check that Spring Boot 4.1.1 is available

## Next Steps

1. Create your first daily plan in the Planning page
2. Submit your first journal entry in the Journal page
3. Track your streak on the Streak page
4. Set up SMART goals on the Goals page
5. Monitor your progress on the Dashboard

For detailed documentation, see [README.md](README.md)