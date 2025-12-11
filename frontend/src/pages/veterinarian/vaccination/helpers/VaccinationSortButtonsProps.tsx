import { ChevronUpIcon, ChevronDownIcon } from "@heroicons/react/24/outline";

type SortField = "animalName" | "vaccinationType" | "dateAdministered" | "nextDueDate";
type SortDirection = "asc" | "desc";

type VaccinationSortButtonsProps = {
  sortField: SortField;
  sortDirection: SortDirection;
  onSort: (field: SortField) => void;
};

export default function VaccinationSortButtons({
  sortField,
  sortDirection,
  onSort,
}: VaccinationSortButtonsProps) {
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
      <button className={buttonClass("animalName")} onClick={() => onSort("animalName")}>
        Animal <SortIcon field="animalName" />
      </button>
      <button className={buttonClass("vaccinationType")} onClick={() => onSort("vaccinationType")}>
        Vaccine <SortIcon field="vaccinationType" />
      </button>
      <button className={buttonClass("dateAdministered")} onClick={() => onSort("dateAdministered")}>
        Date <SortIcon field="dateAdministered" />
      </button>
      <button className={buttonClass("nextDueDate")} onClick={() => onSort("nextDueDate")}>
        Next Due <SortIcon field="nextDueDate" />
      </button>
    </div>
  );
}

export type { SortField, SortDirection };