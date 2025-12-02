import { useState } from "react";
import { NavLink } from "react-router-dom";
import { XMarkIcon, Bars3Icon } from "@heroicons/react/24/outline";
import { useAuth } from "../../context/AuthProvider";
import AuthStatus from "../../security/AuthStatus";

export const Header = () => {
  const auth = useAuth();
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const closeMenu = () => setIsMenuOpen(false);
  return (
    <header className="relative bg-white shadow-sm border-b border-gray-200">
      <div className="container flex items-center justify-between px-6 py-4 mx-auto">
        <h1 className={`${isMenuOpen ? "hidden" : "block"} md:block`}>
          <NavLink to="/" className="text-3xl font-bold text-gray-800 hover:text-gray-600 transition-colors" onClick={closeMenu}>
            Pet Shelter
          </NavLink>
        </h1>
        <nav className={`${isMenuOpen ? "block" : "hidden"} md:block`}>
          <ul className="flex flex-col space-y-4 md:flex-row md:space-y-0 md:space-x-8">
            <li>
              <NavLink
                to="/animals"
                className={({ isActive }) =>
                  `text-lg font-medium transition-colors ${isActive ? "text-indigo-600" : "text-gray-700 hover:text-indigo-600"}`
                }
                onClick={closeMenu}
              >
                Animals
              </NavLink>
            </li>
            <li>
              <NavLink
                to="/about"
                className={({ isActive }) =>
                  `text-lg font-medium transition-colors ${isActive ? "text-indigo-600" : "text-gray-700 hover:text-indigo-600"}`
                }
                onClick={closeMenu}
              >
                About us
              </NavLink>
            </li>
            {auth?.isLoggedInAs(["ADMIN", "STAFF"]) && (
              <li>
                <NavLink
                  to="/admin"
                  className={({ isActive }) =>
                    `text-lg font-medium transition-colors ${isActive ? "text-indigo-600" : "text-gray-700 hover:text-indigo-600"}`
                  }
                  onClick={closeMenu}
                >
                  Admin
                </NavLink>
              </li>
            )}
            <AuthStatus closeMenu={closeMenu} />
          </ul>
        </nav>
        <button
          className="absolute md:hidden top-5 right-6 text-gray-700 hover:text-indigo-600 transition-colors"
          onClick={() => setIsMenuOpen(!isMenuOpen)}
          aria-label={isMenuOpen ? "Close menu" : "Open menu"}
        >
          {isMenuOpen ? <XMarkIcon className="w-6 h-6" /> : <Bars3Icon className="w-6 h-6" />}
        </button>
      </div>
    </header>
  );
};
