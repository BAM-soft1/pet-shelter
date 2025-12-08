import { useState, useEffect } from "react";
import type { MedicalRecord } from "@/types/types";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { PlusIcon, PencilIcon, TrashIcon, EyeIcon } from "@heroicons/react/24/outline";
import { getErrorMessage } from "@/services/fetchUtils";
import { MedicalRecordService } from "../../api/medicalRecord";

import MedicalRecordDetailModal from "./dialogs/MedicalRecordDetailModal";
import MedicalRecordFormDialog from "./dialogs/MedicalRecordFormDialog";
import DeleteConfirmDialogVet from "./dialogs/DeleteConfirmDialogVet";
import MedicalRecordFilters from "./helpers/MedicalRecordFiltersProps";
import MedicalRecordSortButtons from "./helpers/MedicalRecordSortButtonsProps";
import { useMedicalRecordFilters } from "./helpers/useMedicalRecordFilters";

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

  const [isFormOpen, setIsFormOpen] = useState(false);
  const [isDeleteOpen, setIsDeleteOpen] = useState(false);
  const [isDetailOpen, setIsDetailOpen] = useState(false);
  const [selectedRecord, setSelectedRecord] = useState<MedicalRecord | null>(null);

  const {
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
  } = useMedicalRecordFilters(records);

  const fetchMedicalRecords = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await MedicalRecordService.getAllMedicalRecords();
      setRecords(data.filter((record) => record.animal !== null));
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMedicalRecords();
  }, []);

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
    <>
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-semibold text-gray-800">Medical Records Overview</h2>
        <button
          onClick={handleAddClick}
          className="flex items-center px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-500 transition-colors"
        >
          <PlusIcon className="h-5 w-5 mr-2" />
          Add Record
        </button>
      </div>

      <MedicalRecordFilters
        searchTerm={searchTerm}
        setSearchTerm={setSearchTerm}
        dateFrom={dateFrom}
        setDateFrom={setDateFrom}
        dateTo={dateTo}
        setDateTo={setDateTo}
        clearFilters={clearFilters}
        totalCount={records.length}
        filteredCount={filteredAndSortedRecords.length}
      />

      <MedicalRecordSortButtons sortField={sortField} sortDirection={sortDirection} onSort={handleSort} />

      {filteredAndSortedRecords.length === 0 ? (
        <div className="text-center py-12 text-gray-500">No medical records found</div>
      ) : (
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
            {filteredAndSortedRecords.map((record) => (
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
      )}

      <MedicalRecordDetailModal record={selectedRecord} isOpen={isDetailOpen} onClose={() => setIsDetailOpen(false)} />

      <MedicalRecordFormDialog record={selectedRecord} isOpen={isFormOpen} onClose={() => setIsFormOpen(false)} onSubmit={handleFormSubmit} />

      <DeleteConfirmDialogVet record={selectedRecord} isOpen={isDeleteOpen} onClose={() => setIsDeleteOpen(false)} onConfirm={handleDeleteConfirm} />
    </>
  );
}
