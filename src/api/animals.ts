import axiosWithAuth from "../security/axios";
import type { Animal, AnimalRequest } from "../types/types";
import { API_URL } from "../settings";

const API_URL_ANIMALS = `${API_URL}/animal`; // Note: backend uses /api/animal

export const AnimalService = {
  getAnimals: async (): Promise<Animal[]> => {
    const response = await axiosWithAuth.get(API_URL_ANIMALS);
    return response.data;
  },

  getAnimalById: async (id: number): Promise<Animal> => {
    const response = await axiosWithAuth.get(`${API_URL_ANIMALS}/${id}`);
    return response.data;
  },

  createAnimal: async (animal: AnimalRequest): Promise<Animal> => {
    const response = await axiosWithAuth.post(`${API_URL_ANIMALS}/add`, animal);
    return response.data;
  },

  updateAnimal: async (id: number, animal: Partial<Animal>): Promise<Animal> => {
    const response = await axiosWithAuth.put(`${API_URL_ANIMALS}/update/${id}`, animal);
    return response.data;
  },

  deleteAnimal: async (id: number): Promise<void> => {
    await axiosWithAuth.delete(`${API_URL_ANIMALS}/delete/${id}`);
  },
};
