import { useState, useEffect } from "react";
import MainLayout from "../../components/layout/MainLayout";
import type { MedicalRecord } from "@/types/types";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import { PlusIcon, PencilIcon, TrashIcon } from "@heroicons/react/24/outline";
import { getErrorMessage } from "@/services/fetchUtils";
import { MedicalRecordService } from "../../api/medicalRecord";

export default function VeterinarianView() {
  const [records, setRecords] = useState<MedicalRecord[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchMedicalRecords = async () => {
      try {
        setLoading(true);
        setError(null);
        const data = await MedicalRecordService.getAllMedicalRecords();
        setRecords(data);
      } catch (err) {
        setError(getErrorMessage(err));
      } finally {
        setLoading(false);
      }
    };

    console.log(records);


    fetchMedicalRecords();
  }, []);

  

  if (loading) {
    return (
      <MainLayout>
        <div className="text-center py-12">Loading...</div>
      </MainLayout>
    );
  }

  if (error) {
    return (
      <MainLayout>
        <div className="text-center py-12 text-red-600">Error: {error}</div>
      </MainLayout>
    );
  }

  return (
    <MainLayout>
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-semibold text-gray-800">Veterinarian Overview</h2>
        <button className="flex items-center px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-500 transition-colors">
          <PlusIcon className="h-5 w-5 mr-2" />
          Add Record
        </button>
      </div>

      {records.length === 0 ? (
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
              <TableHead>Cost</TableHead>
              <TableHead>Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {records.map((record) => (
              <TableRow key={record.id}>
                <TableCell>{record.id}</TableCell>
                <TableCell>{record.animal.name}</TableCell>
                <TableCell>{new Date(record.date).toLocaleDateString()}</TableCell>
                <TableCell>{record.diagnosis}</TableCell>
                <TableCell>{record.treatment}</TableCell>
                <TableCell>${record.cost.toFixed(2)}</TableCell>
                <TableCell>
                  <div className="flex gap-2">
                    <button className="text-blue-600 hover:text-blue-800">
                      <PencilIcon className="h-5 w-5" />
                    </button>
                    <button className="text-red-600 hover:text-red-800">
                      <TrashIcon className="h-5 w-5" />
                    </button>
                  </div>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      )}
    </MainLayout>
  );
}