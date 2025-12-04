## Database Profiles

This project supports three database backends via Spring Profiles:

| Profile | Database      | Default |
| ------- | ------------- | ------- |
| `mysql` | MySQL (JPA)   | Yes     |
| `mongo` | MongoDB       | No      |
| `neo4j` | Neo4j (Graph) | No      |

## Migrations

Copy data from MySQL to MongoDB or Neo4j:

### MySQL → MongoDB

```bash
# 1. Seed MySQL first
docker compose -f docker-compose.dev.yml --profile mysql --profile app up -d
docker compose -f docker-compose.dev.yml down

# 2. Run migration
SPRING_PROFILE=migrate-mongo docker compose -f docker-compose.dev.yml --profile mysql --profile mongo --profile app up -d
```

### MySQL → Neo4j

```bash
# 1. Seed MySQL first
docker compose -f docker-compose.dev.yml --profile mysql --profile app up -d
docker compose -f docker-compose.dev.yml down

# 2. Run migration
SPRING_PROFILE=migrate-neo4j docker compose -f docker-compose.dev.yml --profile mysql --profile neo4j --profile app up -d
```

## Stop Services

```bash
docker compose -f docker-compose.dev.yml down
```
