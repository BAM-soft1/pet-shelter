// Utility functions for fetch API
export function makeOptions(method: string, body?: object): RequestInit {
  const options: RequestInit = {
    method,
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include", // Include cookies for refresh token
  };

  if (body) {
    options.body = JSON.stringify(body);
  }

  return options;
}

export async function handleHttpErrors(res: Response) {
  if (!res.ok) {
    const errorResponse = await res.json().catch(() => ({ message: "Unknown error" }));
    throw new Error(errorResponse.message || `Error: ${res.status} ${res.statusText}`);
  }
  return res.json();
}

/**
 * Extract error message from axios error response
 * Backend returns { message: "error details", status: 400, ... }
 */
export function getErrorMessage(error: unknown): string {
  if (error && typeof error === "object" && "response" in error) {
    const axiosError = error as { response?: { data?: { message?: string } } };
    if (axiosError.response?.data?.message) {
      return axiosError.response.data.message;
    }
  }
  
  if (error instanceof Error) {
    return error.message;
  }
  
  return "An unexpected error occurred";
}
