import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import MainLayout from "@/components/layout/MainLayout";

export default function NotFound() {
  return (
    <MainLayout>
      <div className="flex flex-col items-center justify-center min-h-[60vh] px-4">
        <div className="text-center space-y-6">
          <h1 className="text-9xl font-bold text-primary">404</h1>
          <h2 className="text-3xl font-semibold text-gray-800">Page Not Found</h2>
          <p className="text-gray-600 max-w-md">
            Oops! The page you're looking for doesn't exist. It might have been moved or deleted.
          </p>
          <div className="flex gap-4 justify-center pt-4">
            <Button asChild>
              <Link to="/">Go Home</Link>
            </Button>
            <Button asChild variant="outline">
              <Link to="/animals">Browse Animals</Link>
            </Button>
          </div>
        </div>
      </div>
    </MainLayout>
  );
}
