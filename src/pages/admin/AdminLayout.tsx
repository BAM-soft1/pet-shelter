import { useState, useEffect } from "react";
import { Link, Outlet, useLocation } from "react-router-dom";
import { Button } from "@/components/ui/button";

export default function AdminLayout() {
  const location = useLocation();
  const [activeTab, setActiveTab] = useState("animals");

  useEffect(() => {
    if (location.pathname.includes("animals")) setActiveTab("animals");
    else if (location.pathname.includes("applications")) setActiveTab("applications");
    else if (location.pathname.includes("adoptions")) setActiveTab("adoptions");
  }, [location]);

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
