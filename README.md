# Pet Shelter - Multi-Database Deployment

Full-stack pet shelter application with Spring Boot backend supporting MySQL, MongoDB, and Neo4j databases

## Quick Start

### Local Development

1. **Clone the repository**

   ```bash
   git clone https://github.com/BAM-soft1/pet-shelter.git
   cd pet-shelter
   ```

2. **Create .env file**

   ```bash
   cp .env.sample .env
   # Edit .env with your local configuration
   ```

3. **Start databases locally**

   ```bash
   docker compose -f docker-compose.dev.yml --profile db up -d
   ```

4. **Start application locally**

   ```bash
   docker compose -f docker-compose.dev.yml --profile app up -d
   ```

5. **Access the application**
   - Frontend: http://localhost
   - Backend API: http://localhost/api
   - Neo4j Browser: http://localhost:7474
