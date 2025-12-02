import MainLayout from "../../components/layout/MainLayout";

export default function About() {
  return (
    <MainLayout>
      <div className="max-w-4xl mx-auto">
        <div className="text-center mb-12">
          <h1 className="text-5xl font-bold text-gray-800 mb-4">About Us</h1>
          <p className="text-xl text-gray-600">Dedicated to finding loving homes for animals in need</p>
        </div>

        <div className="grid md:grid-cols-3 gap-8 mb-16">
          <div className="bg-white p-6 rounded-lg shadow-sm">
            <h3 className="text-xl font-semibold text-gray-800 mb-3">Rescue</h3>
            <p className="text-gray-600">We rescue abandoned and neglected animals, giving them a second chance at life.</p>
          </div>
          <div className="bg-white p-6 rounded-lg shadow-sm">
            <h3 className="text-xl font-semibold text-gray-800 mb-3">Rehabilitate</h3>
            <p className="text-gray-600">We provide medical care, vaccinations, and a safe environment for recovery.</p>
          </div>
          <div className="bg-white p-6 rounded-lg shadow-sm">
            <h3 className="text-xl font-semibold text-gray-800 mb-3">Rehome</h3>
            <p className="text-gray-600">We match animals with loving families to create lasting bonds.</p>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-sm p-8 mb-12">
          <h2 className="text-3xl font-bold text-gray-800 mb-6">Our Mission</h2>
          <p className="text-lg text-gray-700 leading-relaxed">
            Our mission is to rescue, rehabilitate, and rehome abandoned and neglected animals. We provide comprehensive medical care, vaccinations,
            and a safe, nurturing environment for all our animals while they await their forever homes.
          </p>
        </div>

        <div className="bg-white rounded-lg shadow-sm p-8 mb-12">
          <h2 className="text-3xl font-bold text-gray-800 mb-6">Adoption Process</h2>
          <div className="space-y-4">
            <div className="flex gap-4">
              <div className="shrink-0 w-8 h-8 bg-indigo-600 text-white rounded-full flex items-center justify-center font-semibold">1</div>
              <div>
                <h3 className="font-semibold text-gray-800 mb-1">Browse Available Animals</h3>
                <p className="text-gray-600">Explore our collection of animals looking for homes.</p>
              </div>
            </div>
            <div className="flex gap-4">
              <div className="shrink-0 w-8 h-8 bg-indigo-600 text-white rounded-full flex items-center justify-center font-semibold">2</div>
              <div>
                <h3 className="font-semibold text-gray-800 mb-1">Submit an Application</h3>
                <p className="text-gray-600">Fill out our adoption application form.</p>
              </div>
            </div>
            <div className="flex gap-4">
              <div className="shrink-0 w-8 h-8 bg-indigo-600 text-white rounded-full flex items-center justify-center font-semibold">3</div>
              <div>
                <h3 className="font-semibold text-gray-800 mb-1">Meet Your Match</h3>
                <p className="text-gray-600">Visit and interact with your potential new companion.</p>
              </div>
            </div>
            <div className="flex gap-4">
              <div className="shrink-0 w-8 h-8 bg-indigo-600 text-white rounded-full flex items-center justify-center font-semibold">4</div>
              <div>
                <h3 className="font-semibold text-gray-800 mb-1">Welcome Home</h3>
                <p className="text-gray-600">Complete the adoption and take your new friend home.</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </MainLayout>
  );
}
