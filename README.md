[![Backend Build](https://github.com/BAM-soft1/pet-shelter/actions/workflows/backend-build.yml/badge.svg)](https://github.com/BAM-soft1/pet-shelter/actions/workflows/backend-build.yml)
[![Frontend Build](https://github.com/BAM-soft1/pet-shelter/actions/workflows/frontend-build.yml/badge.svg)](https://github.com/BAM-soft1/pet-shelter/actions/workflows/frontend-build.yml)
[![Deploy Pet Shelter](https://github.com/BAM-soft1/pet-shelter/actions/workflows/deploy.yml/badge.svg)](https://github.com/BAM-soft1/pet-shelter/actions/workflows/deploy.yml)

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=BAM-soft1_pet-shelter&metric=coverage)](https://sonarcloud.io/summary/new_code?id=BAM-soft1_pet-shelter)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=BAM-soft1_pet-shelter&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=BAM-soft1_pet-shelter)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=BAM-soft1_pet-shelter&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=BAM-soft1_pet-shelter)

# Pet Shelter - Multi-Database Deployment

- Frontend made with **React** + **TypeScript**
- Spring Boot backend with **MySQL**, **MongoDB**, and **Neo4j** support. Each database runs standalone.

## Quick Start

```bash
git clone https://github.com/BAM-soft1/pet-shelter.git
cd pet-shelter
cp .env.sample .env
```

## Running Standalone Databases

### MySQL (Default)

- Change SPRING_PROFILE_ACTIVE to = mysql in .env

```bash
docker compose -f docker-compose.dev.yml --profile mysql --profile app up -d
```

API: `http://localhost/api/*`

See Swagger Docs: `http://localhost:8080/swagger-ui/index.html`

### MongoDB

- Change SPRING_PROFILE_ACTIVE to = mongo in .env

```bash
docker compose -f docker-compose.dev.yml --profile mongo --profile app up -d
```

API: `http://localhost/api/mongo/*`

See Swagger Docs: `http://localhost:8080/swagger-ui/index.html`

### Neo4j

- Change SPRING_PROFILE_ACTIVE to = neo4j in .env

```bash
docker compose -f docker-compose.dev.yml --profile neo4j --profile app up -d
```

API: `http://localhost/api/neo4j/*`

See Swagger Docs: `http://localhost:8080/swagger-ui/index.html`
