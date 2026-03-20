# CandiFlow

Application SaaS de gestion de candidatures (Job Tracker) développée en Java 25 / Spring Boot 4.

---

## Stack technique

**Backend**
- Java 25
- Spring Boot 4.0.3
- Spring Security (JWT)
- JPA / Hibernate
- PostgreSQL
- Tests JUnit 5 + Mockito
- Docker

---

## Fonctionnalités

- Authentification JWT (register / login)
- Gestion des candidatures (CRUD complet)
- Statuts : `APPLIED`, `INTERVIEW`, `OFFER`, `REJECTED`
- Notes personnelles et lien vers l'offre
- Pagination et filtres dynamiques (statut, entreprise)
- Isolation des données par utilisateur
- Tests unitaires (AuthService, JwtService, ApplicationService, ApplicationSpecification)

---

## Prérequis

- Java 25
- Maven
- PostgreSQL
- Docker (optionnel)

---

## Installation

### 1. Cloner le projet

```bash
git clone https://github.com/ShinWolf/CandiFlow.git
cd CandiFlow
```

### 2. Configurer les variables d'environnement

Créer un fichier `application-prod.yml` ou définir les variables suivantes :

```bash
DB_URL=jdbc:postgresql://localhost:5432/candiflowdb
DB_USERNAME=postgres
DB_PASSWORD=your_password
JWT_SECRET=your_secret_key_min_32_chars
JWT_EXPIRATION=86400000
```

> Générer une clé JWT sécurisée :
> ```bash
> openssl rand -base64 32
> ```

### 3. Lancer l'application

```bash
./mvnw spring-boot:run
```

Ou en production :

```bash
java -jar candiflow.jar --spring.profiles.active=prod
```

---

## Endpoints API

### Auth

| Méthode | URL | Description | Auth requise |
|---------|-----|-------------|--------------|
| `POST` | `/api/auth/register` | Créer un compte | Non |
| `POST` | `/api/auth/login` | Se connecter, retourne un JWT | Non |

### Candidatures

| Méthode | URL | Description | Auth requise |
|---------|-----|-------------|--------------|
| `POST` | `/api/applications` | Créer une candidature | Oui |
| `GET` | `/api/applications` | Lister ses candidatures | Oui |
| `GET` | `/api/applications/{id}` | Détail d'une candidature | Oui |
| `PUT` | `/api/applications/{id}` | Modifier une candidature | Oui |
| `DELETE` | `/api/applications/{id}` | Supprimer une candidature | Oui |

### Paramètres de filtrage (GET /api/applications)

| Paramètre | Type | Description | Exemple |
|-----------|------|-------------|---------|
| `page` | int | Numéro de page (défaut: 0) | `?page=0` |
| `size` | int | Taille de page (défaut: 10) | `?size=5` |
| `status` | enum | Filtrer par statut | `?status=APPLIED` |
| `company` | string | Recherche par entreprise | `?company=Google` |

---

## Authentification

Toutes les routes `/api/applications/**` nécessitent un token JWT dans le header :

```
Authorization: Bearer <token>
```

---

## Exemples de requêtes

### Register

```json
POST /api/auth/register
{
  "email": "user@candiflow.com",
  "password": "password123"
}
```

### Login

```json
POST /api/auth/login
{
  "email": "user@candiflow.com",
  "password": "password123"
}
```

Réponse :
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### Créer une candidature

```json
POST /api/applications
{
  "company": "Google",
  "jobTitle": "Développeur Full Stack",
  "status": "APPLIED",
  "notes": "Candidature envoyée via LinkedIn",
  "offerUrl": "https://careers.google.com/jobs/123",
  "appliedAt": "2026-03-18"
}
```

---

## Configuration production

Points importants avant la mise en prod :

| Paramètre | Dev | Prod |
|-----------|-----|------|
| `ddl-auto` | `update` | `validate` |
| `show-sql` | `true` | `false` |
| `jwt.secret` | valeur en dur | variable d'environnement |
| `datasource` | valeur en dur | variables d'environnement |
| Logging | `debug` | `warn` |

---

## Lancer les tests

```bash
./mvnw test
```

---

## Structure du projet

```
src/
├── main/java/com/example/candiflow/
│   ├── config/          # SecurityConfig
│   ├── controller/      # AuthController, ApplicationController
│   ├── dto/             # DTOs requête / réponse
│   ├── entity/          # User, Application
│   ├── enums/           # Role, ApplicationStatus
│   ├── exception/       # UserException, ApplicationException
│   ├── filter/          # JwtAuthFilter
│   ├── repository/      # UserRepository, ApplicationRepository
│   ├── service/         # AuthService, ApplicationService, JwtService, CustomUserDetailsService
│   └── specification/   # ApplicationSpecification
└── test/java/com/example/candiflow/
    ├── service/         # AuthServiceTest, ApplicationServiceTest, JwtServiceTest
    └── specification/   # ApplicationSpecificationTest
```