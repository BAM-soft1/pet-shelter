type Species = {
  id: number;
  name: string;
};

type Breed = {
  id: number;
  species: Species;
  name: string;
};


export type VaccinationType = {
  id: number;
  vaccineName: string;
  description: string;
  durationMonths: number;
  requiredForAdoption: boolean;
};

export type Vaccination = {
  id: number;
  animal: Animal;
  user: User;
  dateAdministered: string;
  vaccinationType: VaccinationType;
  nextDueDate: string;
};


export type VaccinationRequest = {
  animalId: number | null
  userId?: number | null;
  dateAdministered: string;
  vaccinationTypeId: number | null;
  nextDueDate: string;
}; 


export type VaccinationTypeRequest = {
  vaccineName: string;
  description: string;
  durationMonths: number;
  requiredForAdoption: boolean;
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

type Animal = {
  id: number;
  name: string;
  sex: string;
  species: Species;
  breed: Breed | null;
  birthDate: string; 
  intakeDate: string; 
  status: string;
  price: number;
  isActive: boolean;
  imageUrl?: string; 
};

type AnimalRequest = {
  name: string;
  sex: string;
  species: Species;
  breed: Breed | null;
  birthDate: string;
  intakeDate: string; 
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

export type {
  Animal,
  AnimalRequest,
  Species,
  Breed,
  User,
  Adoption,
  AuthUser,
  LoginRequest,
  RegisterRequest,
  AuthResponse,
  MedicalRecord,
  MedicalRecordRequest,
  DogFact,
};
