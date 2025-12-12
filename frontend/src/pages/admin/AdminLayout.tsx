import { useState, useEffect } from "react";
import { Link, Outlet, useLocation } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { useAuth } from "@/context/AuthProvider";

export default function AdminLayout() {
  const auth = useAuth();
  const location = useLocation();
  const [activeTab, setActiveTab] = useState("animals");

  useEffect(() => {
    if (location.pathname.includes("animals") && !location.pathname.includes("veterinarian")) setActiveTab("animals");
    else if (location.pathname.includes("applications")) setActiveTab("applications");
    else if (location.pathname.includes("adoptions")) setActiveTab("adoptions");
    else if (location.pathname.includes("veterinarian/overview")) setActiveTab("medical-records");
    else if (location.pathname.includes("veterinarian/vaccinations-types")) setActiveTab("vaccination-types");
    else if (location.pathname.includes("veterinarian/vaccinations")) setActiveTab("vaccinations");
    else if (location.pathname === "/admin" || location.pathname === "/admin/") {
      // If veterinarian lands on /admin, set to medical-records, otherwise animals
      if (auth?.isLoggedInAs(["VETERINARIAN"]) && !auth?.isLoggedInAs(["ADMIN", "STAFF"])) {
        setActiveTab("medical-records");
      } else {
        setActiveTab("animals");
      }
    }
  }, [location, auth]);

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Admin Header */}
      <header className="bg-white shadow-sm border-b border-gray-200">
        <div className="container mx-auto px-6 py-4">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold text-gray-800">Admin Dashboard</h1>
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
            {auth?.isLoggedInAs(["ADMIN", "STAFF"]) && (
              <>
                <Link to="/admin/animals">
                  <button
                    className={`px-6 py-3 font-medium transition-colors border-b-2 ${
                      activeTab === "animals"
                        ? "text-indigo-600 border-indigo-600"
                        : "text-gray-600 border-transparent hover:text-indigo-600 hover:border-gray-300"
                    }`}
                  >
                    Animals
                  </button>
                </Link>
                <Link to="/admin/applications">
                  <button
                    className={`px-6 py-3 font-medium transition-colors border-b-2 ${
                      activeTab === "applications"
                        ? "text-indigo-600 border-indigo-600"
                        : "text-gray-600 border-transparent hover:text-indigo-600 hover:border-gray-300"
                    }`}
                  >
                    Applications
                  </button>
                </Link>
                <Link to="/admin/adoptions">
                  <button
                    className={`px-6 py-3 font-medium transition-colors border-b-2 ${
                      activeTab === "adoptions"
                        ? "text-indigo-600 border-indigo-600"
                        : "text-gray-600 border-transparent hover:text-indigo-600 hover:border-gray-300"
                    }`}
                  >
                    Adoptions
                  </button>
                </Link>
              </>
            )}

            {auth?.isLoggedInAs(["ADMIN", "VETERINARIAN"]) && (
              <>
                <Link to="/admin/veterinarian/overview">
                  <button
                    className={`px-6 py-3 font-medium transition-colors border-b-2 ${
                      activeTab === "medical-records"
                        ? "text-indigo-600 border-indigo-600"
                        : "text-gray-600 border-transparent hover:text-indigo-600 hover:border-gray-300"
                    }`}
                  >
                    Medical Records
                  </button>
                </Link>

                <Link to="/admin/veterinarian/vaccinations">
                  <button
                    className={`px-6 py-3 font-medium transition-colors border-b-2 ${
                      activeTab === "vaccinations"
                        ? "text-indigo-600 border-indigo-600"
                        : "text-gray-600 border-transparent hover:text-indigo-600 hover:border-gray-300"
                    }`}
                  >
                    Vaccinations
                  </button>
                </Link>

                <Link to="/admin/veterinarian/vaccinations-types">
                  <button
                    className={`px-6 py-3 font-medium transition-colors border-b-2 ${
                      activeTab === "vaccination-types"
                        ? "text-indigo-600 border-indigo-600"
                        : "text-gray-600 border-transparent hover:text-indigo-600 hover:border-gray-300"
                    }`}
                  >
                    Vaccination Types
                  </button>
                </Link>
              </>
            )}
          </nav>
        </div>
      </div>

      {/* Main Content */}
      <main className="container mx-auto px-6 py-8">
        <Outlet />
      </main>
    </div>
  );
}
