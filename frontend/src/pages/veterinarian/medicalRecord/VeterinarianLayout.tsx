import { useState, useEffect } from "react";
import { Link, Outlet, useLocation } from "react-router-dom";
import { Button } from "@/components/ui/button";

export default function MedicalRecordLayout() {
  const location = useLocation();
  const [activeTab, setActiveTab] = useState("overview");

  useEffect(() => {
    if (location.pathname.includes("overview")) setActiveTab("overview");
    if (location.pathname.includes("vaccinations")) setActiveTab("vaccinations");
  }, [location]);

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white shadow-sm border-b border-gray-200">
        <div className="container mx-auto px-6 py-4">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold text-gray-800">Veterinarian Dashboard</h1>
            </div>
            <Link to="/">
              <Button variant="outline" className="border-gray-300">
                Back to Site
              </Button>
            </Link>
          </div>
        </div>
      </header>

      {/* Navigation Tabs */}
      <div className="bg-white border-b border-gray-200">
        <div className="container mx-auto px-6">
          <nav className="flex gap-1">
            <Link to="/veterinarian/overview">
              <button
                className={`px-6 py-3 font-medium transition-colors border-b-2 ${
                  activeTab === "overview"
                    ? "text-indigo-600 border-indigo-600"
                    : "text-gray-600 border-transparent hover:text-indigo-600 hover:border-gray-300"
                }`}
              >
                Medical Record Overview
              </button>
            </Link>
            <Link to="/veterinarian/vaccinations">
              <button
                className={`px-6 py-3 font-medium transition-colors border-b-2 ${
                  activeTab === "vaccinations"
                    ? "text-indigo-600 border-indigo-600"
                    : "text-gray-600 border-transparent hover:text-indigo-600 hover:border-gray-300"
                }`}
              >
                Vaccination Overview
              </button>
            </Link>
          </nav>
        </div>
      </div>

      {/* Main content */}
      <main className="container mx-auto px-6 py-8">
        <Outlet />
      </main>
    </div>
  );
}