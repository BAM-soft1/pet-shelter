import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { XMarkIcon } from "@heroicons/react/24/outline";

type ApplicationFiltersProps = {
  searchTerm: string;
  setSearchTerm: (value: string) => void;
  statusFilter: string;
  setStatusFilter: (value: string) => void;
  clearFilters: () => void;
  totalCount: number;
  filteredCount: number;
};

export default function ApplicationFilters({
  searchTerm,
  setSearchTerm,
  statusFilter,
  setStatusFilter,
  clearFilters,
  totalCount,
  filteredCount,
}: ApplicationFiltersProps) {
  const hasActiveFilters = searchTerm || statusFilter !== "all";

  return (
    <div className="">
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="md:col-span-2">
          <label className="block text-sm font-medium mb-2">Search</label>
          <Input
            type="text"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder="Search by animal, applicant name, or email..."
            className="w-full"
          />
        </div>
        <div>
          <label className="block text-sm font-medium mb-2">Status</label>
          <div className="flex gap-2">
            <Button
              variant={statusFilter === "all" ? "default" : "outline"}
              size="sm"
              onClick={() => setStatusFilter("all")}
              className="flex-1"
            >
              All
            </Button>
            <Button
              variant={statusFilter === "PENDING" ? "secondary" : "outline"}
              size="sm"
              onClick={() => setStatusFilter("PENDING")}
              className="flex-1"
            >
              Pending
            </Button>
            <Button
              variant={statusFilter === "APPROVED" ? "default" : "outline"}
              size="sm"
              onClick={() => setStatusFilter("APPROVED")}
              className="flex-1"
            >
              Approved
            </Button>
            <Button
              variant={statusFilter === "REJECTED" ? "destructive" : "outline"}
              size="sm"
              onClick={() => setStatusFilter("REJECTED")}
              className="flex-1"
            >
              Rejected
            </Button>
          </div>
        </div>
      </div>
      <div className="flex items-center justify-between">
        <div className="text-sm text-muted-foreground">
          Showing {filteredCount} of {totalCount} applications
        </div>
        {hasActiveFilters && (
          <Button variant="ghost" size="sm" onClick={clearFilters}>
            <XMarkIcon className="h-4 w-4 mr-2" />
            Clear Filters
          </Button>
        )}
      </div>
    </div>
  );
}