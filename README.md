# CandiFlow — Backend

API REST pour la gestion de candidatures, développée en Java 25 / Spring Boot 4.

---

## Stack technique

- Java 25
- Spring Boot 4.0.3
- Spring Security (JWT)
- JPA / Hibernate
- PostgreSQL
- Docker
- Tests JUnit 5 + Mockito

---

## Prérequis

- Java 25
- Maven
- Docker + Docker Compose

---

## Installation locale

### 1. Cloner le projet

```bash
git clone https://github.com/ShinWolf/CandiFlow.git
cd CandiFlow
```

### 2. Configurer les variables d'environnement

Crée un fichier `application.yml` dans `src/main/resources/` ou utilise celui existant pour le dev.

Pour la prod, crée un `.env.prod` à la racine :

```env
DB_URL=jdbc:postgresql://postgres:5432/candiflowdb
DB_USERNAME=candiflow
DB_PASSWORD=ton_mot_de_passe
JWT_SECRET=ta_cle_secrete_min_32_chars
JWT_EXPIRATION=86400000
```

> Génère une clé JWT sécurisée : `openssl rand -base64 32`

### 3. Lancer avec Docker

```bash
docker compose --env-file .env.prod up --build -d
```

### 4. Lancer en local sans Docker

```bash
./mvnw spring-boot:run
```

---

## Endpoints API

### Auth

| Méthode | URL | Description | Auth |
|---------|-----|-------------|------|
| `POST` | `/api/auth/register` | Créer un compte | Non |
| `POST` | `/api/auth/login` | Se connecter, retourne un JWT | Non |

### Candidatures

| Méthode | URL | Description | Auth |
|---------|-----|-------------|------|
| `POST` | `/api/applications` | Créer une candidature | Oui |
| `GET` | `/api/applications` | Lister ses candidatures | Oui |
| `GET` | `/api/applications/{id}` | Détail d'une candidature | Oui |
| `PUT` | `/api/applications/{id}` | Modifier une candidature | Oui |
| `DELETE` | `/api/applications/{id}` | Supprimer une candidature | Oui |

### Paramètres de filtrage (GET /api/applications)

| Paramètre | Type | Description |
|-----------|------|-------------|
| `page` | int | Numéro de page (défaut: 0) |
| `size` | int | Taille de page (défaut: 5) |
| `status` | enum | `APPLIED`, `INTERVIEW`, `OFFER`, `REJECTED` |
| `company` | string | Recherche par entreprise |

### Profil

| Méthode | URL | Description | Auth |
|---------|-----|-------------|------|
| `GET` | `/api/user/profile` | Récupérer son profil | Oui |
| `PATCH` | `/api/user/profile` | Modifier email / pseudo | Oui |
| `PATCH` | `/api/user/password` | Modifier le mot de passe | Oui |

### Dashboard

| Méthode | URL | Description | Auth |
|---------|-----|-------------|------|
| `GET` | `/api/dashboard/stats` | Stats des candidatures | Oui |

---

## Authentification

Toutes les routes sauf `/api/auth/**` nécessitent un token JWT dans le header :

```
Authorization: Bearer <token>
```

---

## Lancer les tests

```bash
./mvnw test
```

---

## Configuration production

| Paramètre | Dev | Prod |
|-----------|-----|------|
| `ddl-auto` | `update` | `validate` |
| `show-sql` | `true` | `false` |
| `jwt.secret` | valeur en dur | variable d'environnement |
| `datasource` | valeur en dur | variables d'environnement |
| Logging | `debug` | `warn` |

---

## Structure du projet

```
src/
├── main/java/com/example/candiflow/
│   ├── config/           # SecurityConfig
│   ├── controller/       # AuthController, ApplicationController, UserController, DashboardController
│   ├── dto/              # DTOs requête / réponse
│   ├── entity/           # User, Application
│   ├── enums/            # Role, ApplicationStatus
│   ├── exception/        # UserException, ApplicationException, GlobalExceptionHandler
│   ├── filter/           # JwtAuthFilter
│   ├── repository/       # UserRepository, ApplicationRepository
│   ├── service/          # AuthService, ApplicationService, JwtService, UserService, DashboardService
│   └── specification/    # ApplicationSpecification
└── test/java/com/example/candiflow/
    ├── service/          # AuthServiceTest, ApplicationServiceTest, JwtServiceTest
    └── specification/    # ApplicationSpecificationTest
```

---

## Déploiement

Le projet utilise **GitHub Actions** pour le déploiement automatique sur push sur `main`.

### Secrets GitHub requis

| Secret | Description |
|--------|-------------|
| `VPS_HOST` | IP du VPS |
| `VPS_USER` | Utilisateur SSH |
| `VPS_SSH_KEY` | Clé privée SSH |

### Initialisation de la base de données

Le script `src/main/resources/db/migration/init.sql` est monté automatiquement dans le container PostgreSQL au premier démarrage.