import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { XMarkIcon, MagnifyingGlassIcon } from "@heroicons/react/24/outline";

export type FilterOption = {
  value: string;
  label: string;
  variant?: "default" | "secondary" | "destructive" | "outline";
};

type SearchAndFilterProps = {
  searchValue: string;
  onSearchChange: (value: string) => void;
  searchPlaceholder?: string;
  filterValue?: string;
  onFilterChange?: (value: string) => void;
  filterOptions?: FilterOption[];
  filterLabel?: string;
  onClearFilters: () => void;
  showClearButton?: boolean;
  totalCount?: number;
  filteredCount?: number;
  showCounts?: boolean;
  onSearchSubmit?: () => void;
  showSearchButton?: boolean;
  showFilters?: boolean;
};

export default function SearchAndFilter({
  searchValue,
  onSearchChange,
  searchPlaceholder = "Search...",
  filterValue,
  onFilterChange,
  filterOptions,
  filterLabel = "Filter",
  onClearFilters,
  showClearButton = true,
  totalCount,
  filteredCount,
  showCounts = false,
  onSearchSubmit,
  showSearchButton = false,
  showFilters = true,
}: SearchAndFilterProps) {
  const hasActiveFilters = searchValue !== "" || (filterOptions && filterValue !== filterOptions[0]?.value);

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter" && onSearchSubmit) {
      onSearchSubmit();
    }
  };

  return (
    <div className="space-y-4">
      <div className={`grid grid-cols-1 ${showFilters ? "md:grid-cols-3" : "md:grid-cols-1"} gap-4`}>
        <div className={showFilters ? "md:col-span-2" : ""}>
          <span className="block text-sm font-medium mb-2">Search</span>
          <div className="flex gap-2">
            <div className="relative flex-1">
              <MagnifyingGlassIcon className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
              <Input
                type="text"
                value={searchValue}
                onChange={(e) => onSearchChange(e.target.value)}
                onKeyDown={handleKeyDown}
                placeholder={searchPlaceholder}
                className="w-full pl-9"
              />
            </div>
            {showSearchButton && (
              <Button onClick={onSearchSubmit} size="default">
                <MagnifyingGlassIcon className="" />
                Search
              </Button>
            )}
          </div>
        </div>
        {showFilters && filterOptions && filterOptions.length > 0 && (
          <div>
            <span className="block text-sm font-medium mb-2">{filterLabel}</span>
            <div className="flex gap-2">
              {filterOptions.map((option) => (
                <Button
                  key={option.value}
                  variant={filterValue === option.value ? option.variant || "default" : "outline"}
                  size="sm"
                  onClick={() => onFilterChange?.(option.value)}
                  className="flex-1"
                >
                  {option.label}
                </Button>
              ))}
            </div>
          </div>
        )}
      </div>

      {showClearButton && hasActiveFilters && (
        <div className="flex items-center justify-between">
          <Button variant="ghost" size="sm" onClick={onClearFilters} className="text-muted-foreground hover:text-foreground">
            <XMarkIcon className="h-4 w-4 mr-2" />
            Clear Filters
          </Button>
          {showCounts && totalCount !== undefined && filteredCount !== undefined && (
            <p className="text-sm text-muted-foreground">
              Showing {filteredCount} of {totalCount} results
            </p>
          )}
        </div>
      )}

      {showCounts && !hasActiveFilters && totalCount !== undefined && (
        <div className="flex items-center justify-end">
          <p className="text-sm text-muted-foreground">
            Showing {totalCount} {totalCount === 1 ? "result" : "results"}
          </p>
        </div>
      )}
    </div>
  );
}
