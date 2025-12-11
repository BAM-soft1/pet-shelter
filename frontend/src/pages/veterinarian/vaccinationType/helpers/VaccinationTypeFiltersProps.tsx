import { XMarkIcon } from "@heroicons/react/24/outline";

type VaccinationTypeFiltersProps = {
  searchTerm: string;
  setSearchTerm: (value: string) => void;
  requiredFilter: string;
  setRequiredFilter: (value: string) => void;
  clearFilters: () => void;
  totalCount: number;
  filteredCount: number;
};

export default function VaccinationTypeFilters({
  searchTerm,
  setSearchTerm,
  requiredFilter,
  setRequiredFilter,
  clearFilters,
  totalCount,
  filteredCount,
}: VaccinationTypeFiltersProps) {
  const hasActiveFilters = searchTerm || requiredFilter;

  return (
    <div className="bg-gray-50 p-4 rounded-lg mb-6 space-y-4">
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Search</label>
          <input
            type="text"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder="Vaccine name, description..."
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Required for Adoption</label>
          <select
            value={requiredFilter}
            onChange={(e) => setRequiredFilter(e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="">All</option>
            <option value="yes">Yes</option>
            <option value="no">No</option>
          </select>
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
            Clear Filters
          </button>
        </div>
      </div>
      <div className="text-sm text-gray-500">
        Showing {filteredCount} of {totalCount} vaccination types
      </div>
    </div>
  );
}