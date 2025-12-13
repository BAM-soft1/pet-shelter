import axiosWithAuth from "../security/axios";
import type { MedicalRecord, MedicalRecordRequest, PageResponse } from "../types/types";
import { API_URL } from "../settings";

const API_URL_MEDICAL_RECORDS = `${API_URL}/medical-record`;

export const MedicalRecordService = {
  getMedicalRecords: async (
    page: number = 0,
    size: number = 10,
    sortBy: string = "date",
    sortDirection: string = "desc",
    animalStatus?: string,
    startDate?: string,
    endDate?: string,
    search?: string
  ): Promise<PageResponse<MedicalRecord>> => {
    const response = await axiosWithAuth.get(API_URL_MEDICAL_RECORDS, {
      params: {
        page,
        size,
        sortBy,
        sortDirection,
        animalStatus: animalStatus || undefined,
        startDate: startDate || undefined,
        endDate: endDate || undefined,
        search: search || undefined,
      },
    });
    return response.data;
  },

  getAllMedicalRecords: async (): Promise<MedicalRecord[]> => {
    const response = await axiosWithAuth.get(API_URL_MEDICAL_RECORDS);
    return response.data;
  },

  getMedicalRecordById: async (id: number): Promise<MedicalRecord> => {
    const response = await axiosWithAuth.get(`${API_URL_MEDICAL_RECORDS}/${id}`);
    return response.data;
  },

  createMedicalRecord: async (record: Omit<MedicalRecordRequest, 'userId'>): Promise<MedicalRecord> => {
    const response = await axiosWithAuth.post(`${API_URL_MEDICAL_RECORDS}/add`, record);
    return response.data;
  },

  updateMedicalRecord: async (id: number, record: Partial<MedicalRecord>): Promise<MedicalRecord> => {
    const response = await axiosWithAuth.put(`${API_URL_MEDICAL_RECORDS}/update/${id}`, record);
    return response.data;
  },

  deleteMedicalRecord: async (id: number): Promise<void> => {
    await axiosWithAuth.delete(`${API_URL_MEDICAL_RECORDS}/delete/${id}`);
  },
};

