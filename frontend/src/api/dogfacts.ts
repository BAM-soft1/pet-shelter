import axiosWithAuth from "../security/axios";
import { API_URL } from "../settings";
import type { DogFact } from "../types/types";

const API_URL_DOG_FACTS = `${API_URL}/dog-facts`;

export const DogFactsService = {
  getDogFacts: async (limit: number = 2): Promise<DogFact[]> => {
    const response = await axiosWithAuth.get(`${API_URL_DOG_FACTS}?limit=${limit}`);
    return response.data;
  },
};
