import axiosWithAuth from "../security/axios";
import type { AdoptionApplication } from "../types/types";
import { API_URL } from "../settings";

const API_URL_ADOPTION_APPLICATION = `${API_URL}/adoption-application`; // Note: backend uses /api/animal

export const AdoptionApplicationService = {

    createAdoptionApplication: async (application: AdoptionApplication): Promise<AdoptionApplication> => {
    const response = await axiosWithAuth.post(`${API_URL_ADOPTION_APPLICATION}/add`, application);
    return response.data;
    }

};