import { Link } from "react-router-dom";

export const Footer = () => {
  return (
    <footer className="bg-white border-t border-gray-200 mt-auto">
      <div className="container mx-auto px-6 py-6">
        <div className="grid md:grid-cols-3 gap-6">
          <div>
            <h3 className="text-sm font-bold text-gray-800 mb-2">Pet Shelter</h3>
            <p className="text-sm text-gray-600">Yap yap yap yap</p>
          </div>

          <div>
            <h3 className="text-sm font-bold text-gray-800 mb-2">Quick Links</h3>
            <ul className="space-y-1">
              <li>
                <Link to="/" className="text-sm text-gray-600 hover:text-indigo-600 transition-colors">
                  Home
                </Link>
              </li>
              <li>
                <Link to="/animals" className="text-sm text-gray-600 hover:text-indigo-600 transition-colors">
                  Animals
                </Link>
              </li>
              <li>
                <Link to="/about" className="text-sm text-gray-600 hover:text-indigo-600 transition-colors">
                  About Us
                </Link>
              </li>
            </ul>
          </div>

          <div>
            <h3 className="text-sm font-bold text-gray-800 mb-2">Contact</h3>
            <ul className="space-y-1 text-sm text-gray-600">
              <li>info@petshelter.com</li>
              <li>123 Shelter Street</li>
              <li>City, State 12345</li>
            </ul>
          </div>
        </div>

        <div className="border-t border-gray-200 mt-6 pt-4 text-center text-sm text-gray-600">
          <p>&copy; {new Date().getFullYear()} BAM Pet Shelter.</p>
        </div>
      </div>
    </footer>
  );
};
