import { useState, useEffect } from "react";
import type { Vaccination } from "@/types/types";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
//import { PlusIcon, PencilIcon, TrashIcon, EyeIcon } from "@heroicons/react/24/outline";
import { getErrorMessage } from "@/services/fetchUtils";
import { VaccinationService } from "../../api/vaccination";






//type VaccinationFormData = {
  //animalId: number | null;
  //vaccinationTypeId: number | null;
 // dateAdministered: string;
 // nextDueDate: string;
//};

export default function VaccinationOverview() {
  const [vaccinations, setVaccinations] = useState<Vaccination[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);




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

  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">Vaccination Overview</h1>

        {loading ? (
            <p>Loading...</p>
        ) : error ? (
            <p className="text-red-500">Error: {error}</p>
        ) : (
            <Table>
              <TableHeader>                    </TableHeader>
                <TableRow>
                  <TableHead>Animal ID</TableHead>
                    <TableHead>Vaccination Type</TableHead>
                    <TableHead>Date Administered</TableHead>
                    <TableHead>Next Due Date</TableHead>
                </TableRow>
              <TableBody>
                {vaccinations.map((vaccination) => (
                  <TableRow key={vaccination.id}>
                    <TableCell>{vaccination.animal?.id}</TableCell>
                    <TableCell>{vaccination.vaccinationType?.vaccineName}</TableCell>
                    <TableCell>{new Date(vaccination.dateAdministered).toLocaleDateString()}</TableCell>
                    <TableCell>{new Date(vaccination.nextDueDate).toLocaleDateString()}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
        )}  
    </div>
  );
}