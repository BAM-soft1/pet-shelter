import { useState, useEffect } from "react";
import type { VaccinationType } from "@/types/types";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import { EyeIcon, PencilIcon, PlusIcon, TrashIcon } from "@heroicons/react/24/outline";
import { Button } from "@/components/ui/button";
import { getErrorMessage } from "@/services/fetchUtils";
import { VaccinationService } from "@/api/vaccination";
import VaccinationTypeDetailModal from "./dialogs/vaccinationTypeDetailModal";
import VaccinationTypeFormModal from "./dialogs/vaccinationTypeFormModal";

type VaccinationTypeFormData = {
    vaccineName: string;
    description: string;
    durationMonths: number;
    requiredForAdoption: boolean;
};

export default function VaccinationTypeOverview() {
    const [vaccinationTypes, setVaccinationTypes] = useState<VaccinationType[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);
    const [isFormOpen, setIsFormOpen] = useState(false);
    const [selectedVaccinationType, setSelectedVaccinationType] = useState<VaccinationType | null>(null);

    const fetchVaccinationTypes = async () => {
        try {
            setLoading(true);
            setError(null);
            const data = await VaccinationService.getAllVaccinationTypes();
            setVaccinationTypes(data);
        } catch (err) {
            setError(getErrorMessage(err));
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchVaccinationTypes();
    }, []);

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
    }

    return (
        <div>
            <div className="flex items-center justify-between mb-4">
                <h1 className="text-2xl font-bold">Vaccination Types</h1>
                <Button variant="default" onClick={handleAddClick}>
                    <PlusIcon className="h-5 w-5 mr-2" />
                    Add Vaccination Type
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
                                            <button 
                                                onClick={() => handleViewClick(type)}
                                                className="text-green-600 hover:text-green-800"
                                                title="View details"
                                            >
                                                <EyeIcon className="h-5 w-5" />
                                            </button>
                                            <button
                                                onClick={() => handleEditClick(type)}
                                                className="text-blue-600 hover:text-blue-800"
                                                title="Edit vaccination type"
                                            >
                                                <PencilIcon className="h-5 w-5" />
                                            </button>
                                            <button className="text-red-600 hover:text-red-800">
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

            <VaccinationTypeDetailModal
                isOpen={isDetailModalOpen}
                onClose={() => setIsDetailModalOpen(false)}
                vaccinationType={selectedVaccinationType}
            />

            <VaccinationTypeFormModal
                isOpen={isFormOpen}
                onClose={() => setIsFormOpen(false)}
                vaccinationType={selectedVaccinationType}
                onSubmit={handleFormSubmit}
            />
        </div>
    );
}