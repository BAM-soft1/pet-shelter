import { Navigate } from "react-router-dom";
import { useAuth } from "@/context/AuthProvider";

interface RoleBasedRedirectProps {
  veterinarianPath: string;
  defaultPath: string;
}

export default function RoleBasedRedirect({ veterinarianPath, defaultPath }: RoleBasedRedirectProps) {
  const auth = useAuth();

  // If user is ONLY a veterinarian (not also admin/staff), redirect to veterinarian path
  if (auth?.isLoggedInAs(["VETERINARIAN"]) && !auth?.isLoggedInAs(["ADMIN", "STAFF"])) {
    return <Navigate to={veterinarianPath} replace />;
  }

  // Otherwise redirect to default path
  return <Navigate to={defaultPath} replace />;
}
