import axiosWithAuth from "../security/axios";
import type { MedicalRecord, MedicalRecordRequest } from "../types/types";
import { API_URL } from "../settings";

const API_URL_MEDICAL_RECORDS = `${API_URL}/medical-record`;

export const MedicalRecordService = {
    getAllMedicalRecords: async (): Promise<MedicalRecord[]> => {
    const response = await axiosWithAuth.get(API_URL_MEDICAL_RECORDS);
    return response.data;
  },

  getMedicalRecordById: async (id: number): Promise<MedicalRecord> => {
    const response = await axiosWithAuth.get(`${API_URL_MEDICAL_RECORDS}/${id}`);
    return response.data;
  },

  createMedicalRecord: async (record: MedicalRecordRequest): Promise<MedicalRecord> => {
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

