import axiosWithAuth from "../security/axios";
import type { Vaccination, VaccinationType, VaccinationRequest, VaccinationTypeRequest } from "../types/types";
import { API_URL } from "../settings";

const API_URL_VACCINATIONS = `${API_URL}/vaccination`;
const API_URL_VACCINATION_TYPES = `${API_URL}/vaccination-type`;


export const VaccinationService = {
  getAllVaccinations: async (): Promise<Vaccination[]> => {
    const response = await axiosWithAuth.get(API_URL_VACCINATIONS);
    return response.data;
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

    getAllVaccinationTypes: async (): Promise<VaccinationType[]> => {
    const response = await axiosWithAuth.get(API_URL_VACCINATION_TYPES);
    return response.data;
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