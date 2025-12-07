import { useState, useMemo } from "react";
import type { MedicalRecord } from "@/types/types";
import type { SortField, SortDirection } from "../components/MedicalRecordSortButtons";

export function useMedicalRecordFilters(records: MedicalRecord[]) {
  const [searchTerm, setSearchTerm] = useState("");
  const [dateFrom, setDateFrom] = useState("");
  const [dateTo, setDateTo] = useState("");
  const [sortField, setSortField] = useState<SortField>("date");
  const [sortDirection, setSortDirection] = useState<SortDirection>("desc");

  const filteredAndSortedRecords = useMemo(() => {
    let filtered = records.filter((record) => {
      const searchLower = searchTerm.toLowerCase();
      const matchesSearch =
        searchTerm === "" ||
        record.animal?.name?.toLowerCase().includes(searchLower) ||
        record.diagnosis.toLowerCase().includes(searchLower) ||
        record.treatment.toLowerCase().includes(searchLower);

      const recordDate = new Date(record.date);
      const matchesDateFrom = !dateFrom || recordDate >= new Date(dateFrom);
      const matchesDateTo = !dateTo || recordDate <= new Date(dateTo);

      return matchesSearch && matchesDateFrom && matchesDateTo;
    });

    filtered.sort((a, b) => {
      let comparison = 0;
      switch (sortField) {
        case "id":
          comparison = a.id - b.id;
          break;
        case "animalName":
          comparison = (a.animal?.name || "").localeCompare(b.animal?.name || "");
          break;
        case "date":
          comparison = new Date(a.date).getTime() - new Date(b.date).getTime();
          break;
        case "diagnosis":
          comparison = a.diagnosis.localeCompare(b.diagnosis);
          break;
        case "treatment":
          comparison = a.treatment.localeCompare(b.treatment);
          break;
        case "cost":
          comparison = a.cost - b.cost;
          break;
      }
      return sortDirection === "asc" ? comparison : -comparison;
    });

    return filtered;
  }, [records, searchTerm, dateFrom, dateTo, sortField, sortDirection]);

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
    setDateFrom("");
    setDateTo("");
  };

  return {
    searchTerm,
    setSearchTerm,
    dateFrom,
    setDateFrom,
    dateTo,
    setDateTo,
    sortField,
    sortDirection,
    handleSort,
    clearFilters,
    filteredAndSortedRecords,
  };
}