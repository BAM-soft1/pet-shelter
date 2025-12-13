import { useState, useEffect, useCallback } from "react";
import type { Vaccination, VaccinationRequest } from "@/types/types";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Button } from "@/components/ui/button";
import { EyeIcon, PencilIcon, PlusIcon, TrashIcon } from "@heroicons/react/24/outline";
import { getErrorMessage } from "@/services/fetchUtils";
import { VaccinationService } from "../../../api/vaccination";
import VaccinationDetailModal from "./dialogs/VaccinationDetailModal";
import VaccinationFormDialog from "./dialogs/VaccinationFormDialog";
import VaccinationDeleteForm from "./dialogs/VaccinationDeleteModal";
import SearchAndFilter from "@/components/ui/SearchAndFilter";
import PaginationControls from "@/components/ui/PaginationControls";
import { useAuth } from "@/context/AuthProvider";

type VaccinationFormData = {
  animalId: number | null;
  vaccinationTypeId: number | null;
  dateAdministered: string;
  nextDueDate: string;
};

export default function VaccinationOverview() {
  const auth = useAuth();
  const [vaccinations, setVaccinations] = useState<Vaccination[]>([]);
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
  const [animalStatus, setAnimalStatus] = useState<string | undefined>(undefined);

  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [isDeleteOpen, setIsDeleteOpen] = useState(false);
  const [selectedVaccination, setSelectedVaccination] = useState<Vaccination | null>(null);

  const handleSearch = () => {
    setSearchTerm(searchInput);
    setCurrentPage(0);
  };

  const fetchData = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const pageResponse = await VaccinationService.getVaccinations(
        currentPage,
        pageSize,
        "dateAdministered",
        "desc",
        animalStatus,
        searchTerm || undefined
      );

      setVaccinations(pageResponse.content);
      setTotalPages(pageResponse.totalPages);
      setTotalElements(pageResponse.totalElements);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  }, [currentPage, searchTerm, pageSize, animalStatus]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const handleViewClick = (vaccination: Vaccination) => {
    setSelectedVaccination(vaccination);
    setIsDetailModalOpen(true);
  };

  const handleAddClick = () => {
    setSelectedVaccination(null);
    setIsFormOpen(true);
  };

  const handleEditClick = (vaccination: Vaccination) => {
    setSelectedVaccination(vaccination);
    setIsFormOpen(true);
  };

  const handleDeleteClick = (vaccination: Vaccination) => {
    setSelectedVaccination(vaccination);
    setIsDeleteOpen(true);
  };

  const handleFormSubmit = async (formData: VaccinationFormData): Promise<void> => {
    try {
      const requestData: VaccinationRequest = {
        animalId: formData.animalId,
        vaccinationTypeId: formData.vaccinationTypeId,
        dateAdministered: formData.dateAdministered,
        nextDueDate: formData.nextDueDate,
        userId: auth?.user?.id,
      };

      if (selectedVaccination) {
        await VaccinationService.updateVaccination(selectedVaccination.id, requestData);
      } else {
        await VaccinationService.createVaccination(requestData);
      }
      await fetchData();
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setIsFormOpen(false);
    }
  };

  const handleDeleteConfirm = async (vaccinationId: number): Promise<void> => {
    try {
      await VaccinationService.deleteVaccination(vaccinationId);
      await fetchData();
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setIsDeleteOpen(false);
    }
  };

  return (
    <div className="p-6 bg-white rounded-lg shadow-md">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-semibold text-gray-800">Vaccination Overview</h2>
        <button id="overview"
          onClick={handleAddClick}
          className="flex items-center px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-500 transition-colors"
        >
          <PlusIcon className="h-5 w-5 mr-2" />
          Add Vaccination
        </button>
      </div>

      <SearchAndFilter
        searchValue={searchInput}
        onSearchChange={setSearchInput}
        searchPlaceholder="Search by animal name or vaccine type..."
        onClearFilters={() => {
          setSearchInput("");
          setSearchTerm("");
          setAnimalStatus(undefined);
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
          <span className="block text-sm font-medium mb-2">Animal Status</span>
          <div className="flex gap-2">
            <Button variant={animalStatus === undefined ? "default" : "outline"} size="sm" onClick={() => setAnimalStatus(undefined)}>
              All
            </Button>
            <Button variant={animalStatus === "available" ? "default" : "outline"} size="sm" onClick={() => setAnimalStatus("available")}>
              Available
            </Button>
            <Button variant={animalStatus === "fostered" ? "default" : "outline"} size="sm" onClick={() => setAnimalStatus("fostered")}>
              Fostered
            </Button>
            <Button variant={animalStatus === "adopted" ? "default" : "outline"} size="sm" onClick={() => setAnimalStatus("adopted")}>
              Adopted
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
              <TableHead>Animal</TableHead>
              <TableHead>Vaccination Type</TableHead>
              <TableHead>Date Administered</TableHead>
              <TableHead>Next Due Date</TableHead>
              <TableHead>Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {vaccinations.length === 0 ? (
              <TableRow>
                <TableCell colSpan={5} className="text-center">
                  No vaccinations found.
                </TableCell>
              </TableRow>
            ) : (
              vaccinations.map((vaccination) => (
                <TableRow key={vaccination.id} className="hover:bg-gray-50">
                  <TableCell>{vaccination.animal?.name ?? vaccination.animal?.id}</TableCell>
                  <TableCell>{vaccination.vaccinationType?.vaccineName}</TableCell>
                  <TableCell>{new Date(vaccination.dateAdministered).toLocaleDateString()}</TableCell>
                  <TableCell>{new Date(vaccination.nextDueDate).toLocaleDateString()}</TableCell>
                  <TableCell>
                    <div className="flex gap-2">
                      <button onClick={() => handleViewClick(vaccination)} className="text-green-600 hover:text-green-800" title="View details">
                        <EyeIcon className="h-5 w-5" />
                      </button>
                      <button onClick={() => handleEditClick(vaccination)} className="text-blue-600 hover:text-blue-800" title="Edit vaccination">
                        <PencilIcon className="h-5 w-5" />
                      </button>
                      <button onClick={() => handleDeleteClick(vaccination)} className="text-red-600 hover:text-red-800" title="Delete vaccination">
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

      {selectedVaccination && (
        <VaccinationDeleteForm
          vaccination={selectedVaccination}
          isOpen={isDeleteOpen}
          onClose={() => setIsDeleteOpen(false)}
          onDelete={handleDeleteConfirm}
        />
      )}

      <VaccinationDetailModal vaccination={selectedVaccination} isOpen={isDetailModalOpen} onClose={() => setIsDetailModalOpen(false)} />

      <VaccinationFormDialog vaccination={selectedVaccination} isOpen={isFormOpen} onClose={() => setIsFormOpen(false)} onSubmit={handleFormSubmit} />
    </div>
  );
}
