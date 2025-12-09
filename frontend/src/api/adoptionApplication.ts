import axiosWithAuth from "../security/axios";
import type { AdoptionApplicationRequest } from "../types/types";
import { API_URL } from "../settings";

const API_URL_ADOPTION_APPLICATION = `${API_URL}/adoption-application`; // Note: backend uses /api/animal

export const AdoptionApplicationService = {

    createAdoptionApplication: async (application: AdoptionApplicationRequest): Promise<AdoptionApplicationRequest> => {
    const response = await axiosWithAuth.post(`${API_URL_ADOPTION_APPLICATION}/add`, application);
    return response.data;
    }

};