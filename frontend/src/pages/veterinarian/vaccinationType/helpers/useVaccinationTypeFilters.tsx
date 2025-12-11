import { useState, useMemo } from "react";
import type { VaccinationType } from "@/types/types";
import type { SortField, SortDirection } from "./VaccinationTypeSortButtonsProps";

export function useVaccinationTypeFilters(vaccinationTypes: VaccinationType[]) {
  const [searchTerm, setSearchTerm] = useState("");
  const [requiredFilter, setRequiredFilter] = useState("");
  const [sortField, setSortField] = useState<SortField>("vaccineName");
  const [sortDirection, setSortDirection] = useState<SortDirection>("asc");

  const filteredAndSortedTypes = useMemo(() => {
    let result = [...vaccinationTypes];

    // Filter by search term
    if (searchTerm) {
      const lowerSearch = searchTerm.toLowerCase();
      result = result.filter(
        (type) =>
          type.vaccineName.toLowerCase().includes(lowerSearch) ||
          (type.description && type.description.toLowerCase().includes(lowerSearch))
      );
    }

    // Filter by required for adoption
    if (requiredFilter) {
      const isRequired = requiredFilter === "yes";
      result = result.filter((type) => type.requiredForAdoption === isRequired);
    }

    // Sort
    result.sort((a, b) => {
      let comparison = 0;

      switch (sortField) {
        case "vaccineName":
          comparison = a.vaccineName.localeCompare(b.vaccineName);
          break;
        case "durationMonths":
          comparison = a.durationMonths - b.durationMonths;
          break;
        case "requiredForAdoption":
          comparison = (a.requiredForAdoption ? 1 : 0) - (b.requiredForAdoption ? 1 : 0);
          break;
      }

      return sortDirection === "asc" ? comparison : -comparison;
    });

    return result;
  }, [vaccinationTypes, searchTerm, requiredFilter, sortField, sortDirection]);

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
    setRequiredFilter("");
  };

  return {
    searchTerm,
    setSearchTerm,
    requiredFilter,
    setRequiredFilter,
    sortField,
    sortDirection,
    handleSort,
    clearFilters,
    filteredTypes: filteredAndSortedTypes,
    totalCount: vaccinationTypes.length,
    filteredCount: filteredAndSortedTypes.length,
  };
}