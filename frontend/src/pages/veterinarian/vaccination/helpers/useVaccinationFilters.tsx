import { useState, useMemo } from "react";
import type { Vaccination } from "@/types/types";
import type { SortField, SortDirection } from "./VaccinationSortButtonsProps";

export function useVaccinationFilters(vaccinations: Vaccination[]) {
  const [searchTerm, setSearchTerm] = useState("");
  const [animalFilter, setAnimalFilter] = useState("");
  const [vaccinationTypeFilter, setVaccinationTypeFilter] = useState("");
  const [dateFrom, setDateFrom] = useState("");
  const [dateTo, setDateTo] = useState("");
  const [sortField, setSortField] = useState<SortField>("dateAdministered");
  const [sortDirection, setSortDirection] = useState<SortDirection>("desc");

  const filteredAndSortedVaccinations = useMemo(() => {
    let result = [...vaccinations];

    // Filter by search term
    if (searchTerm) {
      const lowerSearch = searchTerm.toLowerCase();
      result = result.filter(
        (v) =>
          v.animal?.name?.toLowerCase().includes(lowerSearch) ||
          v.vaccinationType?.vaccineName?.toLowerCase().includes(lowerSearch)
      );
    }

    // Filter by animal
    if (animalFilter) {
      result = result.filter((v) => v.animal?.id?.toString() === animalFilter);
    }

    // Filter by vaccination type
    if (vaccinationTypeFilter) {
      result = result.filter((v) => v.vaccinationType?.id?.toString() === vaccinationTypeFilter);
    }

    // Filter by date range
    if (dateFrom) {
      const fromDate = new Date(dateFrom);
      result = result.filter((v) => new Date(v.dateAdministered) >= fromDate);
    }

    if (dateTo) {
      const toDate = new Date(dateTo);
      result = result.filter((v) => new Date(v.dateAdministered) <= toDate);
    }

    // Sort
    result.sort((a, b) => {
      let comparison = 0;

      switch (sortField) {
        case "animalName":
          comparison = (a.animal?.name ?? "").localeCompare(b.animal?.name ?? "");
          break;
        case "vaccinationType":
          comparison = (a.vaccinationType?.vaccineName ?? "").localeCompare(
            b.vaccinationType?.vaccineName ?? ""
          );
          break;
        case "dateAdministered":
          comparison = new Date(a.dateAdministered).getTime() - new Date(b.dateAdministered).getTime();
          break;
        case "nextDueDate":
          comparison = new Date(a.nextDueDate).getTime() - new Date(b.nextDueDate).getTime();
          break;
      }

      return sortDirection === "asc" ? comparison : -comparison;
    });

    return result;
  }, [vaccinations, searchTerm, animalFilter, vaccinationTypeFilter, dateFrom, dateTo, sortField, sortDirection]);

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
    setAnimalFilter("");
    setVaccinationTypeFilter("");
    setDateFrom("");
    setDateTo("");
  };

  return {
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
    sortField,
    sortDirection,
    handleSort,
    clearFilters,
    filteredVaccinations: filteredAndSortedVaccinations,
    totalCount: vaccinations.length,
    filteredCount: filteredAndSortedVaccinations.length,
  };
}