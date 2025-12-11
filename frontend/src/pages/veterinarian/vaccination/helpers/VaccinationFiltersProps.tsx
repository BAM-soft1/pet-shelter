import { XMarkIcon } from "@heroicons/react/24/outline";
import type { Animal, VaccinationType } from "@/types/types";

type VaccinationFiltersProps = {
  searchTerm: string;
  setSearchTerm: (value: string) => void;
  animalFilter: string;
  setAnimalFilter: (value: string) => void;
  vaccinationTypeFilter: string;
  setVaccinationTypeFilter: (value: string) => void;
  dateFrom: string;
  setDateFrom: (value: string) => void;
  dateTo: string;
  setDateTo: (value: string) => void;
  clearFilters: () => void;
  totalCount: number;
  filteredCount: number;
  animals: Animal[];
  vaccinationTypes: VaccinationType[];
};

export default function VaccinationFilters({
  searchTerm,
  setSearchTerm,
  animalFilter,
  setAnimalFilter,
  vaccinationTypeFilter,
  setVaccinationTypeFilter,
  dateFrom,
  setDateFrom,
  dateTo,
  setDateTo,
  clearFilters,
  totalCount,
  filteredCount,
  animals,
  vaccinationTypes,
}: VaccinationFiltersProps) {
  const hasActiveFilters = searchTerm || animalFilter || vaccinationTypeFilter || dateFrom || dateTo;

  return (
    <div className="bg-gray-50 p-4 rounded-lg mb-6 space-y-4">
      <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-6 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Search</label>
          <input
            type="text"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder="Animal name, vaccine..."
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Animal</label>
          <select
            value={animalFilter}
            onChange={(e) => setAnimalFilter(e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="">All Animals</option>
            {animals.map((animal) => (
              <option key={animal.id} value={animal.id.toString()}>
                {animal.name}
              </option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Vaccination Type</label>
          <select
            value={vaccinationTypeFilter}
            onChange={(e) => setVaccinationTypeFilter(e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="">All Types</option>
            {vaccinationTypes.map((type) => (
              <option key={type.id} value={type.id.toString()}>
                {type.vaccineName}
              </option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Date From</label>
          <input
            type="date"
            value={dateFrom}
            onChange={(e) => setDateFrom(e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Date To</label>
          <input
            type="date"
            value={dateTo}
            onChange={(e) => setDateTo(e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
          />
        </div>
        <div className="flex items-end">
          <button
            onClick={clearFilters}
            disabled={!hasActiveFilters}
            className={`flex items-center px-4 py-2 border rounded-md transition-colors ${
              hasActiveFilters
                ? "text-red-600 border-red-300 hover:bg-red-50"
                : "text-gray-400 border-gray-200 cursor-not-allowed"
            }`}
          >
            <XMarkIcon className="h-4 w-4 mr-2" />
            Clear
          </button>
        </div>
      </div>
      <div className="text-sm text-gray-500">
        Showing {filteredCount} of {totalCount} vaccinations
      </div>
    </div>
  );
}