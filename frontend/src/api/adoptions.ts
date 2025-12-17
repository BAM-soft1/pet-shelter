import axiosWithAuth from "../security/axios";
import type { Adoption, PageResponse } from "../types/types";
import { API_URL } from "../settings";

const API_URL_ADOPTIONS = `${API_URL}/adoption`;

export const AdoptionService = {
  getAdoptions: async (
    page = 0,
    size = 10,
    sortBy = "adoptionDate",
    sortDirection = "desc"
  ): Promise<PageResponse<Adoption>> => {
    const params = { page, size, sortBy, sortDirection };
    const response = await axiosWithAuth.get(API_URL_ADOPTIONS, { params });
    return response.data;
  },
};
