import { useState, useEffect } from "react";
import type { Vaccination, VaccinationType, Animal } from "@/types/types";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { VaccinationService } from "@/api/vaccination";
import { AnimalService } from "@/api/animals";

type VaccinationFormData = {
  animalId: number | null;
  vaccinationTypeId: number | null;
  dateAdministered: string;
  nextDueDate: string;
};

type VaccinationFormDialogProps = {
  vaccination: Vaccination | null;
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: VaccinationFormData) => Promise<void>;
};

export default function VaccinationFormDialog({ vaccination, isOpen, onClose, onSubmit }: VaccinationFormDialogProps) {
  const [formData, setFormData] = useState<VaccinationFormData>({
    animalId: null,
    vaccinationTypeId: null,
    dateAdministered: "",
    nextDueDate: "",
  });

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [vaccinationTypes, setVaccinationTypes] = useState<VaccinationType[]>([]);
  const [animals, setAnimals] = useState<Animal[]>([]);

  useEffect(() => {
    if (vaccination) {
      setFormData({
        animalId: vaccination.animal?.id ?? null,
        vaccinationTypeId: vaccination.vaccinationType?.id ?? null,
        dateAdministered: new Date(vaccination.dateAdministered).toISOString().split("T")[0],
        nextDueDate: new Date(vaccination.nextDueDate).toISOString().split("T")[0],
      });
    } else {
      setFormData({
        animalId: null,
        vaccinationTypeId: null,
        dateAdministered: "",
        nextDueDate: "",
      });
    }
  }, [vaccination]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [types, animalsData] = await Promise.all([VaccinationService.getAllVaccinationTypes(), AnimalService.getAnimals()]);
        setVaccinationTypes(types);
        setAnimals(animalsData.content);
      } catch (err) {
        console.error("Failed to fetch data", err);
        setError("Failed to load animals and vaccination types.");
      }
    };
    if (isOpen) fetchData();
  }, [isOpen]);

  useEffect(() => {
    if (formData.dateAdministered && formData.vaccinationTypeId) {
      const selectedType = vaccinationTypes.find((t) => t.id === formData.vaccinationTypeId);
      if (selectedType?.durationMonths) {
        const date = new Date(formData.dateAdministered);
        date.setMonth(date.getMonth() + selectedType.durationMonths);
        setFormData((prev) => ({
          ...prev,
          nextDueDate: date.toISOString().split("T")[0],
        }));
      }
    }
  }, [formData.dateAdministered, formData.vaccinationTypeId, vaccinationTypes]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === "animalId" || name === "vaccinationTypeId" ? Number(value) : value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.animalId || !formData.vaccinationTypeId) {
      setError("Please select an animal and vaccination type.");
      return;
    }
    setIsSubmitting(true);
    setError(null);
    try {
      await onSubmit(formData);
      onClose();
    } catch (err) {
      console.error("Failed to submit vaccination data", err);
      setError("Failed to submit vaccination data.");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-lg p-6">
        <DialogHeader>
          <DialogTitle>{vaccination ? "Edit Vaccination" : "Add Vaccination"}</DialogTitle>
          <DialogDescription>
            {vaccination ? "Update the details of the vaccination." : "Fill in the details to add a new vaccination."}
          </DialogDescription>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-4 mt-4">
          <div>
            <Label htmlFor="animalId">Animal</Label>
            <select
              id="animalId"
              name="animalId"
              value={formData.animalId ?? ""}
              onChange={handleChange}
              className="w-full px-3 py-2 border rounded"
              required
            >
              <option value="" disabled>
                Select an animal
              </option>
              {animals.map((animal) => (
                <option key={animal.id} value={animal.id}>
                  {animal.name}
                </option>
              ))}
            </select>
          </div>
          <div>
            <Label htmlFor="vaccinationTypeId">Vaccination Type</Label>
            <select
              id="vaccinationTypeId"
              name="vaccinationTypeId"
              value={formData.vaccinationTypeId ?? ""}
              onChange={handleChange}
              className="w-full px-3 py-2 border rounded"
              required
            >
              <option value="" disabled>
                Select a type
              </option>
              {vaccinationTypes.map((type) => (
                <option key={type.id} value={type.id}>
                  {type.vaccineName} ({type.durationMonths} months)
                </option>
              ))}
            </select>
          </div>
          <div>
            <Label htmlFor="dateAdministered">Date Administered</Label>
            <Input type="date" id="dateAdministered" name="dateAdministered" value={formData.dateAdministered} onChange={handleChange} required />
          </div>
          <div>
            <Label htmlFor="nextDueDate">Next Due Date</Label>
            <Input type="date" id="nextDueDate" name="nextDueDate" value={formData.nextDueDate} onChange={handleChange} required />
            <p className="text-xs text-gray-500 mt-1">Auto-calculated based on vaccine type</p>
          </div>
          {error && <p className="text-red-500 text-sm">{error}</p>}
          <DialogFooter>
            <Button type="button" variant="outline" onClick={onClose} disabled={isSubmitting}>
              Cancel
            </Button>
            <Button type="submit" disabled={isSubmitting}>
              {isSubmitting ? "Submitting..." : vaccination ? "Update" : "Add Vaccination"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
