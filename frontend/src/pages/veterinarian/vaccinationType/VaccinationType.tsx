import { useState, useEffect, useCallback } from "react";
import type { VaccinationType } from "@/types/types";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Button } from "@/components/ui/button";
import { EyeIcon, PencilIcon, PlusIcon, TrashIcon } from "@heroicons/react/24/outline";
import { getErrorMessage } from "@/services/fetchUtils";
import { VaccinationService } from "@/api/vaccination";
import SearchAndFilter from "@/components/ui/SearchAndFilter";
import PaginationControls from "@/components/ui/PaginationControls";
import VaccinationTypeDetailModal from "./dialogs/vaccinationTypeDetailModal";
import VaccinationTypeFormModal from "./dialogs/vaccinationTypeFormModal";
import VaccinationTypeDeleteModal from "./dialogs/vaccinationTypeDelete";

type VaccinationTypeFormData = {
  vaccineName: string;
  description: string;
  durationMonths: number;
  requiredForAdoption: boolean;
};

export default function VaccinationTypeOverview() {
  const [vaccinationTypes, setVaccinationTypes] = useState<VaccinationType[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Pagination state
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const pageSize = 10;

  // Search and filter state
  const [searchInput, setSearchInput] = useState("");
  const [searchTerm, setSearchTerm] = useState("");
  const [requiredForAdoption, setRequiredForAdoption] = useState<boolean | undefined>(undefined);

  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [isDeleteOpen, setIsDeleteOpen] = useState(false);
  const [selectedVaccinationType, setSelectedVaccinationType] = useState<VaccinationType | null>(null);

  const handleSearch = () => {
    setSearchTerm(searchInput);
    setCurrentPage(0);
  };

  const fetchVaccinationTypes = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const pageResponse = await VaccinationService.getVaccinationTypes(
        currentPage,
        pageSize,
        "vaccineName",
        "asc",
        requiredForAdoption,
        searchTerm || undefined
      );

      setVaccinationTypes(pageResponse.content);
      setTotalPages(pageResponse.totalPages);
      setTotalElements(pageResponse.totalElements);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  }, [currentPage, searchTerm, pageSize, requiredForAdoption]);

  useEffect(() => {
    fetchVaccinationTypes();
  }, [fetchVaccinationTypes]);

  const handleViewClick = (type: VaccinationType) => {
    setSelectedVaccinationType(type);
    setIsDetailModalOpen(true);
  };

  const handleAddClick = () => {
    setSelectedVaccinationType(null);
    setIsFormOpen(true);
  };

  const handleEditClick = (type: VaccinationType) => {
    setSelectedVaccinationType(type);
    setIsFormOpen(true);
  };

  const handleDeleteClick = (type: VaccinationType) => {
    setSelectedVaccinationType(type);
    setIsDeleteOpen(true);
  };

  const handleFormSubmit = async (data: VaccinationTypeFormData) => {
    try {
      if (selectedVaccinationType) {
        await VaccinationService.updateVaccinationType(selectedVaccinationType.id, data);
      } else {
        await VaccinationService.createVaccinationType(data);
      }
      setIsFormOpen(false);
      fetchVaccinationTypes();
    } catch (err) {
      alert("Error saving vaccination type: " + getErrorMessage(err));
    }
  };

  const handleDelete = async (vaccinationTypeId: number) => {
    try {
      await VaccinationService.deleteVaccinationType(vaccinationTypeId);
      setIsDeleteOpen(false);
      fetchVaccinationTypes();
    } catch (err) {
      alert("Error deleting vaccination type: " + getErrorMessage(err));
    }
  };

  return (
    <div className="p-6 bg-white rounded-lg shadow-md">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-semibold text-gray-800">Vaccination Types</h2>
        <button
          onClick={handleAddClick}
          className="flex items-center px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-500 transition-colors"
        >
          <PlusIcon className="h-5 w-5 mr-2" />
          Add Vaccination Type
        </button>
      </div>

      <SearchAndFilter
        searchValue={searchInput}
        onSearchChange={setSearchInput}
        searchPlaceholder="Search by vaccine name or description..."
        onClearFilters={() => {
          setSearchInput("");
          setSearchTerm("");
          setRequiredForAdoption(undefined);
          setCurrentPage(0);
        }}
        onSearchSubmit={handleSearch}
        showSearchButton={true}
        showClearButton={true}
        totalCount={totalElements}
        filteredCount={totalElements}
        showCounts={true}
        showFilters={false}
      />

      {/* Additional Filters */}
      <div className="flex gap-4 flex-wrap mb-6">
        <div>
          <label className="block text-sm font-medium mb-2">Required for Adoption</label>
          <div className="flex gap-2">
            <Button variant={requiredForAdoption === undefined ? "default" : "outline"} size="sm" onClick={() => setRequiredForAdoption(undefined)}>
              All
            </Button>
            <Button variant={requiredForAdoption === true ? "default" : "outline"} size="sm" onClick={() => setRequiredForAdoption(true)}>
              Required
            </Button>
            <Button variant={requiredForAdoption === false ? "default" : "outline"} size="sm" onClick={() => setRequiredForAdoption(false)}>
              Optional
            </Button>
          </div>
        </div>
      </div>

      {loading ? (
        <p>Loading...</p>
      ) : error ? (
        <p className="text-red-500">Error: {error}</p>
      ) : (
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Name</TableHead>
              <TableHead>Description</TableHead>
              <TableHead>Duration (Months)</TableHead>
              <TableHead>Required for Adoption</TableHead>
              <TableHead>Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {vaccinationTypes.length === 0 ? (
              <TableRow>
                <TableCell colSpan={5} className="text-center">
                  No vaccination types found.
                </TableCell>
              </TableRow>
            ) : (
              vaccinationTypes.map((type) => (
                <TableRow key={type.id}>
                  <TableCell>{type.vaccineName}</TableCell>
                  <TableCell>{type.description}</TableCell>
                  <TableCell>{type.durationMonths}</TableCell>
                  <TableCell>{type.requiredForAdoption ? "Yes" : "No"}</TableCell>
                  <TableCell>
                    <div className="flex space-x-2">
                      <button onClick={() => handleViewClick(type)} className="text-green-600 hover:text-green-800" title="View details">
                        <EyeIcon className="h-5 w-5" />
                      </button>
                      <button onClick={() => handleEditClick(type)} className="text-blue-600 hover:text-blue-800" title="Edit vaccination type">
                        <PencilIcon className="h-5 w-5" />
                      </button>
                      <button onClick={() => handleDeleteClick(type)} className="text-red-600 hover:text-red-800" title="Delete vaccination type">
                        <TrashIcon className="h-5 w-5" />
                      </button>
                    </div>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      )}

      <PaginationControls currentPage={currentPage} totalPages={totalPages} totalElements={totalElements} onPageChange={setCurrentPage} />

      <VaccinationTypeDetailModal isOpen={isDetailModalOpen} onClose={() => setIsDetailModalOpen(false)} vaccinationType={selectedVaccinationType} />

      <VaccinationTypeFormModal
        isOpen={isFormOpen}
        onClose={() => setIsFormOpen(false)}
        vaccinationType={selectedVaccinationType}
        onSubmit={handleFormSubmit}
      />

      {selectedVaccinationType && (
        <VaccinationTypeDeleteModal
          isOpen={isDeleteOpen}
          onClose={() => setIsDeleteOpen(false)}
          vaccinationType={selectedVaccinationType}
          onDelete={handleDelete}
        />
      )}
    </div>
  );
}
