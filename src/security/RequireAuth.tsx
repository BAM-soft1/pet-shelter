import { useAuth } from "../context/AuthProvider";
import { Navigate, useLocation } from "react-router-dom";
import type { ReactElement } from "react";

type Props = {
  children: ReactElement;
  roles?: string[];
};

export default function RequireAuth({ children, roles }: Props) {
  const auth = useAuth();
  const location = useLocation();

  if (roles) {
    if (!auth?.isLoggedInAs(roles)) {
      return <Navigate to="/login" state={{ from: location }} replace />;
    }
  }

  if (!auth?.isLoggedIn()) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return children;
}
