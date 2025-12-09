# Pet Shelter – Frontend

React + TypeScript frontend for the Pet Shelter application. Allows users to browse and adopt animals, while administrators manage animal information, adoptions, and users.

## Tech Stack

- **React** + **Vite**
- **TypeScript**
- **Tailwind CSS**

## Development

This frontend is part of the [Pet Shelter monorepo](https://github.com/BAM-soft1/pet-shelter).

### Quick Start

Run the full application with Docker Compose from the repo root:

```bash
git clone https://github.com/BAM-soft1/pet-shelter.git
cd pet-shelter
cp .env.sample .env
docker compose -f docker-compose.dev.yml --profile mysql --profile app up -d
```

See the [main README](../README.md) for details.

### Standalone Frontend Development

Run only the frontend:

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on http://localhost:5173
