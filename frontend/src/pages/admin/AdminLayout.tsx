import { useState, useEffect } from "react";
import { Link, Outlet, useLocation } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { useAuth } from "@/context/AuthProvider";
import { Bars3Icon, XMarkIcon } from "@heroicons/react/24/outline";

export default function AdminLayout() {
  const auth = useAuth();
  const location = useLocation();
  const [activeTab, setActiveTab] = useState("animals");
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

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
        <div className="container mx-auto px-4 md:px-6 py-4">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-xl md:text-3xl font-bold text-gray-800">Admin Dashboard</h1>
            </div>
            <Link to="/">
              <Button variant="outline" className="border-gray-300 text-sm md:text-base">
                Back to Site
              </Button>
            </Link>
          </div>
        </div>
      </header>

      {/* Navigation Tabs */}
      <div className="bg-white border-b border-gray-200">
        <div className="container mx-auto px-4 md:px-6">
          {/* Mobile Menu Button */}
          <div className="md:hidden py-3">
            <button
              onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
              className="flex items-center gap-2 text-gray-700 hover:text-indigo-600 transition-colors"
            >
              {isMobileMenuOpen ? <XMarkIcon className="h-6 w-6" /> : <Bars3Icon className="h-6 w-6" />}
              <span className="font-medium">Menu</span>
            </button>
          </div>

          {/* Desktop Navigation */}
          <nav className="hidden md:flex gap-1">
            {auth?.isLoggedInAs(["ADMIN", "STAFF"]) && (
              <>
                <Link to="/admin/animals">
                  <button
                    className={`px-3 md:px-6 py-3 font-medium text-sm md:text-base whitespace-nowrap transition-colors border-b-2 ${
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
                    className={`px-3 md:px-6 py-3 font-medium text-sm md:text-base whitespace-nowrap transition-colors border-b-2 ${
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
                    className={`px-3 md:px-6 py-3 font-medium text-sm md:text-base whitespace-nowrap transition-colors border-b-2 ${
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
                    className={`px-3 md:px-6 py-3 font-medium text-sm md:text-base whitespace-nowrap transition-colors border-b-2 ${
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
                    className={`px-3 md:px-6 py-3 font-medium text-sm md:text-base whitespace-nowrap transition-colors border-b-2 ${
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
                    className={`px-3 md:px-6 py-3 font-medium text-sm md:text-base whitespace-nowrap transition-colors border-b-2 ${
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

          {/* Mobile Navigation Dropdown */}
          {isMobileMenuOpen && (
            <div className="md:hidden border-t border-gray-200 py-2">
              <nav className="flex flex-col">
                {auth?.isLoggedInAs(["ADMIN", "STAFF"]) && (
                  <>
                    <Link to="/admin/animals" onClick={() => setIsMobileMenuOpen(false)}>
                      <button
                        className={`w-full text-left px-4 py-3 font-medium transition-colors ${
                          activeTab === "animals" ? "text-indigo-600 bg-indigo-50" : "text-gray-600 hover:bg-gray-50"
                        }`}
                      >
                        Animals
                      </button>
                    </Link>
                    <Link to="/admin/applications" onClick={() => setIsMobileMenuOpen(false)}>
                      <button
                        className={`w-full text-left px-4 py-3 font-medium transition-colors ${
                          activeTab === "applications" ? "text-indigo-600 bg-indigo-50" : "text-gray-600 hover:bg-gray-50"
                        }`}
                      >
                        Applications
                      </button>
                    </Link>
                    <Link to="/admin/adoptions" onClick={() => setIsMobileMenuOpen(false)}>
                      <button
                        className={`w-full text-left px-4 py-3 font-medium transition-colors ${
                          activeTab === "adoptions" ? "text-indigo-600 bg-indigo-50" : "text-gray-600 hover:bg-gray-50"
                        }`}
                      >
                        Adoptions
                      </button>
                    </Link>
                  </>
                )}
                {auth?.isLoggedInAs(["ADMIN", "VETERINARIAN"]) && (
                  <>
                    <Link to="/admin/veterinarian/overview" onClick={() => setIsMobileMenuOpen(false)}>
                      <button
                        className={`w-full text-left px-4 py-3 font-medium transition-colors ${
                          activeTab === "medical-records" ? "text-indigo-600 bg-indigo-50" : "text-gray-600 hover:bg-gray-50"
                        }`}
                      >
                        Medical Records
                      </button>
                    </Link>
                    <Link to="/admin/veterinarian/vaccinations" onClick={() => setIsMobileMenuOpen(false)}>
                      <button
                        className={`w-full text-left px-4 py-3 font-medium transition-colors ${
                          activeTab === "vaccinations" ? "text-indigo-600 bg-indigo-50" : "text-gray-600 hover:bg-gray-50"
                        }`}
                      >
                        Vaccinations
                      </button>
                    </Link>
                    <Link to="/admin/veterinarian/vaccinations-types" onClick={() => setIsMobileMenuOpen(false)}>
                      <button
                        className={`w-full text-left px-4 py-3 font-medium transition-colors ${
                          activeTab === "vaccination-types" ? "text-indigo-600 bg-indigo-50" : "text-gray-600 hover:bg-gray-50"
                        }`}
                      >
                        Vaccination Types
                      </button>
                    </Link>
                  </>
                )}
              </nav>
            </div>
          )}
        </div>
      </div>

      {/* Main Content */}
      <main className="container mx-auto px-4 md:px-6 py-6 md:py-8">
        <Outlet />
      </main>
    </div>
  );
}
