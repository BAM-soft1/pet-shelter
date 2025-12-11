import { ChevronUpIcon, ChevronDownIcon } from "@heroicons/react/24/outline";

type SortField = "vaccineName" | "durationMonths" | "requiredForAdoption";
type SortDirection = "asc" | "desc";

type VaccinationTypeSortButtonsProps = {
  sortField: SortField;
  sortDirection: SortDirection;
  onSort: (field: SortField) => void;
};

export default function VaccinationTypeSortButtons({
  sortField,
  sortDirection,
  onSort,
}: VaccinationTypeSortButtonsProps) {
  const SortIcon = ({ field }: { field: SortField }) => {
    if (sortField !== field) return null;
    return sortDirection === "asc" ? (
      <ChevronUpIcon className="h-4 w-4 inline ml-1" />
    ) : (
      <ChevronDownIcon className="h-4 w-4 inline ml-1" />
    );
  };

  const buttonClass = (field: SortField) =>
    `px-3 py-1 text-sm rounded-md transition-colors ${
      sortField === field
        ? "bg-indigo-100 text-indigo-700"
        : "bg-gray-100 text-gray-600 hover:bg-gray-200"
    }`;

  return (
    <div className="flex items-center gap-2 mb-4">
      <span className="text-sm text-gray-500">Sort by:</span>
      <button className={buttonClass("vaccineName")} onClick={() => onSort("vaccineName")}>
        Name <SortIcon field="vaccineName" />
      </button>
      <button className={buttonClass("durationMonths")} onClick={() => onSort("durationMonths")}>
        Duration <SortIcon field="durationMonths" />
      </button>
      <button className={buttonClass("requiredForAdoption")} onClick={() => onSort("requiredForAdoption")}>
        Required <SortIcon field="requiredForAdoption" />
      </button>
    </div>
  );
}

export type { SortField, SortDirection };