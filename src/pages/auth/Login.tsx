import { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthProvider";
import { LoginForm } from "@/components/login-form";
import MainLayout from "../../components/layout/MainLayout";
import { getErrorMessage } from "../../services/fetchUtils";

export default function Login() {
  const navigate = useNavigate();
  const location = useLocation();
  const auth = useAuth();

  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const from = (location.state as { from?: Location })?.from?.pathname || "/";

  // Redirect if already logged in
  useEffect(() => {
    if (auth?.isLoggedIn()) {
      navigate("/", { replace: true });
    }
  }, [auth, navigate]);

  const handleSubmit = async (email: string, password: string) => {
    setError("");
    setIsLoading(true);

    try {
      await auth?.signIn({ email, password });
      navigate(from, { replace: true });
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <MainLayout>
      <div className="flex min-h-[calc(100vh-200px)] w-full items-center justify-center p-6 md:p-10">
        <div className="w-full max-w-md">
          <LoginForm onSubmit={handleSubmit} error={error} isLoading={isLoading} />
        </div>
      </div>
    </MainLayout>
  );
}
