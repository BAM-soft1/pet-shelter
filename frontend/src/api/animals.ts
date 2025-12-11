import axiosWithAuth from "../security/axios";
import type { Animal, AnimalRequest, PageResponse } from "../types/types";
import { API_URL } from "../settings";

const API_URL_ANIMALS = `${API_URL}/animal`; // Note: backend uses /api/animal

export const AnimalService = {
  getAnimals: async (
    page = 0,
    size = 8,
    sortBy = "name",
    sortDirection = "asc",
    status?: string,
    isActive?: boolean,
    hasRequiredVaccinations?: boolean,
    sex?: string,
    minAge?: number,
    maxAge?: number,
    search?: string
  ): Promise<PageResponse<Animal>> => {
    const params: Record<string, string | number | boolean> = { page, size, sortBy, sortDirection };

    // Only add filter params if they're provided
    if (status !== undefined) params.status = status;
    if (isActive !== undefined) params.isActive = isActive;
    if (hasRequiredVaccinations !== undefined) params.hasRequiredVaccinations = hasRequiredVaccinations;
    if (sex !== undefined && sex !== "" && sex !== "all") params.sex = sex;
    if (minAge !== undefined) params.minAge = minAge;
    if (maxAge !== undefined) params.maxAge = maxAge;
    if (search !== undefined && search !== "") params.search = search;

    const response = await axiosWithAuth.get(API_URL_ANIMALS, { params });
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
