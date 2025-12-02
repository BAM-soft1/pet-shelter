import { useAuth } from "../context/AuthProvider";
import { NavLink, Link } from "react-router-dom";

interface AuthStatusProps {
  closeMenu?: () => void;
}

export default function AuthStatus({ closeMenu }: AuthStatusProps) {
  const auth = useAuth();

  if (!auth?.isLoggedIn()) {
    return (
      <li>
        <NavLink
          to="/login"
          onClick={closeMenu}
          className={({ isActive }) =>
            `text-lg font-medium transition-colors ${isActive ? "text-indigo-600" : "text-gray-700 hover:text-indigo-600"}`
          }
        >
          Log in
        </NavLink>
      </li>
    );
  } else {
    return (
      <li>
        <Link
          to="/"
          onClick={() => {
            auth.signOut();
            closeMenu?.();
          }}
          className="text-lg font-medium text-gray-700 hover:text-indigo-600 transition-colors"
        >
          Log out ({auth.username})
        </Link>
      </li>
    );
  }
}
