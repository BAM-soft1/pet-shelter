import { BrowserRouter, Routes, Route } from "react-router-dom";
import Landing from "./pages/landing/Landing";
import Animals from "./pages/animals/AnimalOverview";
import AnimalDetailPage from "./pages/animals/AnimalDetailPage";
import About from "./pages/about/About";
import AdminLayout from "./pages/admin/AdminLayout";
import AdminAnimals from "./pages/admin/AdminAnimals";
import AdminApplications from "./pages/admin/AdminApplications";
import AdminAdoptions from "./pages/admin/AdminAdoptions";
import DogFacts from "./pages/dogfacts/DogFacts";
import MedicalRecordOverview from "./pages/veterinarian/medicalRecord/MedicalRecordOverview";
import VaccinationOverview from "./pages/veterinarian/vaccination/Vaccination";
import VaccinationTypeOverview from "./pages/veterinarian/vaccinationType/VaccinationType";
import MyAdoptApplications from "./pages/adoptApplication/MyAdoptApplications";
import AdminReviewApplication from "./pages/admin/AdminReviewApplication";
import RoleBasedRedirect from "./components/RoleBasedRedirect";

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
          <Route path="/animal-detailed" element={<AnimalDetailPage />} />

          <Route
            path="/my-adopt-applications"
            element={
              <RequireAuth roles={["USER"]}>
                <MyAdoptApplications />
              </RequireAuth>
            }
          />

          {/* Protected Admin Routes */}
          <Route
            path="/admin"
            element={
              <RequireAuth roles={["ADMIN", "STAFF", "VETERINARIAN"]}>
                <AdminLayout />
              </RequireAuth>
            }
          >
            <Route index element={<RoleBasedRedirect veterinarianPath="/admin/veterinarian/overview" defaultPath="/admin/animals" />} />
            <Route
              path="animals"
              element={
                <RequireAuth roles={["ADMIN", "STAFF"]}>
                  <AdminAnimals />
                </RequireAuth>
              }
            />
            <Route
              path="applications"
              element={
                <RequireAuth roles={["ADMIN", "STAFF"]}>
                  <AdminApplications />
                </RequireAuth>
              }
            />
            <Route
              path="applications/:id"
              element={
                <RequireAuth roles={["ADMIN", "STAFF"]}>
                  <AdminReviewApplication />
                </RequireAuth>
              }
            />
            <Route
              path="adoptions"
              element={
                <RequireAuth roles={["ADMIN", "STAFF"]}>
                  <AdminAdoptions />
                </RequireAuth>
              }
            />
            {/* Veterinarian routes - only ADMIN and VETERINARIAN can access */}
            <Route
              path="veterinarian/overview"
              element={
                <RequireAuth roles={["ADMIN", "VETERINARIAN"]}>
                  <MedicalRecordOverview />
                </RequireAuth>
              }
            />
            <Route
              path="veterinarian/vaccinations"
              element={
                <RequireAuth roles={["ADMIN", "VETERINARIAN"]}>
                  <VaccinationOverview />
                </RequireAuth>
              }
            />
            <Route
              path="veterinarian/vaccinations-types"
              element={
                <RequireAuth roles={["ADMIN", "VETERINARIAN"]}>
                  <VaccinationTypeOverview />
                </RequireAuth>
              }
            />
          </Route>
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
