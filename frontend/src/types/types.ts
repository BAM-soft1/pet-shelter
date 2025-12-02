// Backend entity types
type Species = {
  id: number;
  name: string;
};

type Breed = {
  id: number;
  species: Species;
  name: string;
};

// Animal type matching backend AnimalDTOResponse
type Animal = {
  id: number;
  name: string;
  sex: string;
  species: Species;
  breed: Breed | null;
  birthDate: string; // ISO date string
  intakeDate: string; // ISO date string
  status: string;
  price: number;
  isActive: boolean;
  imageUrl?: string; // Optional field for frontend
};

type AnimalRequest = {
  name: string;
  sex: string;
  species: Species;
  breed: Breed | null;
  birthDate: string; // ISO date string
  intakeDate: string; // ISO date string
  status: string;
  price: number;
  imageUrl?: string;
};

type User = {
  user_id: number;
  email: string;
  first_name: string;
  last_name: string;
  phone: string | null;
  role: "ADMIN" | "STAFF" | "VETERINARIAN" | "ADOPTER" | "FOSTER" | "USER";
};

type Adoption = {
  adoption_id: number;
  animal_name: string;
  species: string;
  breed: string | null;
  adopter_name: string;
  adopter_email: string;
  adopter_phone: string | null;
  adoption_date: string;
  days_since_adoption: number;
};

// Auth Types
type AuthUser = {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  phone: string | null;
  isActive: boolean;
  role: "ADMIN" | "STAFF" | "VETERINARIAN" | "ADOPTER" | "FOSTER" | "USER";
};

type LoginRequest = {
  email: string;
  password: string;
};

type RegisterRequest = {
  email: string;
  firstName: string;
  lastName: string;
  phone?: string | null;
  password: string;
};

type AuthResponse = {
  accessToken: string;
  tokenType: string;
  expiresInSeconds: number;
};

export type { Animal, AnimalRequest, Species, Breed, User, Adoption, AuthUser, LoginRequest, RegisterRequest, AuthResponse };
