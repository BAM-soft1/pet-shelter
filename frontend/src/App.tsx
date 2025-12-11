import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Landing from "./pages/landing/Landing";
import Animals from "./pages/animals/AnimalOverview";
import About from "./pages/about/About";
import AdminLayout from "./pages/admin/AdminLayout";
import AdminAnimals from "./pages/admin/AdminAnimals";
import AdminApplications from "./pages/admin/AdminApplications";
import AdminAdoptions from "./pages/admin/AdminAdoptions";
import DogFacts from "./pages/dogfacts/DogFacts";
import MedicalRecordOverview from "./pages/veterinarian/medicalRecord/MedicalRecordOverview"
import VaccinationOverview from "./pages/veterinarian/vaccination/Vaccination";
import VaccinationTypeOverview from "./pages/veterinarian/vaccinationType/VaccinationType";
import VeterinarianLayout from "./pages/veterinarian/layout/VeterinarianLayout";
import Login from "./pages/auth/Login";
import Register from "./pages/auth/Register";
import AuthProvider from "./context/AuthProvider";
import RequireAuth from "./security/RequireAuth";

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path="/" element={<Landing />} />
          <Route path="/animals" element={<Animals />} />
          <Route path="/about" element={<About />} />
          <Route path="/dog-facts" element={<DogFacts />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />


          <Route
            path="/veterinarian"
            element={
              <RequireAuth roles={["VETERINARIAN", "ADMIN", "STAFF"]}>
                <VeterinarianLayout />
              </RequireAuth>
            }
          >
            <Route index element={<Navigate to="overview" replace />} />
            <Route path="overview" element={<MedicalRecordOverview />} />
            <Route path="vaccinations" element={<VaccinationOverview />} />
            <Route path="vaccinations-types" element={<VaccinationTypeOverview />} />
          </Route>


          {/* Protected Admin Routes */}
          <Route
            path="/admin"
            element={
              <RequireAuth roles={["ADMIN", "STAFF"]}>
                <AdminLayout />
              </RequireAuth>
            }
          >
            <Route index element={<AdminAnimals />} />
            <Route path="animals" element={<AdminAnimals />} />
            <Route path="applications" element={<AdminApplications />} />
            <Route path="adoptions" element={<AdminAdoptions />} />
            <Route path="veterinarian/overview" element={<MedicalRecordOverview />} />
            <Route path="veterinarian/vaccinations" element={<VaccinationOverview />} />
            <Route path="veterinarian/vaccination-types" element={<VaccinationTypeOverview />} />
          </Route>
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;