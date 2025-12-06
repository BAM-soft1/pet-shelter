# Pet Shelter Backend

## Database Profiles

This project supports three database backends via Spring Profiles:

| Profile         | Database      | Description                        |
| --------------- | ------------- | ---------------------------------- |
| `mysql`         | MySQL (JPA)   | Default relational database        |
| `mongo`         | MongoDB       | Document-based NoSQL database      |
| `neo4j`         | Neo4j         | Graph database                     |
| `migrate-mongo` | MySQL + Mongo | Migration profile: MySQL → MongoDB |
| `migrate-neo4j` | MySQL + Neo4j | Migration profile: MySQL → Neo4j   |

## Running the Application

### Using Docker Compose

Configure the `.env` file in the project root (`pet-shelter/.env`):

```env
# For MySQL (default)
SPRING_PROFILE_ACTIVE=mysql
MIGRATION_ENABLED=false

# For MongoDB
SPRING_PROFILE_ACTIVE=mongo
MIGRATION_ENABLED=false

# For Neo4j
SPRING_PROFILE_ACTIVE=neo4j
MIGRATION_ENABLED=false
```

Start the application:

```bash
# MySQL
docker compose -f docker-compose.dev.yml --profile mysql --profile app up -d

# MongoDB
docker compose -f docker-compose.dev.yml --profile mongo --profile app up -d

# Neo4j
docker compose -f docker-compose.dev.yml --profile neo4j --profile app up -d
```

---

## Database Migrations

Migrate data from MySQL to MongoDB or Neo4j.

### Prerequisites

- MySQL database with seeded data
- Docker and Docker Compose installed

---

### MySQL → MongoDB Migration

#### Step 1: Configure and Run Migration

Set the `.env` file:

```env
# .env file
SPRING_PROFILE_ACTIVE=migrate-mongo
MIGRATION_ENABLED=true
```

Start MySQL, MongoDB, and the backend together:

```bash
docker compose -f docker-compose.dev.yml --profile mysql --profile mongo --profile app up -d
```

The application will:

1. Start MySQL and MongoDB containers
2. Seed MySQL with initial data (via `InitData`)
3. Run migration to copy all data from MySQL to MongoDB
4. Log migration progress

Check the logs:

```bash
docker compose -f docker-compose.dev.yml logs -f backend
```

#### Step 2: Verify and Switch to MongoDB

After migration completes, update `.env` to use MongoDB only:

```env
# .env file
SPRING_PROFILE_ACTIVE=mongo
MIGRATION_ENABLED=false
```

Restart with MongoDB profile:

```bash
docker compose -f docker-compose.dev.yml --profile mysql --profile app down
docker compose -f docker-compose.dev.yml --profile mongo --profile app up -d
```

---

### MySQL → Neo4j Migration

#### Step 1: Configure and Run Migration

Set the `.env` file:

```env
# .env file
SPRING_PROFILE_ACTIVE=migrate-neo4j
MIGRATION_ENABLED=true
```

Start MySQL, Neo4j, and the backend together:

```bash
docker compose -f docker-compose.dev.yml --profile mysql --profile neo4j --profile app up -d
```

The application will:

1. Start MySQL and Neo4j containers
2. Seed MySQL with initial data (via `InitData`)
3. Run migration to copy all data from MySQL to Neo4j
4. Log migration progress

Check the logs:

```bash
docker compose -f docker-compose.dev.yml logs -f backend
```

#### Step 2: Verify and Switch to Neo4j

After migration completes, update `.env` to use Neo4j only:

```env
# .env file
SPRING_PROFILE_ACTIVE=neo4j
MIGRATION_ENABLED=false
```

Restart with Neo4j profile:

```bash
docker compose -f docker-compose.dev.yml --profile mysql --profile app down
docker compose -f docker-compose.dev.yml --profile neo4j --profile app up -d
```

---

## Database Connection Details

| Database | Port        | Credentials                  | Connection String                                              |
| -------- | ----------- | ---------------------------- | -------------------------------------------------------------- |
| MySQL    | 3307        | `root` / `rootpassword123`   | `jdbc:mysql://localhost:3307/pet_shelter`                      |
| MongoDB  | 27017       | `mongouser` / `mongopass123` | `mongodb://mongouser:mongopass123@localhost:27017/pet_shelter` |
| Neo4j    | 7474 / 7687 | `neo4j` / `neo4jpass123`     | `bolt://localhost:7687`                                        |

## Inspecting Database Contents

### MongoDB

Use `mongosh` to query the database:

```bash
# List all collections
docker exec mongodb mongosh -u mongouser -p mongopass123 --authenticationDatabase admin pet_shelter --eval "db.getCollectionNames()"

# View animals
docker exec mongodb mongosh -u mongouser -p mongopass123 --authenticationDatabase admin pet_shelter --eval "db.animals.find().pretty()"

# Count documents in a collection
docker exec mongodb mongosh -u mongouser -p mongopass123 --authenticationDatabase admin pet_shelter --eval "db.animals.countDocuments()"
```

### Neo4j

Open the Neo4j Browser at [http://localhost:7474](http://localhost:7474) and login with `neo4j` / `neo4jpass123`.

Run Cypher queries:

```cypher
// View all nodes
MATCH (n) RETURN n LIMIT 25

// View all animals
MATCH (a:Animal) RETURN a

// Count nodes by label
MATCH (n) RETURN labels(n), count(n)
```

## Stop Services

```bash
docker compose -f docker-compose.dev.yml down
```

## Clean Up (Remove Volumes)

```bash
docker compose -f docker-compose.dev.yml down -v
```
