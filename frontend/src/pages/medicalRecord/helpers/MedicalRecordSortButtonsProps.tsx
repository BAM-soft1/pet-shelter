import { ChevronUpIcon, ChevronDownIcon } from "@heroicons/react/24/outline";

export type SortField = "id" | "animalName" | "date" | "diagnosis" | "treatment" | "cost";
export type SortDirection = "asc" | "desc";

type MedicalRecordSortButtonsProps = {
  sortField: SortField;
  sortDirection: SortDirection;
  onSort: (field: SortField) => void;
};

const sortOptions: { field: SortField; label: string }[] = [
  { field: "id", label: "ID" },
  { field: "animalName", label: "Animal" },
  { field: "date", label: "Date" },
  { field: "diagnosis", label: "Diagnosis" },
  { field: "treatment", label: "Treatment" },
  { field: "cost", label: "Cost" },
];

export default function MedicalRecordSortButtons({
  sortField,
  sortDirection,
  onSort,
}: MedicalRecordSortButtonsProps) {
  return (
    <div className="flex flex-wrap gap-2 mb-4">
      <span className="text-sm font-medium text-gray-700 self-center mr-2">Sort by:</span>
      {sortOptions.map(({ field, label }) => {
        const isActive = sortField === field;
        return (
          <button
            key={field}
            onClick={() => onSort(field)}
            className={`flex items-center px-3 py-1.5 text-sm rounded-md border transition-colors ${
              isActive
                ? "bg-indigo-600 text-white border-indigo-600"
                : "bg-white text-gray-700 border-gray-300 hover:bg-gray-50"
            }`}
          >
            {label}
            {isActive && (
              sortDirection === "asc" ? (
                <ChevronUpIcon className="h-4 w-4 ml-1" />
              ) : (
                <ChevronDownIcon className="h-4 w-4 ml-1" />
              )
            )}
          </button>
        );
      })}
    </div>
  );
}