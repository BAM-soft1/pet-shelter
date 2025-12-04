# Pet Shelter

Full-stack pet shelter application with:

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
