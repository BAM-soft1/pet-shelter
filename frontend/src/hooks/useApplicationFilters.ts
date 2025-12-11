import { useState, useMemo } from "react";
import type { AdoptionApplication } from "@/types/types";
import type { SortField, SortDirection } from "@/pages/admin/helpers/ApplicationSortButtons";

export function useApplicationFilters(applications: AdoptionApplication[]) {
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState<string>("all");
  const [sortField, setSortField] = useState<SortField>("applicationDate");
  const [sortDirection, setSortDirection] = useState<SortDirection>("desc");

  const filteredAndSortedApplications = useMemo(() => {
    const filtered = applications.filter((application) => {
      const searchLower = searchTerm.toLowerCase();
      const matchesSearch =
        searchTerm === "" ||
        application.animal.name.toLowerCase().includes(searchLower) ||
        application.user.firstName.toLowerCase().includes(searchLower) ||
        application.user.lastName.toLowerCase().includes(searchLower) ||
        application.user.email.toLowerCase().includes(searchLower);

      const matchesStatus = statusFilter === "all" || application.status === statusFilter;

      return matchesSearch && matchesStatus;
    });

    filtered.sort((a, b) => {
      let comparison = 0;
      switch (sortField) {
        case "id":
          comparison = a.id - b.id;
          break;
        case "animalName":
          comparison = a.animal.name.localeCompare(b.animal.name);
          break;
        case "applicantName":
          comparison = `${a.user.firstName} ${a.user.lastName}`.localeCompare(`${b.user.firstName} ${b.user.lastName}`);
          break;
        case "applicationDate":
          comparison = new Date(a.applicationDate).getTime() - new Date(b.applicationDate).getTime();
          break;
        case "status":
          comparison = a.status.localeCompare(b.status);
          break;
      }
      return sortDirection === "asc" ? comparison : -comparison;
    });

    return filtered;
  }, [applications, searchTerm, statusFilter, sortField, sortDirection]);

  const handleSort = (field: SortField) => {
    if (sortField === field) {
      setSortDirection(sortDirection === "asc" ? "desc" : "asc");
    } else {
      setSortField(field);
      setSortDirection("asc");
    }
  };

  const clearFilters = () => {
    setSearchTerm("");
    setStatusFilter("all");
  };

  return {
    searchTerm,
    setSearchTerm,
    statusFilter,
    setStatusFilter,
    sortField,
    sortDirection,
    handleSort,
    clearFilters,
    filteredAndSortedApplications,
  };
}
