[![Backend Build](https://github.com/BAM-soft1/pet-shelter/actions/workflows/backend-build.yml/badge.svg)](https://github.com/BAM-soft1/pet-shelter/actions/workflows/backend-build.yml)
[![Frontend Build](https://github.com/BAM-soft1/pet-shelter/actions/workflows/frontend-build.yml/badge.svg)](https://github.com/BAM-soft1/pet-shelter/actions/workflows/frontend-build.yml)
[![Deploy Pet Shelter](https://github.com/BAM-soft1/pet-shelter/actions/workflows/deploy.yml/badge.svg)](https://github.com/BAM-soft1/pet-shelter/actions/workflows/deploy.yml)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=BAM-soft1_pet-shelter&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=BAM-soft1_pet-shelter)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=BAM-soft1_pet-shelter&metric=coverage)](https://sonarcloud.io/summary/new_code?id=BAM-soft1_pet-shelter)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=BAM-soft1_pet-shelter&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=BAM-soft1_pet-shelter)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=BAM-soft1_pet-shelter&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=BAM-soft1_pet-shelter)

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
