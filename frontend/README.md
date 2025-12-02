# 🐾 Pet Shelter – Fullstack Application

Velkommen til **Pet Shelter**, et webbaseret system til et dyreinternat.  
Applikationen gør det muligt for brugere at se, adoptere og registrere kæledyr, mens administratorer kan håndtere dyreoplysninger, adoptioner og brugere via et administrationspanel.

---

## Repository & Deployede Versioner

- [**Frontend repository:**](https://github.com/BAM-soft1/pet-shelter-frontend)
- [**Backend repository:** ](https://github.com/BAM-soft1/BackendPetShelter)
- [**Deployed Frontend:**](https://pet-shelter-frontend-i82h.onrender.com/)
- [**Deployed Backend:**](https://bam-shelter-backend-bggeadccfkhnbydg.germanywestcentral-01.azurewebsites.net/api/animal)

## API Docs:

- [**Swagger docs**](https://bam-shelter-backend-bggeadccfkhnbydg.germanywestcentral-01.azurewebsites.net/swagger-ui/index.html)

---

## Teknologier

### Frontend

- **React** + **Vite**
- **TypeScript**
- **Tailwind CSS**
- **React Router** til client-side routing
- **Axios** til API-kald
- **Miljøvariabler (.env)** til API-konfiguration

### Backend

- **Node.js** + **Express**
- **MySQL** database
- **Mysql-driver**
- **JWT Authentication**
- **dotenv** til konfiguration
- **CORS** middleware

### Database

- **MySQL** til persistent lagring af data

---

### Frontend

1. **Klon projektet**

   ```bash
   git clone https://github.com/BAM-soft1/pet-shelter-frontend.git
   cd pet-shelter-frontend


   ```

2. Installer afhængigheder

```
npm install
```

3. Opret .env-fil og tilføj følgende:

```
VITE_DEV_API_BASE_URL=http://localhost:8080/api
```

4. Start udviklingsserver

```
npm run dev
```

Frontend kører typisk på:
👉 http://localhost:5173

---

### Backend

1. Klon backend-repoet

```
git clone https://github.com/BAM-soft1/BackendPetShelter.git
cd BackendPetShelter
```

2. Installer afhængigheder

```
./mvnw clean install
```

3. Konfigurér følgende env variabler:

```
JDBC_DATABASE_URL=jdbc:mysql://localhost:3306/pet_shelter
JDBC_USERNAME=username
JDBC_PASSWORD=password
JWT_ACCESS_SECRET=test-access-secret-random-random-random-random-value123123
JWT_REFRESH_SECRET=test-access-secret-random-random-random-random-value123123
```

4. Opret MySQL-database

Åbn DataGrip eller et andet SQL-værktøj

Opret en database:

CREATE DATABASE petshelter;

Kør migrations eller lad ORM’en oprette tabeller ved første kørsel

5. Start backend-server

```
./mvnw spring-boot:run
```

API’et kører som standard på:
👉 http://localhost:3000
