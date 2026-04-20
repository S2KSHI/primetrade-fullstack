# PrimeTrade — Full Stack Task Management App

A secure, production-ready full-stack task management application built with **Spring Boot**, **React 19**, and **MongoDB Atlas**. Features JWT authentication, BCrypt password hashing, CRUD task management, and a modern dark/light mode UI — deployed live via Docker on Render.

<br/>

🌐 **Live Demo:** [https://primetrade-frontend-6l39.onrender.com](https://primetrade-frontend-6l39.onrender.com)  
⚙️ **Backend API:** [https://primetrade-fullstack.onrender.com/api/v1](https://primetrade-fullstack.onrender.com/api/v1)

> **Note:** Hosted on Render free tier — the first request may take ~50 seconds to wake up the backend instance.

---

## Features

- **JWT Authentication** — Secure register, login, and logout flow
- **BCrypt Password Hashing** — Passwords are never stored in plain text
- **Role-Based Access** — User and Admin roles supported
- **Task CRUD** — Create, read, update (status), and delete tasks
- **Protected Routes** — All task endpoints require a valid JWT token
- **Dark / Light Mode** — Toggle with theme preference saved to localStorage
- **Responsive UI** — Works on desktop and mobile
- **Dockerized Backend** — Consistent deployment via Docker on Render
- **API Versioning** — All endpoints under `/api/v1/`

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.2, Spring Security |
| Database | MongoDB Atlas (cloud) |
| Auth | JWT (jjwt 0.11.5) + BCrypt |
| Frontend | React 19, CSS3 |
| Deployment | Docker, Render.com |
| Build Tool | Maven (mvnw) |

---

## Project Structure

```
primetrade-fullstack/
├── demo/                          # Spring Boot backend
│   ├── src/main/java/com/primetrade/
│   │   ├── config/
│   │   │   └── SecurityConfig.java
│   │   ├── controller/
│   │   │   ├── AuthController.java
│   │   │   └── TaskController.java
│   │   ├── data/                  # DTOs
│   │   │   ├── AuthResponse.java
│   │   │   ├── LoginRequest.java
│   │   │   └── RegisterRequest.java
│   │   ├── model/
│   │   │   ├── User.java
│   │   │   └── Task.java
│   │   ├── repository/
│   │   │   ├── UserRepository.java
│   │   │   └── TaskRepository.java
│   │   ├── security/
│   │   │   ├── JwtUtil.java
│   │   │   └── JwtAuthFilter.java
│   │   ├── service/
│   │   │   ├── AuthService.java
│   │   │   └── TaskService.java
│   │   └── DemoApplication.java
│   ├── Dockerfile
│   └── pom.xml
│
└── fusion-ui/                     # React frontend
    ├── public/
    ├── src/
    │   ├── App.js
    │   ├── App.css
    │   └── index.js
    └── package.json
```

---

## API Endpoints

### Auth
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/v1/auth/register` | Register new user | No |
| POST | `/api/v1/auth/login` | Login, returns JWT token | No |

### Tasks
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/v1/tasks` | Get all tasks for logged-in user | Yes |
| POST | `/api/v1/tasks` | Create a new task | Yes |
| PUT | `/api/v1/tasks/{id}` | Update task status | Yes |
| DELETE | `/api/v1/tasks/{id}` | Delete a task | Yes |

### Request Headers (protected routes)
```
Authorization: Bearer <your_jwt_token>
```

---

## Running Locally

### Prerequisites
- Java 17+
- Node.js 18+
- MongoDB Atlas account (or local MongoDB)
- Maven

### Backend

```bash
cd demo

# Set your MongoDB URI in application.properties
# spring.data.mongodb.uri=mongodb+srv://<user>:<password>@cluster.mongodb.net/primetrade_db

./mvnw spring-boot:run
```

Backend runs on `http://localhost:8080`

### Frontend

```bash
cd fusion-ui

# Create .env.development
echo "REACT_APP_API_URL=http://localhost:8080/api/v1" > .env.development

npm install
npm start
```

Frontend runs on `http://localhost:3000`

---

## Environment Variables

### Backend (set in Render dashboard)

| Variable | Description |
|----------|-------------|
| `MONGODB_URI` | Full MongoDB Atlas connection string |
| `JWT_SECRET` | Secret key for signing JWT tokens (min 64 chars) |

### Frontend

| Variable | Description |
|----------|-------------|
| `REACT_APP_API_URL` | Base URL of the backend API |

---

## Deployment

### Backend — Docker on Render

The backend includes a `Dockerfile`. On Render:

1. Create a new **Web Service**
2. Connect your GitHub repo
3. Set **Root Directory** to `demo`
4. Add environment variables: `MONGODB_URI`, `JWT_SECRET`
5. Render auto-deploys on every push to `main`

### Frontend — Static Site on Render

1. Create a new **Static Site**
2. Connect your GitHub repo
3. Set **Root Directory** to `fusion-ui`
4. Build Command: `npm install && npm run build`
5. Publish Directory: `build`
6. Add environment variable: `REACT_APP_API_URL`

---

## Database Schema

### Users Collection
```json
{
  "_id": "ObjectId",
  "name": "string",
  "email": "string (unique, indexed)",
  "password": "string (BCrypt hashed)",
  "role": "USER | ADMIN"
}
```

### Tasks Collection
```json
{
  "_id": "ObjectId",
  "title": "string",
  "description": "string",
  "status": "PENDING | IN_PROGRESS | COMPLETED",
  "userId": "string (ref to User._id)",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

---

## Security Practices

- Passwords hashed with BCrypt before storage
- JWT tokens expire after 24 hours
- CORS configured for production origins
- Spring Security stateless session (no server-side sessions)
- Auth endpoints public; all task endpoints require valid JWT
- Input validation on all API requests

---

## Scalability Notes

- **Stateless backend** — can run multiple instances behind a load balancer
- **MongoDB Atlas** — scales horizontally with sharding
- **Docker** — easy to deploy to Kubernetes or ECS for auto-scaling
- **API versioning** (`/api/v1/`) allows non-breaking future upgrades
- **Service layer pattern** — easy to extract into microservices

---

## Author

**Sakshi Gupta**  
[GitHub](https://github.com/S2KSHI) · sg0438462@gmail.com

---

## License

This project was built as part of a Backend Developer Intern assignment for PrimeTrade.
