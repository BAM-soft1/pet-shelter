import axiosWithAuth from "../security/axios";
import type { Species, Breed } from "../types/types";
import { API_URL } from "../settings";

const API_URL_SPECIES = `${API_URL}/species`;
const API_URL_BREED = `${API_URL}/breed`;

export const SpeciesService = {
  getAllSpecies: async (): Promise<Species[]> => {
    const response = await axiosWithAuth.get(API_URL_SPECIES);
    return response.data;
  },

  getSpeciesById: async (id: number): Promise<Species> => {
    const response = await axiosWithAuth.get(`${API_URL_SPECIES}/${id}`);
    return response.data;
  },
};

export const BreedService = {
  getAllBreeds: async (): Promise<Breed[]> => {
    const response = await axiosWithAuth.get(API_URL_BREED);
    return response.data;
  },

  getBreedById: async (id: number): Promise<Breed> => {
    const response = await axiosWithAuth.get(`${API_URL_BREED}/${id}`);
    return response.data;
  },

  getBreedsBySpecies: async (speciesId: number): Promise<Breed[]> => {
    const response = await axiosWithAuth.get(`${API_URL_BREED}/species/${speciesId}`);
    return response.data;
  },
};
