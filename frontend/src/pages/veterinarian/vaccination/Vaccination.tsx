import { useState, useEffect } from "react";
import type { Vaccination, VaccinationRequest } from "@/types/types";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { EyeIcon, PencilIcon, PlusIcon } from "@heroicons/react/24/outline";
import { Button } from "@/components/ui/button";
import { getErrorMessage } from "@/services/fetchUtils";
import { VaccinationService } from "../../../api/vaccination";
import VaccinationDetailModal from "./dialogs/VaccinationDetailModal";
import VaccinationFormDialog from "./dialogs/VaccinationFormDialog";
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

  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedVaccination, setSelectedVaccination] = useState<Vaccination | null>(null);

  const fetchVaccinations = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await VaccinationService.getAllVaccinations();
      setVaccinations(data);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchVaccinations();
  }, []);

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
      await fetchVaccinations();
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setIsFormOpen(false);
    }
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-4">
        <h1 className="text-2xl font-bold">Vaccination Overview</h1>
        <Button onClick={handleAddClick} className="flex items-center gap-2">
          <PlusIcon className="h-5 w-5" />
          Add Vaccination
        </Button>
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
            {vaccinations.map((vaccination) => (
              <TableRow key={vaccination.id} className="hover:bg-gray-50">
                <TableCell>{vaccination.animal?.name ?? vaccination.animal?.id}</TableCell>
                <TableCell>{vaccination.vaccinationType?.vaccineName}</TableCell>
                <TableCell>
                  {new Date(vaccination.dateAdministered).toLocaleDateString()}
                </TableCell>
                <TableCell>
                  {new Date(vaccination.nextDueDate).toLocaleDateString()}
                </TableCell>
                <TableCell>
                  <div className="flex gap-2">
                    <button
                      onClick={() => handleViewClick(vaccination)}
                      className="text-green-600 hover:text-green-800"
                      title="View details"
                    >
                      <EyeIcon className="h-5 w-5" />
                    </button>
                    <button
                      onClick={() => handleEditClick(vaccination)}
                      className="text-blue-600 hover:text-blue-800"
                      title="Edit vaccination"
                    >
                      <PencilIcon className="h-5 w-5" />
                    </button>
                  </div>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      )}

      <VaccinationDetailModal
        vaccination={selectedVaccination}
        isOpen={isDetailModalOpen}
        onClose={() => setIsDetailModalOpen(false)}
      />

      <VaccinationFormDialog
        vaccination={selectedVaccination}
        isOpen={isFormOpen}
        onClose={() => setIsFormOpen(false)}
        onSubmit={handleFormSubmit}
      />
    </div>
  );
}