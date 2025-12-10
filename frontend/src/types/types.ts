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

type MedicalRecord = {
  id: number;
  animal: Animal;
  user: User;
  date: string;
  diagnosis: string;
  treatment: string;
  cost: number;
};

type MedicalRecordRequest = {
  animalId: number;
  userId: number;
  date: string;
  diagnosis: string;
  treatment: string;
  cost: number;
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
  firstName: string;
  lastName: string;
  phone: string | null;
  role: "ADMIN" | "STAFF" | "VETERINARIAN" | "ADOPTER" | "FOSTER" | "USER";
};

type AdoptionApplication = {
  id: number;
  user: User;
  animal: Animal;
  applicationDate: string;
  status: string;
  description: string;
  reviewedByUser: User | null;
  isActive: boolean;
};

type AdoptionApplicationRequest = {
  userId: number;
  animalId: number;
  description: string;
};

type AdoptionApplicationResponse = {
  id: number;
  userId: number;
  userName: string;
  animal: Animal;
  description: string;
  applicationDate: string;
  status: Status;
  reviewedByUserName: string | null;
  isActive: boolean;
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

type DogFact = string;

type Status = "AVAILABLE" | "ADOPTED" | "FOSTERED" | "DECEASED" | "PENDING" | "APPROVED" | "REJECTED";

export type {
  Animal,
  AnimalRequest,
  Species,
  Breed,
  User,
  Adoption,
  AdoptionApplicationRequest,
  AdoptionApplication,
  AdoptionApplicationResponse,
  AuthUser,
  LoginRequest,
  RegisterRequest,
  AuthResponse,
  MedicalRecord,
  MedicalRecordRequest,
  DogFact,

};
