import { Link } from "react-router-dom";
import MainLayout from "../../components/layout/MainLayout";

export default function Landing() {
  return (
    <MainLayout>
      <div className="max-w-4xl mx-auto">
        <div className="text-center mb-12">
          <h1 className="text-4xl md:text-5xl font-bold text-gray-800 mb-4">Every Pet Deserves a Loving Home</h1>
          <p className="text-lg md:text-xl text-gray-600 mb-8">Meet the wonderful animals waiting to become part of your family</p>

          <Link
            to="/animals"
            className="inline-block bg-indigo-600 text-white px-8 py-3 rounded-lg font-semibold hover:bg-indigo-700 transition-colors"
          >
            Meet Our Animals
          </Link>
        </div>

        <div className="bg-white rounded-lg shadow-sm p-8">
          <h2 className="text-2xl font-bold text-gray-800 mb-6 text-center">Why Adopt?</h2>
          <div className="grid md:grid-cols-3 gap-6">
            <div>
              <h3 className="text-lg font-semibold text-gray-800 mb-2">Save a Life</h3>
              <p className="text-gray-600">
                When you adopt, you're giving an animal a second chance at happiness and opening up space for another rescue.
              </p>
            </div>
            <div>
              <h3 className="text-lg font-semibold text-gray-800 mb-2">Find Your Match</h3>
              <p className="text-gray-600">Our team helps you find the perfect companion that fits your lifestyle and family.</p>
            </div>
            <div>
              <h3 className="text-lg font-semibold text-gray-800 mb-2">Health & Care</h3>
              <p className="text-gray-600">All our animals are vaccinated, health-checked, and ready for their new home.</p>
            </div>
          </div>
        </div>
      </div>
    </MainLayout>
  );
}
