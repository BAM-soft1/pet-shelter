import { useState, useEffect, useCallback } from "react";
import type { MedicalRecord } from "@/types/types";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { PlusIcon, PencilIcon, TrashIcon, EyeIcon } from "@heroicons/react/24/outline";
import { getErrorMessage } from "@/services/fetchUtils";
import { MedicalRecordService } from "../../../api/medicalRecord";
import SearchAndFilter from "@/components/ui/SearchAndFilter";
import PaginationControls from "@/components/ui/PaginationControls";
import MedicalRecordDetailModal from "./dialogs/MedicalRecordDetailModal";
import MedicalRecordFormDialog from "./dialogs/MedicalRecordFormDialog";
import DeleteConfirmDialogVet from "./dialogs/DeleteConfirmDialogVet";

type MedicalRecordFormData = {
  animalId: number | null;
  date: string;
  diagnosis: string;
  treatment: string;
  cost: number;
};

export default function MedicalRecordOverview() {
  const [records, setRecords] = useState<MedicalRecord[]>([]);
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
  const [startDate, setStartDate] = useState<string>("");
  const [endDate, setEndDate] = useState<string>("");

  const [isFormOpen, setIsFormOpen] = useState(false);
  const [isDeleteOpen, setIsDeleteOpen] = useState(false);
  const [isDetailOpen, setIsDetailOpen] = useState(false);
  const [selectedRecord, setSelectedRecord] = useState<MedicalRecord | null>(null);

  const handleSearch = () => {
    setSearchTerm(searchInput);
    setCurrentPage(0);
  };

  const fetchMedicalRecords = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const pageResponse = await MedicalRecordService.getMedicalRecords(
        currentPage,
        pageSize,
        "date",
        "desc",
        animalStatus,
        startDate || undefined,
        endDate || undefined,
        searchTerm || undefined
      );

      setRecords(pageResponse.content);
      setTotalPages(pageResponse.totalPages);
      setTotalElements(pageResponse.totalElements);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  }, [currentPage, searchTerm, pageSize, animalStatus, startDate, endDate]);

  useEffect(() => {
    fetchMedicalRecords();
  }, [fetchMedicalRecords]);

  const handleAddClick = () => {
    setSelectedRecord(null);
    setIsFormOpen(true);
  };

  const handleEditClick = (record: MedicalRecord) => {
    setSelectedRecord(record);
    setIsFormOpen(true);
  };

  const handleDeleteClick = (record: MedicalRecord) => {
    setSelectedRecord(record);
    setIsDeleteOpen(true);
  };

  const handleViewClick = (record: MedicalRecord) => {
    setSelectedRecord(record);
    setIsDetailOpen(true);
  };

  const handleFormSubmit = async (data: MedicalRecordFormData) => {
    try {
      if (selectedRecord) {
        const updatePayload = {
          animalId: data.animalId!,
          date: new Date(data.date).toISOString(),
          diagnosis: data.diagnosis,
          treatment: data.treatment,
          cost: data.cost,
        };
        await MedicalRecordService.updateMedicalRecord(selectedRecord.id, updatePayload);
      } else {
        const createPayload = {
          animalId: data.animalId!,
          date: data.date,
          diagnosis: data.diagnosis,
          treatment: data.treatment,
          cost: data.cost,
        };
        await MedicalRecordService.createMedicalRecord(createPayload);
      }
      await fetchMedicalRecords();
      setIsFormOpen(false);
    } catch (err) {
      console.error("Failed to submit medical record:", err);
      setError("Failed to submit. Please try again.");
    }
  };

  const handleDeleteConfirm = async () => {
    if (selectedRecord) {
      try {
        await MedicalRecordService.deleteMedicalRecord(selectedRecord.id);
        await fetchMedicalRecords();
        setIsDeleteOpen(false);
      } catch (err) {
        console.error("Failed to delete medical record:", err);
        throw err;
      }
    }
  };

  if (loading) {
    return <div className="text-center py-12">Loading...</div>;
  }

  if (error) {
    return <div className="text-center py-12 text-red-600">Error: {error}</div>;
  }

  return (
    <div className="p-4 md:p-6 bg-white rounded-lg shadow-md">
      <div className="flex justify-between items-center mb-6 gap-4">
        <h2 className="text-xl md:text-2xl font-semibold text-gray-800">Medical Record Overview</h2>
        <button
          onClick={handleAddClick}
          className="flex items-center px-3 py-2 md:px-4 text-sm md:text-base bg-indigo-600 text-white rounded hover:bg-indigo-500 transition-colors whitespace-nowrap"
        >
          <PlusIcon className="h-4 w-4 md:h-5 md:w-5 mr-1 md:mr-2" />
          <span className="hidden sm:inline">Add Record</span>
          <span className="sm:hidden">Add</span>
        </button>
      </div>

      <SearchAndFilter
        searchValue={searchInput}
        onSearchChange={setSearchInput}
        searchPlaceholder="Search by animal, diagnosis, or treatment..."
        onClearFilters={() => {
          setSearchInput("");
          setSearchTerm("");
          setAnimalStatus(undefined);
          setStartDate("");
          setEndDate("");
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
          <div className="flex flex-wrap gap-2">
            <Button
              variant={animalStatus === undefined ? "default" : "outline"}
              size="sm"
              className="text-xs sm:text-sm"
              onClick={() => setAnimalStatus(undefined)}
            >
              All
            </Button>
            <Button
              variant={animalStatus === "available" ? "default" : "outline"}
              size="sm"
              className="text-xs sm:text-sm"
              onClick={() => setAnimalStatus("available")}
            >
              Available
            </Button>
            <Button
              variant={animalStatus === "fostered" ? "default" : "outline"}
              size="sm"
              className="text-xs sm:text-sm"
              onClick={() => setAnimalStatus("fostered")}
            >
              Fostered
            </Button>
            <Button
              variant={animalStatus === "adopted" ? "default" : "outline"}
              size="sm"
              className="text-xs sm:text-sm"
              onClick={() => setAnimalStatus("adopted")}
            >
              Adopted
            </Button>
          </div>
        </div>

        <div>
          <span className="block text-sm font-medium mb-2">Date Range</span>
          <div className="flex flex-col sm:flex-row gap-2 items-stretch sm:items-center">
            <input
              type="date"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
              className="px-2 sm:px-3 py-1.5 text-xs sm:text-sm border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring w-full sm:w-auto"
            />
            <span className="text-xs sm:text-sm text-muted-foreground hidden sm:inline">to</span>
            <span className="text-xs text-muted-foreground sm:hidden text-center">to</span>
            <input
              type="date"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
              className="px-2 sm:px-3 py-1.5 text-xs sm:text-sm border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring w-full sm:w-auto"
            />
          </div>
        </div>
      </div>

      {loading ? (
        <div className="text-center py-12">Loading...</div>
      ) : error ? (
        <div className="text-center py-12 text-red-600">Error: {error}</div>
      ) : records.length === 0 ? (
        <div className="text-center py-12 text-gray-500">No medical records found</div>
      ) : (
        <>
          {/* Mobile Card View */}
          <div className="md:hidden space-y-4 px-4">
            {records.map((record) => (
              <Card key={record.id}>
                <CardContent className="p-4">
                  <div className="flex gap-4">
                    {record.animal?.imageUrl && (
                      <img src={record.animal.imageUrl} alt={record.animal.name} className="w-16 h-16 rounded-lg object-cover shrink-0" />
                    )}
                    <div className="flex-1 min-w-0">
                      <div className="flex items-start justify-between gap-2 mb-2">
                        <div className="flex-1 min-w-0">
                          <h3 className="font-semibold text-base truncate">{record.animal?.name || "N/A"}</h3>
                          <p className="text-xs text-muted-foreground">{new Date(record.date).toLocaleDateString()}</p>
                        </div>
                        <div className="flex gap-1 shrink-0">
                          <button
                            onClick={() => handleViewClick(record)}
                            className="p-1.5 text-green-600 hover:text-green-800 hover:bg-green-50 rounded"
                            title="View"
                          >
                            <EyeIcon className="h-4 w-4" />
                          </button>
                          <button
                            onClick={() => handleEditClick(record)}
                            className="p-1.5 text-blue-600 hover:text-blue-800 hover:bg-blue-50 rounded"
                            title="Edit"
                          >
                            <PencilIcon className="h-4 w-4" />
                          </button>
                          <button
                            onClick={() => handleDeleteClick(record)}
                            className="p-1.5 text-red-600 hover:text-red-800 hover:bg-red-50 rounded"
                            title="Delete"
                          >
                            <TrashIcon className="h-4 w-4" />
                          </button>
                        </div>
                      </div>
                      <div className="space-y-1.5 text-sm">
                        <div>
                          <span className="text-muted-foreground text-xs">Diagnosis: </span>
                          <span className="text-xs line-clamp-2">{record.diagnosis}</span>
                        </div>
                        <div>
                          <span className="text-muted-foreground text-xs">Treatment: </span>
                          <span className="text-xs line-clamp-2">{record.treatment}</span>
                        </div>
                        <div className="flex justify-between items-center pt-1">
                          <span className="text-muted-foreground text-xs">Cost:</span>
                          <span className="font-semibold text-green-600">${record.cost.toFixed(2)}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>

          {/* Desktop Table View */}
          <div className="hidden md:block">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Record ID</TableHead>
                  <TableHead>Animal Name</TableHead>
                  <TableHead>Date</TableHead>
                  <TableHead>Diagnosis</TableHead>
                  <TableHead>Treatment</TableHead>
                  <TableHead>Medical Price</TableHead>
                  <TableHead>Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {records.map((record) => (
                  <TableRow key={record.id}>
                    <TableCell>{record.id}</TableCell>
                    <TableCell>{record.animal?.name || "N/A"}</TableCell>
                    <TableCell>{new Date(record.date).toLocaleDateString()}</TableCell>
                    <TableCell>{record.diagnosis}</TableCell>
                    <TableCell>{record.treatment}</TableCell>
                    <TableCell>${record.cost.toFixed(2)}</TableCell>
                    <TableCell>
                      <div className="flex gap-2">
                        <button onClick={() => handleViewClick(record)} className="text-green-600 hover:text-green-800" title="View details">
                          <EyeIcon className="h-5 w-5" />
                        </button>
                        <button onClick={() => handleEditClick(record)} className="text-blue-600 hover:text-blue-800" title="Edit">
                          <PencilIcon className="h-5 w-5" />
                        </button>
                        <button onClick={() => handleDeleteClick(record)} className="text-red-600 hover:text-red-800" title="Delete">
                          <TrashIcon className="h-5 w-5" />
                        </button>
                      </div>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>
        </>
      )}

      <PaginationControls currentPage={currentPage} totalPages={totalPages} totalElements={totalElements} onPageChange={setCurrentPage} />

      <MedicalRecordDetailModal record={selectedRecord} isOpen={isDetailOpen} onClose={() => setIsDetailOpen(false)} />

      <MedicalRecordFormDialog record={selectedRecord} isOpen={isFormOpen} onClose={() => setIsFormOpen(false)} onSubmit={handleFormSubmit} />

      <DeleteConfirmDialogVet record={selectedRecord} isOpen={isDeleteOpen} onClose={() => setIsDeleteOpen(false)} onConfirm={handleDeleteConfirm} />
    </div>
  );
}
