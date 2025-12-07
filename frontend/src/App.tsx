import { BrowserRouter, Routes, Route } from "react-router-dom";
import Landing from "./pages/landing/Landing";
import Animals from "./pages/animals/AnimalOverview";
import About from "./pages/about/About";
import AdminLayout from "./pages/admin/AdminLayout";
import AdminAnimals from "./pages/admin/AdminAnimals";
import AdminApplications from "./pages/admin/AdminApplications";
import AdminAdoptions from "./pages/admin/AdminAdoptions";
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
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          

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
          </Route>
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
