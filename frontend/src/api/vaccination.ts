import axiosWithAuth from "../security/axios";
import type { Vaccination, VaccinationType, VaccinationRequest, VaccinationTypeRequest, PageResponse } from "../types/types";
import { API_URL } from "../settings";

const API_URL_VACCINATIONS = `${API_URL}/vaccination`;
const API_URL_VACCINATION_TYPES = `${API_URL}/vaccination-type`;

export const VaccinationService = {
  getVaccinations: async (
    page: number = 0,
    size: number = 10,
    sortBy: string = "dateAdministered",
    sortDirection: string = "desc",
    animalStatus?: string,
    search?: string
  ): Promise<PageResponse<Vaccination>> => {
    const response = await axiosWithAuth.get(API_URL_VACCINATIONS, {
      params: {
        page,
        size,
        sortBy,
        sortDirection,
        animalStatus: animalStatus || undefined,
        search: search || undefined,
      },
    });
    return response.data;
  },

  getAllVaccinations: async (): Promise<Vaccination[]> => {
    const response = await axiosWithAuth.get(API_URL_VACCINATIONS, {
      params: { page: 0, size: 1000 },
    });
    return response.data.content;
  },

  getVaccinationById: async (id: number): Promise<Vaccination> => {
    const response = await axiosWithAuth.get(`${API_URL_VACCINATIONS}/${id}`);
    return response.data;
  },

  createVaccination: async (vaccination: VaccinationRequest): Promise<Vaccination> => {
    const response = await axiosWithAuth.post(`${API_URL_VACCINATIONS}/add`, vaccination);
    return response.data;
  },

  updateVaccination: async (id: number, vaccination: Partial<Vaccination>): Promise<Vaccination> => {
    const response = await axiosWithAuth.put(`${API_URL_VACCINATIONS}/update/${id}`, vaccination);
    return response.data;
  },

  deleteVaccination: async (id: number): Promise<void> => {
    await axiosWithAuth.delete(`${API_URL_VACCINATIONS}/delete/${id}`);
  },

  // Vaccination Type

  getVaccinationTypes: async (
    page: number = 0,
    size: number = 10,
    sortBy: string = "vaccineName",
    sortDirection: string = "asc",
    requiredForAdoption?: boolean,
    search?: string
  ): Promise<PageResponse<VaccinationType>> => {
    const response = await axiosWithAuth.get(API_URL_VACCINATION_TYPES, {
      params: {
        page,
        size,
        sortBy,
        sortDirection,
        requiredForAdoption: requiredForAdoption !== undefined ? requiredForAdoption : undefined,
        search: search || undefined,
      },
    });
    return response.data;
  },

  getAllVaccinationTypes: async (): Promise<VaccinationType[]> => {
    const response = await axiosWithAuth.get(API_URL_VACCINATION_TYPES, {
      params: { page: 0, size: 1000 },
    });
    return response.data.content;
  },

  createVaccinationType: async (vaccinationType: VaccinationTypeRequest): Promise<VaccinationType> => {
    const response = await axiosWithAuth.post(`${API_URL_VACCINATION_TYPES}/add`, vaccinationType);
    return response.data;
  },

  updateVaccinationType: async (id: number, vaccinationType: Partial<VaccinationType>): Promise<VaccinationType> => {
    const response = await axiosWithAuth.put(`${API_URL_VACCINATION_TYPES}/update/${id}`, vaccinationType);
    return response.data;
  },

  deleteVaccinationType: async (id: number): Promise<void> => {
    await axiosWithAuth.delete(`${API_URL_VACCINATION_TYPES}/delete/${id}`);
  },
};
