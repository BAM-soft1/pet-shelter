import axiosWithAuth from "../security/axios";
import type { AdoptionApplicationRequest, AdoptionApplicationResponse, AdoptionApplication, PageResponse } from "../types/types";
import { API_URL } from "../settings";

const API_URL_ADOPTION_APPLICATION = `${API_URL}/adoption-application`; // Note: backend uses /api/animal

export const AdoptionApplicationService = {
  createAdoptionApplication: async (application: AdoptionApplicationRequest): Promise<AdoptionApplicationRequest> => {
    const response = await axiosWithAuth.post(`${API_URL_ADOPTION_APPLICATION}/add`, application);
    return response.data;
  },

  getAdoptionApplicationForUser: async (userId: number): Promise<AdoptionApplicationResponse[]> => {
    const response = await axiosWithAuth.get(`${API_URL_ADOPTION_APPLICATION}/user/${userId}`);
    return response.data;
  },

  getHasUserAppliedForAnimal: async (userId: number, animalId: number): Promise<boolean> => {
    const response = await axiosWithAuth.get(`${API_URL_ADOPTION_APPLICATION}/has-applied/${userId}/${animalId}`);
    return response.data;
  },

  getAllApplications: async (
    page = 0, 
    size = 8, 
    sortBy = "applicationDate", 
    sortDirection = "desc",
    status?: string,
    search?: string
  ): Promise<PageResponse<AdoptionApplication>> => {
    const response = await axiosWithAuth.get(`${API_URL_ADOPTION_APPLICATION}/all`, {
      params: { 
        page, 
        size, 
        sortBy, 
        sortDirection,
        ...(status && status !== "all" && { status }),
        ...(search && { search })
      },
    });
    return response.data;
  },

  approveApplication: async (applicationId: number, adoptionDate: string, reviewedByUserId: number): Promise<void> => {
    await axiosWithAuth.post(`${API_URL}/test-procedures/complete-adoption`, null, {
      params: {
        applicationId,
        adoptionDate,
        reviewedByUserId,
      },
    });
  },

  rejectApplication: async (applicationId: number, reviewedByUserId: number): Promise<AdoptionApplication> => {
    const response = await axiosWithAuth.patch(`${API_URL_ADOPTION_APPLICATION}/reject/${applicationId}`, null, {
      params: {
        reviewedByUserId,
      },
    });
    return response.data;
  },
};
