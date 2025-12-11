import { Button } from "@/components/ui/button";
import { ChevronUpIcon, ChevronDownIcon } from "@heroicons/react/24/outline";

export type SortField = "id" | "animalName" | "applicantName" | "applicationDate" | "status";
export type SortDirection = "asc" | "desc";

type ApplicationSortButtonsProps = {
  sortField: SortField;
  sortDirection: SortDirection;
  onSort: (field: SortField) => void;
};

const sortOptions: { field: SortField; label: string }[] = [
  { field: "id", label: "ID" },
  { field: "animalName", label: "Animal" },
  { field: "applicantName", label: "Applicant" },
  { field: "applicationDate", label: "Date" },
  { field: "status", label: "Status" },
];

export default function ApplicationSortButtons({
  sortField,
  sortDirection,
  onSort,
}: ApplicationSortButtonsProps) {
  return (
    <div className="flex flex-wrap gap-2 items-center">
      <span className="text-sm font-medium mr-2">Sort by:</span>
      {sortOptions.map(({ field, label }) => {
        const isActive = sortField === field;
        return (
          <Button
            key={field}
            variant={isActive ? "default" : "outline"}
            size="sm"
            onClick={() => onSort(field)}
          >
            {label}
            {isActive && (
              sortDirection === "asc" ? (
                <ChevronUpIcon className="h-4 w-4 ml-1" />
              ) : (
                <ChevronDownIcon className="h-4 w-4 ml-1" />
              )
            )}
          </Button>
        );
      })}
    </div>
  );
}