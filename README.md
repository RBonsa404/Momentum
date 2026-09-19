# Momentum - Personal Evolution Tracker

Momentum is a personal evolution tracker providing daily planning, guided journaling, streak tracking, SMART goals, habits, and procrastination analytics. Built with a microservices architecture and a dark-themed Angular UI.

## Architecture Overview

### Backend (Spring Boot 4.1.1 + Java 17)

**Microservices (12 modules):**
- `common` - Shared event types, payloads, and utilities
- `eureka-server` - Service discovery (Port 8761)
- `config-server` - Centralized configuration (Port 8888)
- `api-gateway` - API gateway with JWT auth (Port 8080)
- `auth-service` - JWT authentication and user management
- `planning-service` - Daily planning, time blocks, nested tasks
- `journal-service` - Guided evening journaling
- `streak-service` - Streak tracking with jokers and days off
- `goal-service` - SMART goals, milestones, habits
- `stats-service` - Statistics and dashboard analytics
- `notification-service` - Notifications and reminders

**Infrastructure:**
- PostgreSQL 16 - One database per service
- RabbitMQ 3.13 - Event-driven communication with DLX/DLQ
- Spring Cloud Config - Centralized configuration
- Spring Cloud Gateway - API routing and filtering
- Eureka - Service discovery
- Flyway - Database migrations
- Actuator - Health and metrics endpoints
- Micrometer - Distributed tracing

### Frontend (Angular 19.2)

**Features:**
- Dark-only design with ember/flame accent colors
- Three.js for 3D visualizations (background halo, streak orb)
- GSAP for fluid animations
- ApexCharts for dashboard graphs
- Tailwind CSS for styling
- Standalone components with lazy loading

**Pages:**
- Dashboard - Stats, procrastination score, heatmap
- Planning - Time blocks, nested tasks, drag & drop
- Journal - Guided evening journaling template
- Streak - 3D streak visualization with rules configuration
- Goals - SMART goals, milestones, habits tracking

## Quick Start

### Prerequisites
- Java 17
- Node.js 22
- Docker & Docker Compose

### Local Development

**1. Start Infrastructure:**
```bash
docker-compose up postgres rabbitmq
```

**2. Build Backend:**
```bash
cd backend
mvn clean install
```

**3. Start Services (one by one):**
```bash
# Terminal 1
cd eureka-server
mvn spring-boot:run

# Terminal 2
cd config-server
mvn spring-boot:run

# Terminal 3
cd api-gateway
mvn spring-boot:run

# Terminal 4
cd auth-service
mvn spring-boot:run

# Terminal 5
cd planning-service
mvn spring-boot:run

# Terminal 6
cd journal-service
mvn spring-boot:run

# Terminal 7
cd streak-service
mvn spring-boot:run

# Terminal 8
cd goal-service
mvn spring-boot:run

# Terminal 9
cd stats-service
mvn spring-boot:run

# Terminal 10
cd notification-service
mvn spring-boot:run
```

**4. Start Frontend:**
```bash
cd frontend
npm install
npm start
```

**5. Access the Application:**
- Frontend: http://localhost:4200
- API Gateway: http://localhost:8080
- Eureka Dashboard: http://localhost:8761
- Config Server: http://localhost:8888
- RabbitMQ Management: http://localhost:15672 (momentum/momentum)

### Docker Deployment

**Development:**
```bash
docker-compose up --build
```

**Production:**
```bash
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up --build
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and get JWT token

### Planning
- `GET /api/planning/days?date=YYYY-MM-DD` - Get day bundle
- `POST /api/planning/days/{id}/blocks` - Create time block
- `POST /api/planning/days/{id}/tasks` - Create task
- `POST /api/planning/tasks/{id}/complete` - Complete task
- `POST /api/planning/days/{id}/close` - Close day

### Journal
- `POST /api/journal` - Submit journal entry
- `GET /api/journal` - Get journal history

### Streak
- `GET /api/streak/current` - Get current streak
- `GET /api/streak/rules` - Get streak rules
- `PUT /api/streak/rules` - Update streak rules

### Goals
- `GET /api/goals` - Get all goals
- `POST /api/goals` - Create goal
- `GET /api/goals/{id}/milestones` - Get milestones
- `POST /api/goals/{id}/milestones` - Add milestone
- `GET /api/goals/habits` - Get habits
- `POST /api/goals/habits` - Create habit

### Stats
- `GET /api/stats/dashboard?period=week` - Get dashboard stats
- `GET /api/stats/streak` - Get streak data
- `GET /api/stats/procrastination` - Get procrastination score

## Event Flow

**RabbitMQ Events:**
- `momentum.planning.task-created.v1` - Task created
- `momentum.planning.task-completed.v1` - Task completed
- `momentum.planning.task-postponed.v1` - Task postponed
- `momentum.planning.day-closed.v1` - Day closed
- `momentum.journal.submitted.v1` - Journal submitted
- `momentum.journal.missed.v1` - Journal missed
- `momentum.streak.updated.v1` - Streak updated
- `momentum.goal.progress.v1` - Goal progress updated

**Dead Letter Queue:**
- `momentum.events.dlx` - DLX for failed events
- `momentum.events.dlq` - DLQ for retry/inspection

## Testing

**Backend Tests:**
```bash
cd backend
mvn test
```

**Frontend Tests:**
```bash
cd frontend
npm test
```

## Configuration

**Environment Variables:**
- `POSTGRES_USER` - PostgreSQL user (default: momentum)
- `POSTGRES_PASSWORD` - PostgreSQL password (default: momentum)
- `RABBITMQ_USER` - RabbitMQ user (default: momentum)
- `RABBITMQ_PASSWORD` - RabbitMQ password (default: momentum)
- `JWT_SECRET` - JWT secret key (CHANGE IN PRODUCTION)
- `CORS_ORIGINS` - CORS allowed origins
- `BOOTSTRAP_USER_EMAIL` - Bootstrap user email
- `BOOTSTRAP_USER_PASSWORD` - Bootstrap user password

## Database Schema

Each service has its own PostgreSQL database:
- `auth_db` - Users and authentication
- `planning_db` - Days, time blocks, tasks, subtasks
- `journal_db` - Journal entries
- `streak_db` - Streaks and rules
- `goal_db` - Goals, milestones, habits
- `stats_db` - Daily snapshots and statistics
- `notification_db` - Notifications and reminders

## Version Constraints

- **Spring Boot**: 4.1.1 (intentional - do not downgrade)
- **Spring Cloud**: 2025.1.3
- **Java**: 17
- **Angular**: 19.2
- **PostgreSQL**: 16
- **RabbitMQ**: 3.13

## Production Deployment

**Oracle Cloud Always Free Target:**
- Use docker-compose.prod.yml with Caddy reverse proxy
- Set DOMAIN environment variable
- Configure HTTPS with Let's Encrypt via Caddy
- Update JWT_SECRET with strong value
- Update BOOTSTRAP credentials

## License

MIT